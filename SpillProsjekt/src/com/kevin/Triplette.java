package com.kevin;

import java.awt.Image;

public class Triplette {
	private Image img;
	private String string;
	private int val1, val2;
	public Triplette(Image img, int val1, int val2)
	{
		this.img = img;
		this.val1=val1;
		this.val2=val2;
	}
	public Triplette(String string, int val1, int val2)
	{
		this.string = string;
		this.val1=val1;
		this.val2=val2;
	}
	public void updateString(String string)
	{
		this.string = string;
	}
	public String getString()
	{
		return string;
	}
	public Image getImg()
	{
		return img;
	}
	public int getVal1()
	{
		return val1;
	}
	public int getVal2()
	{
		return val2;
	}
}
