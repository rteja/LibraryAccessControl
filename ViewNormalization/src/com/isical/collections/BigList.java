package com.isical.collections;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import com.isical.database.IConnection;

public class BigList<T> implements IList<T> {

	IConnection db;
	List<T> l;
	
	int dbListStart;
	int bufferSize;
	int listSize;
	String listTblName;
	
		
	public BigList(IConnection db, int bufferSize, String listTblName) throws SQLException
	{
		this.db = db;
		this.listTblName = listTblName;
		this.bufferSize = bufferSize;
		
		l = new ArrayList<T>();
		
		//Query to check if listId exists otherwise create one.		
		Statement stmt = db.getConnection().createStatement();
		
		DatabaseMetaData md = db.getConnection().getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		while (rs.next()) 
		{
		  System.out.println(rs.getString(3));
		  
		  if (rs.getString(3).compareTo(listTblName) == 0) //table already exists
			  return;
		  
		}
		
		
		String sql = "CREATE TABLE " + listTblName 
				+ "( id INTEGER not NULL, " 
				+ "  item INTEGER not NULL," 
				+ "  PRIMARY KEY(id) );";
		
		stmt.executeUpdate(sql);
		
		
		return;
	}
	
	public BigList()
	{
		db = null;
		l = new ArrayList<T>();
		
	}
	
	private void batchUpdate() throws SQLException
	{
		Statement stmt = db.getConnection().createStatement();
		
		String sql = "SELECT count(*) FROM "
				+ listTblName + ";";
		
		ResultSet rs = stmt.executeQuery(sql); rs.next();
		
		int dbListSize = rs.getInt(1);
		int it = 0;
		
		for (T item : l)
		{
			it++;
					
			sql = "INSERT INTO " 
					+ listTblName  
					+ " (id, item) VALUES" 
					+ " (" + (dbListSize + it)  + ","  + item.toString()  + ")";
			stmt.executeUpdate(sql);									
		}
		
	}
	
	@Override
	public void add(T t) throws SQLException {
		// TODO Auto-generated method stub
		l.add(t);
		
		// Insert Query
		if (l.size() >= bufferSize)
		{
			batchUpdate();			
			l.clear();
		}
		return;
	}

	public Integer getInt(int Id) throws SQLException
	{
			
		int dbListSize = dbSize();
		
		// check if Id is within current buffer
		// else fetch the buffer and return the element.
		if (Id <= ( dbListSize + l.size()) && Id > dbListSize )
		{
			Id = Id - dbListSize;
			
			return Integer.parseInt(l.get(Id).toString());
		}
		
		Statement stmt = db.getConnection().createStatement();
		String sql = "SELECT item from " 
				+ listTblName  + "WHERE "
				+ "id=" + Id + ";";
		
		ResultSet rs = stmt.executeQuery(sql); 
		
		int it = 0;
		while (it < Id)		
			rs.next();
		
		return rs.getInt(1);
	}
	
	@Override
	public T get(int Id) throws Exception {
		// TODO Auto-generated method stub
		
		//throw (new Exception());
		
		
		int dbListSize = dbSize();
		
		// check if Id is within current buffer
		// else fetch the buffer and return the element.
		if (Id <= ( dbListSize + l.size()) && Id > dbListSize )
		{
			Id = Id - dbListSize - 1;
			
			return l.get(Id);
		}
		
		Statement stmt = db.getConnection().createStatement();
		String sql = "SELECT item "
				+ "FROM " + listTblName
				+ " WHERE " 
				+ "id=" + Id + ";";
		
		ResultSet rs = stmt.executeQuery(sql); rs.next();			
		
		
		return (T)rs.getObject(1);
		
		
	}
	
	
	private int dbSize() throws SQLException 
	{
		Statement stmt = db.getConnection().createStatement();
		
		String sql = "SELECT count(*) FROM "
				+ listTblName + ";";
		
		ResultSet rs = stmt.executeQuery(sql); rs.next();
		
		return rs.getInt(1);
		
	}

	@Override
	public int size() throws SQLException 
	{		
		
		return (dbSize() + l.size());
	}

	@Override
	public void clear() throws SQLException {
		Statement stmt = db.getConnection().createStatement();
		
		String sql = "DELETE FROM " + listTblName + " WHERE id = id;";  
		
		stmt.executeUpdate(sql);
		
		return;
				
		
	}

}

/*
 * 

public void insertBatch(final List<Record > records ) {

    String query = "INSERT INTO table (id, name, value) VALUES (?, ?, ?)";


    GenericDAO.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {


        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
              Record record = records .get(i);
              ps.setInt(1, record.id);
              ps.setString(2, record.name);
              ps.setInt(3, record.value);
        }

        @Override
        public int getBatchSize() {
            return records.size();
        }
    });
}

*/

