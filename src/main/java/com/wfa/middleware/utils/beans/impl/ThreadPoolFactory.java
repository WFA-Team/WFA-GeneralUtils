package com.wfa.middleware.utils.beans.impl;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wfa.middleware.utils.beans.api.IThreadPool;
import com.wfa.middleware.utils.beans.api.IThreadPoolFactory;

@Component
public class ThreadPoolFactory<T extends Runnable> implements IThreadPoolFactory<T> {
	private ObjectProvider<IThreadPool<T>> threadPoolProvider;
	
	@Autowired
	public ThreadPoolFactory(ObjectProvider<IThreadPool<T>> threadPoolProvider) {
		this.threadPoolProvider = threadPoolProvider;
	}
	 
	@Override
	public IThreadPool<T> getNewThreadPool() {
		return threadPoolProvider.getObject(); // creates new instance every time
	}
}
