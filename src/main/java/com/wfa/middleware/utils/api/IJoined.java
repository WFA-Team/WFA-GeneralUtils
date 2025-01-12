package com.wfa.middleware.utils.api;

import java.util.List;

public interface IJoined <T> {
	List<T> getJoinedElements();
	T get();
}
