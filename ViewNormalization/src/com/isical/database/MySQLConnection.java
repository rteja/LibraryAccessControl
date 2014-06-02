package com.isical.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.omg.PortableInterceptor.USER_EXCEPTION;

public class MySQLConnection implements IConnection {
	
	private String DB_URL;
	
	private String dbUsername;
	private String dbPassword;
	
	private String dbHostName;
	private String dbName;
	
	
	protected Connection con;
	
	public MySQLConnection(String hostname, String db, String username, String password) throws ClassNotFoundException, SQLException
	{
		dbUsername = username;
		dbPassword = password;
		dbName = db;
		dbHostName = hostname;				
		
		DB_URL = "jdbc:mysql://" + hostname +  "/" + db;
		
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(DB_URL, username, password);
	}
	
	@Override
	public Connection getConnection() 
	{
		// TODO Auto-generated method stub
		return con;
	}

	@Override
	public void open() throws SQLException 
	{
		// TODO Auto-generated method stub
	
		if (con.isClosed() )
		{
			con = DriverManager.getConnection(DB_URL, dbUsername, dbPassword);
		}
				
		return;
	}

	@Override
	public void close() throws SQLException 
	{
		if (!con.isClosed())
			con.close();
					
		return;
	}

	@Override
	public boolean state() throws SQLException 
	{			
		
		return con.isClosed();
	}

}
