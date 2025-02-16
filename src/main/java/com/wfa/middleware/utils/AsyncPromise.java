package com.wfa.middleware.utils;

import java.util.ArrayList;
import java.util.List;

import com.wfa.middleware.utils.api.IAsyncCallback;

/**
 * A promise object representing a promise to notify callback on success/failure
 * of a task.
 * 
 * This promise will have to be concluded via application logic
 * 
 * author -> tortoiseDev
 * @param <T> T is the type of result object
 */

public class AsyncPromise<T> {
	
	protected List<IAsyncCallback<T>> callbacks;
	protected volatile boolean succeeded;
	protected volatile boolean isDone;
	protected T result;
	
	protected AsyncPromise() {
		callbacks = new ArrayList<IAsyncCallback<T>> ();
		succeeded = false;
		isDone = false;
	}
	
	public boolean hasSucceeded () {
		return succeeded;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public void succeed(T result) {
		if (!isDone()) {
			this.result = result;
			this.succeeded = true;
			for (IAsyncCallback<T> callback : callbacks) {
				callback.onSuccess(result);
			}
			
			this.succeeded= true; 
			this.isDone = true;
		}
	}
	
	public T getResult() {
		return result;
	}
	
	public void fail(T result) {
		if (!isDone) {
			this.result = result;
			for (IAsyncCallback<T> callback : callbacks) {
				callback.onFailure(result);
			}
			
			this.isDone = true;
		}
	}
	
	public void appendCallback(IAsyncCallback<T> callback) {
		callbacks.add(callback);
		if (this.isDone()) {
			if (this.hasSucceeded()) {
				callback.onSuccess(this.result);
			} else {
				callback.onFailure(this.result);
			}
		}
	}
		
	public static <T> AsyncPromise<T> getNewPromise() {
		return new AsyncPromise<T>();
	}
}
