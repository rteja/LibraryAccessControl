package com.isical.collections;

import java.util.Map;

import com.isical.database.IConnection;

public class BigMap<S, T> implements IMap<S, T> {

	IConnection db;
	Map<S, T> m;
	
	
	public BigMap()
	{
		
	}
	
	@Override
	public void put(S s, T t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T get(S s) {
		// TODO Auto-generated method stub
		return null;
	}

}
