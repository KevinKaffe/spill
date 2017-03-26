package com.kevin;

import java.awt.Image;

public class Triplette {
	private Image img;
	private int val1, val2;
	public Triplette(Image img, int val1, int val2)
	{
		this.img = img;
		this.val1=val1;
		this.val2=val2;
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
