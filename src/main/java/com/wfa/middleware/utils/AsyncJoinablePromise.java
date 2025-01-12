package com.wfa.middleware.utils;

import java.util.ArrayList;
import java.util.List;

import com.wfa.middleware.utils.api.IAsyncCallback;
import com.wfa.middleware.utils.api.IJoinable;
import com.wfa.middleware.utils.api.IJoined;
import com.wfa.middleware.utils.api.IJoinedJoinable;

/**
 * A promise that can be joined with other promises of its type
 * 
 * author -> tortoiseDev
 * @param <T> T is the type of result object
 */

public class AsyncJoinablePromise<T extends IJoinable<T>> extends AsyncPromise<T> 
				implements IJoinable<AsyncPromise<T>> {	
	
	protected AsyncJoinablePromise() {
		callbacks = new ArrayList<IAsyncCallback<T>> ();
		succeeded = false;
	}
	
	public static <T extends IJoinable<T>> AsyncJoinablePromise<T> getNewPromise() {
		return new AsyncJoinablePromise<T>();
	}

	@Override
	public IJoined<AsyncPromise<T>> joinTo(IJoinable<AsyncPromise<T>> joinTo) {
		return AsyncJoinedPromise.getNewJoinedPromise(this, joinTo);
	}

	@Override
	public AsyncPromise<T> get() {
		return this;
	}
	
	public static <T extends IJoinable<T>> AsyncJoinablePromise<T> getNewJoinablePromise() {
		return new AsyncJoinablePromise<T>();
	}
}
