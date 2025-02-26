package com.wfa.middleware.utils.beans.impl;

import java.lang.Thread.State;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.springframework.stereotype.Component;

import com.wfa.middleware.utils.PlayType;
import com.wfa.middleware.utils.beans.api.IThreadPool;

/* Architecture of thread pool. We first have a master thread, this master thread will only inspect(peek) the
 * runnable queue until it finds a valid entry that can be executed. When, it finds, it will want a worker
 * thread to actually dequeue(poll) this task and execute it. All the worker threads by default are waiting on their
 * own runnable (which has logic to dequeue and run task from queue if there is any). Now this master thread in the event 
 * of a pending task will notify the runnable of any idle working thread. As a result the working thread will start, dequeue
 * the pending task and execute it.
 * 
 * author -> tortoiseDev
 */
@Component
public class ThreadPool<T extends Runnable> implements IThreadPool<T>{
	private static final int DEFAULT_PARALLELISM = 32;
	private int maxParallelism;
	private Queue<T> queue;
	private volatile PlayType poolState;
	private Map<Thread, Runnable> workerThreads;
	private Thread masterThread; // queue polling thread
	private Runnable masterThreadRunnable;
	
	public ThreadPool() {
		maxParallelism = DEFAULT_PARALLELISM;
		poolState = PlayType.NOT_STARTED;
		workerThreads = new HashMap<Thread, Runnable>();
	}
	
	private void initializeMasterThread() {
		masterThread = new Thread(new Runnable() {
			@SuppressWarnings("incomplete-switch")
			@Override
			public void run() {
				boolean terminate = false;
				while(!terminate) {
					switch(poolState) {
					case PlayType.STARTED:
						if (peekQueue() != null) {
							Thread workerThread = getIdleWorkerThread();
							while (workerThread == null) {
								workerThread = getIdleWorkerThread();
							}
							
							awakenWhicheverThreadOnRunnable(workerThreads.get(workerThread));
						}
						break;
						
					case PlayType.PAUSED:
						pauseCurrentThreadOnRunnable(this);
						break;
						
					case PlayType.STOPPED:
						terminate = true;
						break;					
					}
				}
			}
		});
		
		masterThread.start();
	}
	
	private Thread getIdleWorkerThread() {
		for (Thread workerThread : workerThreads.keySet()) {
			if (workerThread.getState().equals(State.WAITING)) {
				return workerThread;
			}
		}
		
		return null;
	}
		
	private static void awakenWhicheverThreadOnRunnable(Runnable runnable) {
		synchronized(runnable) {
			runnable.notify();
		}
	}
	
	private static void pauseCurrentThreadOnRunnable(Runnable runnable) {
		synchronized(runnable) {
			try {
				runnable.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private synchronized Runnable peekQueue() {
		return queue.peek();
	}
	
	private synchronized Runnable pollQueue() {
		return queue.poll();
	}
	
	@Override
	public void submitRunnableQueue(Queue<T> runnableQueue) throws IllegalStateException {
		if (poolState.equals(PlayType.STARTED)) {
			throw new IllegalStateException("Not allowed to change queue if pool started");
		}
		
		this.queue = runnableQueue;
	}

	@Override
	public void setMaxParallelism(int parallelism) {
		if (poolState.equals(PlayType.STARTED) || poolState.equals(PlayType.PAUSED)) {
			throw new IllegalStateException("Not allowed to change parallelism once started");
		}
		
		maxParallelism = parallelism;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void start() throws IllegalStateException {
		if (poolState.equals(PlayType.STARTED)) {
			throw new IllegalStateException("Pool has already been started");
		}
		
		poolState = PlayType.STARTED; // first change state for threads
		
		switch(poolState) {
		case PlayType.STOPPED:
		case PlayType.NOT_STARTED:
			prepareWorkerThreads();
			initializeMasterThread();
			assert((workerThreads.size() + 1) == maxParallelism);
			break;
		case PlayType.PAUSED:
			awakenWhicheverThreadOnRunnable(masterThreadRunnable);
			break;
		}
	}

	@Override
	public void pause() throws IllegalStateException {
		if (!poolState.equals(PlayType.STARTED)) {
			throw new IllegalStateException("Cannot pause something that hasn't been started");
		}
		
		poolState = PlayType.PAUSED;	
	}

	@Override
	public void stop() throws IllegalStateException {
		if (poolState.equals(PlayType.STOPPED) || poolState.equals(PlayType.NOT_STARTED)) {
			throw new IllegalStateException("Cannot stop what is already stopped.");
		}

		poolState = PlayType.STOPPED;

		// Awaken worker threads so that they terminate
		for (Runnable runnable : workerThreads.values()) {
			awakenWhicheverThreadOnRunnable(runnable);
		}
		
		workerThreads.clear();
		masterThread = null;
	}

	@Override
	public int getRunningThreadCount() {
		int count = 0;
		for (Thread workerThread : workerThreads.keySet()) {
			if (workerThread.getState().equals(State.RUNNABLE)) {
				count++;
			}
		}
		
		return count;
	}
	
	private void prepareWorkerThreads() {	
		for (int i = 0; i < maxParallelism - 1 /*Reserve one entry for master thread*/; i ++) {
			Runnable workerThreadRunnable =  new Runnable() {
				@SuppressWarnings("incomplete-switch")
				@Override
				public void run() {
					boolean terminate = false;
					while(!terminate) {
						switch(poolState) {
						case PlayType.STARTED:
							Runnable runnable = pollQueue();
							if (runnable != null) {
								runnable.run();
							} else {
								pauseCurrentThreadOnRunnable(this);
							}
							break;
						case PlayType.PAUSED:
							pauseCurrentThreadOnRunnable(this);
							break;
						case PlayType.STOPPED:
							terminate = true;
							break;
						}
					}
				}
			};
				
			Thread thread = new Thread(workerThreadRunnable);
			workerThreads.put(thread, workerThreadRunnable);
			thread.start();
		}
	}

	@Override
	public Queue<T> getRunnableQueue() {
		return queue;
	}
}
