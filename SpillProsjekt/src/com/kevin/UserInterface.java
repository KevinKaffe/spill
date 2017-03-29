package com.kevin;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class UserInterface {
	private List<Element> elements =  new ArrayList<>();
	private int x,y;
	private Image background;
	public UserInterface(int x, int y)
	{
		this.x=x;
		this.y=y;
		ImageIcon ii = new ImageIcon("interface_background.png");
		background = ii.getImage();
		elements.add(new Element(x+25, y+100));
		elements.get(0).addImage("faceOne.png", 0, 0);
		elements.add(new Element(x+25, y+225));
		elements.get(1).addImage("faceOne.png", 0, 0);
		elements.add(new Element(x+25, y+350));
		elements.get(2).addImage("faceOne.png", 0, 0);
		elements.add(new Element(x+25, y+475));
		elements.get(3).addImage("faceOne.png", 0, 0);
		/*ii=new ImageIcon("superpower_bar.png");
		icons.add(ii.getImage());*/
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public Image getImage()
	{
		return background;
	}
	public List<Element> getElements()
	{
		return elements;
	}
}
