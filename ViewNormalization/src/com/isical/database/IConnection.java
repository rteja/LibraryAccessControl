package com.isical.database;

import java.sql.*;

public interface IConnection 
{	
	
	public abstract Connection getConnection();	
	public abstract void open() throws Exception;
	public abstract void close() throws Exception;
	public abstract boolean state() throws Exception;
	

}
