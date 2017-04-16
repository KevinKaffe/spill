package com.kevin;

public class Pair {
	private int val1, val2, calls;//Cheat?
	public Pair(int val1, int val2)
	{
		this.val1=val1;
		this.val2=val2;
		calls=0;
	}
	public int getFirst()
	{
		return val1;
	}
	public int getSecond()
	{
		return val2;
	}
	public void setFirst(int val)
	{
		val1=val;
	}
	public void setSecond(int val)
	{
		val2=val;
	}
	public int getCalls()
	{
		return calls;
	}
	public Pair addCall()
	{
		calls++;
		return this;
	}
	public String toString()
	{
		return val1+""+val2;
	}
}
