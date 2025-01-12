package com.wfa.middleware.utils;

import java.util.ArrayList;
import java.util.List;

import com.wfa.middleware.utils.api.IAsyncCallback;
import com.wfa.middleware.utils.api.IJoinable;
import com.wfa.middleware.utils.api.IJoined;

public class AsyncJoinedPromise<T extends IJoinable<T>> extends AsyncPromise<T> 
				implements IJoined<AsyncPromise<T>> {

	private IJoinable<AsyncPromise<T>> firstChildPromise;
	private IJoinable<AsyncPromise<T>> secondChildPromise;
	private IAsyncCallback<T> joinedCallback;
	
	protected AsyncJoinedPromise(IJoinable<AsyncPromise<T>> firstChildPromise, IJoinable<AsyncPromise<T>> secondChildPromise) {
		this.firstChildPromise = firstChildPromise;
		this.secondChildPromise = secondChildPromise;
		
		joinedCallback = new IAsyncCallback<T>(
				) {
			
			@Override
			public void onSuccess(T result) {
				checkAndNotifySuccess(result);	
			}

			@Override
			public void onFailure(T result) {
				isDone = true;
				fail(result);
			}};
		
		monitorChildren();
	}
	
	private void monitorChildren() {
		firstChildPromise.get().appendCallback(joinedCallback);
		secondChildPromise.get().appendCallback(joinedCallback);
	}
	
	private void checkAndNotifySuccess(T result) {
		if (firstChildPromise.get().hasSucceeded() && secondChildPromise.get().succeeded) {
			isDone = true;
			IJoined<T> joinedResult = firstChildPromise.get().getResult().joinTo(secondChildPromise.get().getResult());
			succeed(joinedResult.get());
		}
	}
	
	public static <T extends IJoinable<T>> AsyncJoinedPromise<T> getNewJoinedPromise(IJoinable<AsyncPromise<T>> firstPromise,
			IJoinable<AsyncPromise<T>> secondPromise) {
		return new AsyncJoinedPromise<T>(firstPromise, secondPromise);
	}

	@Override
	public List<AsyncPromise<T>> getJoinedElements() {
		List<AsyncPromise<T>> children = new ArrayList<AsyncPromise<T>>();
		children.add(firstChildPromise.get());
		children.add(secondChildPromise.get());
		return children;
	}

	@Override
	public AsyncPromise<T> get() {
		return this;
	}
}
