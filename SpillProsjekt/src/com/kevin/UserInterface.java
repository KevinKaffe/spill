package com.kevin;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class UserInterface {
	private List<Image> icons = new ArrayList<>();
	private List<Element> elements =  new ArrayList<>();
	private int x,y;
	private Image background;
	public UserInterface(int x, int y)
	{
		this.x=x;
		this.y=y;
		ImageIcon ii = new ImageIcon("interface_background.png");
		background = ii.getImage();
		elements.add(new Element(x+50, y+10));
		elements.get(0).addImage("faceOne.png", 0, 0);
		/*ii=new ImageIcon("superpower_bar.png");
		icons.add(ii.getImage());*/
	}
	public Image getImage()
	{
		return background;
	}
	public List<Image> getImageIcons()
	{
		return icons;
	}
	public List<Element> getElements()
	{
		return elements;
	}
}
