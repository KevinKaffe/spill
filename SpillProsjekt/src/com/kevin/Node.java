package com.kevin;

public class Node {
	private boolean disabled, goal, enabled;
	private Node[] nodes= new Node[4];
	private Node parent;
	private int x, y;
	public Node(boolean disabled, int x, int y)
	{
		this.x=x;
		this.y=y;
		parent = this;
		this.disabled=disabled;
		enabled=false;
		goal=false;

	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public void setRelations(Node left, Node up, Node right, Node down)
	{
		nodes[0]=left;
		nodes[1]=up;
		nodes[2]=right;
		nodes[3]=down;
	}
	public String toString()
	{
		return "OK";
	}
	public void setGoal()
	{
		goal=true;
	}
	public boolean tick()
	{
		if(enabled && !disabled)
		{
			if(goal)
				return true;
			for(int i =0; i < 4; i ++)
			{
				if(nodes[i]!=null)
					nodes[i].setEnabled(this);
			}

		}
		return false;
	}
	public void setEnabled(Node parent)
	{
		if(parent==this)
			this.parent = parent;
		enabled=true;
	}
	public Node getParent()
	{
		return parent;
	}
	public void setEnabled()
	{
		enabled=true;
	}
	
}
