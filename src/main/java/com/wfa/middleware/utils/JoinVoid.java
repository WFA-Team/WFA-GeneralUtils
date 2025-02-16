package com.wfa.middleware.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wfa.middleware.utils.api.IJoinable;
import com.wfa.middleware.utils.api.IJoined;
import com.wfa.middleware.utils.api.IJoinedJoinable;

/* Void result type but compatible with join framework
 * author -> tortoiseDev
 */
public class JoinVoid implements IJoinedJoinable<JoinVoid>{
	private static JoinVoid JoinVoidInstance = new JoinVoid();
	
	@Override
	public List<JoinVoid> getJoinedElements() {
		return new ArrayList<JoinVoid>(Arrays.asList(JoinVoidInstance));
	}

	@Override
	public JoinVoid get() {
		return JoinVoidInstance;
	}

	@Override
	public IJoined<JoinVoid> joinTo(IJoinable<JoinVoid> joinTo) {
		return JoinVoidInstance;
	}

	@Override
	public IJoinedJoinable<JoinVoid> joinableJoinTo(IJoinable<JoinVoid> joinTo) {
		return JoinVoidInstance;
	}

}
