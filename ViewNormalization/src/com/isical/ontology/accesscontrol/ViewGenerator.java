package com.isical.ontology.accesscontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


import com.isical.graph.*;
import com.isical.ontology.*;

public class ViewGenerator {
	
	private PathNormalizedDAG pg = null; 
	private Map<Integer, Integer> authorizations = null;
	private Map<Integer, Integer> visitedAnchorNodes = null;
	private Map<Integer, Integer> permissions ;
	
	private Integer negPerm = 0;
	private Integer posPerm = 1;

	ViewGenerator(DAG<Integer> g, Map<Integer, Integer> perms)
	{
		pg = new PathNormalizedDAG(g);		
		authorizations = perms;
	}
	
	ViewGenerator(PathNormalizedDAG g, Map<Integer, Integer> perms)
	{
		pg = g;
		authorizations = perms;
		
		visitedAnchorNodes = new TreeMap<Integer, Integer>();				
	}
	
	public void SetAuthorizations(Map<Integer, Integer> perms)
	{
		authorizations = perms;
	}
	
	private List<Integer> get_negetive_nodes()
	{
		List<Integer> nodes = new ArrayList<Integer>();
		
		//dont know if this will be a performance issue
		Set<Integer> authNodes = authorizations.keySet();
		
		for (Integer node : authNodes)
			if (authorizations.get(node) == negPerm)
				authNodes.add(node);
					
		return nodes;
	}
	
	private List<Integer> get_positive_nodes()
	{
		//TODO implement get_positive_nodes
		return null;
	}
	
	private Set<Integer> get_paths_for_node(Integer node)
	{
		
		Set<Integer> pathList = new HashSet();
		pathList.addAll(pg.getPathList(node));		
		
		return pathList;
	}
	
	private Set<Integer> get_paths_for_nodes(List<Integer> nodes)
	{
		Set<Integer> paths = new TreeSet<Integer>();
		
		for (Integer node : nodes)
			paths.addAll(get_paths_for_node(node));
		
		return paths;
	}
	
	// returns a set of anchor nodes that are decisively negative.
	private Set<Integer> assign_perms(Integer pathId)
	{
		List<Integer> path = pg.getPath(pathId);
		Set<Integer> negAnchorNodes = new TreeSet<Integer>();								
		Set<Integer> scanPaths = new HashSet<Integer>(); scanPaths.add(pathId);
							
		while (true)
		{
			boolean skipNodes = true;
			Integer curPerm = 1;				
			
			scanPaths.remove(pathId);

			for (Integer nodeId : path)
			{
				if (!authorizations.containsKey(nodeId)  )
				{
					if (skipNodes 
							|| (permissions.containsKey(nodeId) 
									&& permissions.get(nodeId) >= 0) )
						continue;
				}
				else 
				{
					skipNodes = false;
					curPerm = authorizations.get(nodeId);
				}
				
				// check if node is an anchor node and break accordingly
				List<Integer> nodePaths = pg.getPathList(nodeId);
				
				boolean isAnchor = false;
				
				List<Integer> addScanPaths = null;
				int anchorDegree = 0;
				if (!visitedAnchorNodes.containsKey(nodeId) 	)
				{
					if (curPerm == negPerm)
					{						
						for (Integer pId : nodePaths)
						{
							
							if (pId == pathId)
								continue;
							
							List<Integer> p = pg.getPath(pId);
							if (p.get(0) != nodeId)
							{
								isAnchor = true;
								anchorDegree++;
							}
	
						}
						permissions.put(nodeId, -1);
					}
					else //curPerm is positive
					{
						addScanPaths = new ArrayList<Integer>();
						for (Integer pId : nodePaths)
						{
							List<Integer> p = pg.getPath(pId);					
							if (p.get(0) == nodeId)
							{
								addScanPaths.add(pId);
							}
						}
					}
					
					/* this may not be needed */
					if (isAnchor)					
						visitedAnchorNodes.put(nodeId, anchorDegree);		
				}
				else // the node is an anchor 
				{
					isAnchor = true;
					Integer visits = permissions.get(nodeId) - 1;
					
					if (visits == -1 * visitedAnchorNodes.get(nodeId))
						permissions.put(nodeId, negPerm);
					else
						permissions.put(nodeId, visits - 1);
					
					negAnchorNodes.add(nodeId);
				}				
				
				if (curPerm == posPerm
						|| !isAnchor )
				{
					permissions.put(nodeId, curPerm);
					scanPaths.addAll(addScanPaths);
				}						
				else //we cannot determine permissions beyond this point in this path so break
					break;
				
		
			}
			
			if (scanPaths.size() == 0 )
				break;
			
			pathId = scanPaths.iterator().next();
			path = pg.getPath(pathId);
		}		
				
		return negAnchorNodes;		
	}
	
	/* Path Normal view creation algorithm */
	public PathNormalizedDAG ComputeView()
	{
		PathNormalizedDAG view = null;		
		
		List<Integer> negNodes = get_negetive_nodes();
		
		permissions = new HashMap<Integer, Integer>();
		
		while (negNodes.size() != 0)
		{
			Set<Integer> negPathIds = get_paths_for_nodes(negNodes);
			
			for (Integer pathId : negPathIds)
			{
				negNodes.addAll(assign_perms(pathId));				
			}			
		}				
		return view;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}