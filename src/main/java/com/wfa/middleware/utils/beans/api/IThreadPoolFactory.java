package com.wfa.middleware.utils.beans.api;

public interface IThreadPoolFactory <T extends Runnable> {
	IThreadPool<T> getNewThreadPool();
}
