package com.ebksoft.fightbooking.utils;

//public abstract class DataRequestCallback<E> {
//	private E mResult;
//
//	public void setResult(E result) {
//		mResult = result;
//	}
//
//	public abstract void onResult(E result, boolean continueWaiting);
//
//	public void onResult(boolean continueWaiting) {
//		onResult(mResult, continueWaiting);
//	}
//
//}

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class DataRequestCallback<T extends Object> {
	private T mResult;
	private Type type;

	public DataRequestCallback() {
		type = ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public Type getType() {
		return type;
	}

	public void setResult(T result) {
		mResult = result;
	}

	public abstract void onResult(T result, boolean continueWaiting);

	public void onResult(boolean continueWaiting) {
		onResult(mResult, continueWaiting);
	}

}
