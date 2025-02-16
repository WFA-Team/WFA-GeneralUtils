package com.wfa.middleware.utils;

import java.util.ArrayList;
import java.util.List;

import com.wfa.middleware.utils.api.IAsyncCallback;
import com.wfa.middleware.utils.api.IJoinable;
import com.wfa.middleware.utils.api.IJoined;

/**
 * This is resultant promise when you join 2 joinable promises having joinable results.
 * It will have 2 child promises out of which if both succeed then this promise will automatically succeed
 * And even if one of them fails, this one will fail too.
 * 
 * @author tortoiseDev
 * @param <T>
 */
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
		if (firstChildPromise.get().hasSucceeded() && secondChildPromise.get().hasSucceeded()) {
			isDone = true;
			IJoined<T> joinedResult = firstChildPromise.get().getResult().joinTo(secondChildPromise.get().getResult());
			succeed(joinedResult.get());
		}
	}
	
	public static <T extends IJoinable<T>> IJoined<AsyncPromise<T>> getNewJoinedPromise(IJoinable<AsyncPromise<T>> firstPromise,
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
