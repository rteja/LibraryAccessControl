package com.isical.graph;

import java.util.Vector;

import com.isical.graph.DAGNode;

public abstract class DAGNode<NodeLabel_t> {
	NodeLabel_t nodeId;
	Integer flag;
	
	DAGNode()
	{
		flag = 0;
	}
	
	public NodeLabel_t getId()
	{
		return nodeId;
	}
	
	public abstract Vector<DAGNode<NodeLabel_t>> getChildren();
	
	public Integer getFlag()
	{
		return flag;
	}
	
	public void setFlag(int f)
	{
		flag = f;
	}
	
}