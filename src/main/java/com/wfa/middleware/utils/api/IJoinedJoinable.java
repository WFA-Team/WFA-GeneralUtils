package com.wfa.middleware.utils.api;

public interface IJoinedJoinable<T> extends IJoined<T>, IJoinable<T>{
	IJoinedJoinable<T> joinableJoinTo(IJoinable<T> joinTo);
}
