/**
 * 
 */
package com.isical.collections;

import java.sql.SQLException;

/**
 * @author raghu
 *
 */
public interface IList<T> 
{	
	public abstract void add(T t) throws Exception;
	public abstract T get(int Id) throws Exception;
	public abstract int size() throws Exception;
	public abstract void clear() throws Exception;
}
