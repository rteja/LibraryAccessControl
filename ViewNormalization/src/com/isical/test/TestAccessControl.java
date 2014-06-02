package com.isical.test;

import java.util.Vector;

import com.isical.graph.*;
import com.isical.ontology.*;

class ontoGraphNode extends DAGNode<Integer>
{

	ontoGraphNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector<DAGNode<Integer>> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

class ontoGraph extends DAG<Integer>
{
	DAGNode<Integer> root;
	
	public DAGNode<Integer> getRoot()
	{
		return root;
	}
}

public class TestAccessControl {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
}
