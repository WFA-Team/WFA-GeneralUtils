package com.wfa.middleware.utils;

import com.wfa.middleware.utils.api.IJoinable;
import com.wfa.middleware.utils.api.IJoined;
import com.wfa.middleware.utils.api.IJoinedJoinable;

public class AsyncJoinedJoinablePromise<T extends IJoinedJoinable<T>> extends AsyncJoinedPromise<T> 
				implements IJoinedJoinable<AsyncPromise<T>>{

	protected AsyncJoinedJoinablePromise(IJoinable<AsyncPromise<T>> firstChildPromise,
			IJoinable<AsyncPromise<T>> secondChildPromise) {
		super(firstChildPromise, secondChildPromise);
	}

	@Override
	public IJoined<AsyncPromise<T>> joinTo(IJoinable<AsyncPromise<T>> joinTo) {
		return AsyncJoinedPromise.getNewJoinedPromise(this, joinTo);
	}

	@Override
	public IJoinedJoinable<AsyncPromise<T>> joinableJoinTo(IJoinable<AsyncPromise<T>> joinTo) {
		return getNewJoinedJoinablePromise(this, joinTo);
	}
	
	public static <T extends IJoinedJoinable<T>> IJoinedJoinable<AsyncPromise<T>> getNewJoinedJoinablePromise(IJoinable<AsyncPromise<T>> firstChildPromise,
			IJoinable<AsyncPromise<T>> secondChildPromise) {
		return new AsyncJoinedJoinablePromise<T>(firstChildPromise, secondChildPromise);
	}

}
