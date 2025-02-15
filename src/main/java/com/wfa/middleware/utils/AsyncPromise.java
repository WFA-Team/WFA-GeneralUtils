package com.wfa.middleware.utils;

import java.util.ArrayList;
import java.util.List;

import com.wfa.middleware.utils.api.IAsyncCallback;
import com.wfa.middleware.utils.api.IJoinable;

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
	protected boolean succeeded;
	protected boolean isDone;
	protected T result;
	
	protected AsyncPromise() {
		callbacks = new ArrayList<IAsyncCallback<T>> ();
		succeeded = false;
	}
	
	public boolean hasSucceeded () {
		return succeeded;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public void succeed(T result) {
		this.result = result;
		
		for (IAsyncCallback<T> callback : callbacks) {
			callback.onSuccess(result);
		}
	}
	
	public T getResult() {
		return result;
	}
	
	public void fail(T result) {
		this.result = result;
		for (IAsyncCallback<T> callback : callbacks) {
			callback.onFailure(result);
		}		
	}
	
	public void appendCallback(IAsyncCallback<T> callback) {
		callbacks.add(callback);
	}
		
	public static <T extends IJoinable<T>> AsyncPromise<T> getNewPromise() {
		return new AsyncPromise<T>();
	}
}
