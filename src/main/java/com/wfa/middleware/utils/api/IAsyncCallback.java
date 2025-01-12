package com.wfa.middleware.utils.api;

public interface IAsyncCallback<T> {
	void onSuccess(T result);
	void onFailure(T result);
}
