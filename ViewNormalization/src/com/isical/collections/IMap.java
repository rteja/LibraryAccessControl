package com.isical.collections;


public interface IMap<S,T> 
{
	public abstract void put(S s, T t) throws Exception;
	public abstract T get(S s) throws Exception;
}
