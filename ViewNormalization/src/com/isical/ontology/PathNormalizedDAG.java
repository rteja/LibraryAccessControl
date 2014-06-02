/**
 * 
 */
package com.isical.ontology;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.hp.hpl.jena.*;
import com.hp.hpl.jena.ontology.OntModel;

import com.isical.collections.IList;
import com.isical.collections.IMap;
import com.isical.collections.IVector;

import com.isical.graph.*;


/**
 * @author raghu
 *
 */

public class PathNormalizedDAG
{
	Vector<List<Integer>> normalizedPaths;
	Map<Integer, List<Integer>> nodeBucket;
	
	/* Path Normalization algorithm */
	List<Integer> currentPath;
	
	private void create_new_path()
	{
		currentPath = new ArrayList<Integer>();
		normalizedPaths.add(currentPath);	
	}
	
	private void compute_paths_r(DAGNode<Integer> gRoot)
	{		
		// add to a path	
		currentPath.add(gRoot.getId());
		
		List<Integer> l = null;
		if (nodeBucket.get(gRoot.getId()) == null)
			l = new ArrayList<Integer>();
		else
			l = nodeBucket.get(gRoot.getId());
		
		l.add(normalizedPaths.size() - 1);
		nodeBucket.put(gRoot.getId(), l);
		
		
		if (gRoot.getChildren().size() != 0 
				&& gRoot.getFlag() != 2 )			
		{
			gRoot.setFlag(1);			
			Vector<DAGNode<Integer>> children = gRoot.getChildren();	
			
			boolean firstChild = true;
			
			for (DAGNode<Integer> child : children )
			{
				if (firstChild)
				{
					compute_paths_r(child);
				}
				else
				{
					create_new_path();
					
					l.add(normalizedPaths.size() - 1);
					nodeBucket.put(gRoot.getId(), l);
					
					currentPath.add(gRoot.getId());
					compute_paths_r(child);					
				}				
					
				firstChild = false;
			}
		}
		else
		{
			gRoot.setFlag(2);
			create_new_path();			
		}
		
		gRoot.setFlag(2);
	}
	
	private void compute_paths(DAG<Integer> g)
	{
		create_new_path();
		compute_paths_r(g.getRoot()); //nodeId of root = 0;
	}
	/* End of Path Normalization algorithm */
	
	public void AddPath(List<Integer> path)
	{
		normalizedPaths.add(path);
	}
	
	public Integer getNoOfPaths()
	{
		return normalizedPaths.size();
	}
	
	public List<Integer> getPath(Integer pathId)
	{
		return normalizedPaths.get(pathId);			
	}
	
	public Vector<List<Integer>> getPaths()
	{
		return normalizedPaths;
	}
	
	public List<Integer> getPathList(Integer nodeId)
	{
		return nodeBucket.get(nodeId);
	}
	
	public PathNormalizedDAG(DAG<Integer> g)
	{
		// Initialize normlizedPaths and nodeBucket first
		nodeBucket = new HashMap<Integer, List<Integer>>();
		// Compute the Normalized pathsets.
		compute_paths(g);
	}
}
