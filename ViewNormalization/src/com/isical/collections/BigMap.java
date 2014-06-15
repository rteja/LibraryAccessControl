package com.isical.collections;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.isical.database.IConnection;

public class BigMap<S, T> implements IMap<S, T> {

	IConnection db;
	Map<S, T> m;
	
	String mapTblName;
	int bufferSize;
	
	public enum STORAGE
	{
		PERSISTENT, TEMPORARY, INMEMORY
	}
	
	STORAGE storeType; 
	
	public BigMap(IConnection db, int bufferSize, String mapTblName, STORAGE storeType) throws SQLException
	{
		m = new HashMap<S, T>();
		
		this.db = db;
		this.mapTblName = mapTblName;
		this.bufferSize = bufferSize;
		this.storeType = storeType;
		
		if (storeType == STORAGE.INMEMORY)
		{
			db = null;
			m = new HashMap<S, T>();
			
			return;
		}
		
		//prepare a statement to execute
		Statement stmt = db.getConnection().createStatement();
		
		DatabaseMetaData md = db.getConnection().getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		
		while (rs.next())
		{
			  System.out.println("DEBUG" + rs.getString(3));
			  
			  if (rs.getString(3).compareTo(mapTblName) == 0) //table already exists
				  return;
		}
		
		String sql = "CREATE TABLE " + mapTblName 
				+ "( k INTEGER not NULL, " 
				+ "  value INTEGER not NULL," 
				+ "  PRIMARY KEY(k) );";
		
		stmt.executeUpdate(sql);
		
	}
	
	

	public BigMap()
	{
		db = null;
		m = new HashMap<S, T>();
		
		storeType = STORAGE.INMEMORY;
	}
	
	public void clear() throws SQLException 
	{
		Statement stmt = db.getConnection().createStatement();
		
		String sql = "DELETE FROM " + mapTblName + " WHERE k = k;";
		stmt.executeUpdate(sql);
		return;
	}
	
	private void batchUpdate() throws SQLException
	{
		Statement stmt = db.getConnection().createStatement();
		
		Set<S> keys = m.keySet();
		
		
		for (S key : keys)
		{
			String sql = "DELETE FROM " 
					+ mapTblName 
					+ " WHERE k=" + key;
			
			stmt.executeUpdate(sql);
			
			sql = "INSERT INTO " 
					+ mapTblName
					+ " (k, value) VALUES" 
					+ " (" + key + "," + m.get(key) + ")";
			
			stmt.executeUpdate(sql);
	
		}
	}
	
	@Override
	public void put(S s, T t) throws SQLException {
		// TODO Auto-generated method stub
		
		//first delete the already existing key and then insert
		m.put(s, t);
		
		if (m.size() > bufferSize)
		{
			batchUpdate();
		}
	}

	@Override
	public T get(S s) throws SQLException {
		// TODO Auto-generated method stub
		Statement stmt = db.getConnection().createStatement();
		
		T item = m.get(s);
		
		if (item != null)
			return item;
		
		String sql = "SELECT * FROM "
				+ mapTblName
				+ " WHERE k=" + s;
		
		ResultSet rs = stmt.executeQuery(sql); 
		
		while (rs.next())
		{
			item = (T) rs.getObject(1);
		}

		
		return item;
	}
	
	protected void finalize() throws SQLException
	{
		if (storeType == STORAGE.TEMPORARY)
		{
			if (db != null)
			{
				String sql = "DROP TABLE " + mapTblName;				
				Statement stmt = db.getConnection().createStatement();				
				stmt.executeUpdate(sql);
				
			}
		}
	}
	
}
