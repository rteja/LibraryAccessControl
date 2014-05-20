/**
 * An interface for a DAG which has all its nodes connected from a given root.
 */
package com.isical.graph;


import java.util.Vector;

import com.isical.graph.DAGNode;

/**
 * @author raghu
 *
 */

public abstract class DAG<NodeLabel_t> {
	DAGNode<NodeLabel_t> root;
	
	public DAGNode<NodeLabel_t> getRoot()
	{
		return root;
	}
	
	
}
