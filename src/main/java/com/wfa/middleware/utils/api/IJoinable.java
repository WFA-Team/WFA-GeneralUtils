package com.wfa.middleware.utils.api;

// Could be simplified to joinTo(T joinTo), but I am reserving it for options
// and also liking the added complexity
public interface IJoinable<T> {
	IJoined<T> joinTo(IJoinable<T> joinTo);
	T get();
}
