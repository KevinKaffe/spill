package com.kevin;

import java.awt.Image;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Element {
	List<Triplette> icons = new ArrayList<>();
	List<Triplette> strings = new ArrayList<>();
	int x,y;
	public Element(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	public void addString(String str, int x, int y)
	{
		strings.add(new Triplette(str, x+this.x, y+this.y));
	}
	public void addImage(String imgSrc, int x, int y)
	{
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(imgSrc);
		try{
			ImageIcon ii = new ImageIcon(ImageIO.read(is));
			icons.add(new Triplette(ii.getImage(),x+this.x,y+this.y));
		}
		catch(Exception e)
		{
			
		}

	}

	public List<Triplette> getIcons()
	{
		return icons;
	}
	public List<Triplette> getStrings()
	{
		return strings;
	}
}
