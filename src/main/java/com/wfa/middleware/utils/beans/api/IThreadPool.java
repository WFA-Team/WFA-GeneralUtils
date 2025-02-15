package com.wfa.middleware.utils.beans.api;

import java.util.Queue;

public interface IThreadPool {
	void submitRunnableQueue(Queue<Runnable> runnableQueue) throws IllegalStateException;
	void setMaxParallelism(int parallelism) throws IllegalStateException;
	void start() throws IllegalStateException;
	void pause() throws IllegalStateException;
	void stop() throws IllegalStateException;
	int getRunningThreadCount();
	Queue<Runnable>getRunnableQueue();
}
