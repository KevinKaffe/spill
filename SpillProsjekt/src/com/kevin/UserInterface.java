package com.kevin;

import java.awt.Image;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class UserInterface {
	private List<Element> elements =  new ArrayList<>();
	private int x,y;
	private Image background;
	public UserInterface(int x, int y)
	{
		this.x=x;
		this.y=y;
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("interface_background.png");
		try{
			ImageIcon ii = new ImageIcon(ImageIO.read(is));
			background = ii.getImage();
		}
		catch(Exception e)
		{
			
		}
		elements.add(new Element(x+25, y+100));
		elements.get(0).addImage("faceOne.png", 0, 0);
		elements.get(0).addImage("heart.png", 10, 33);
		//elements.get(0).addImage("superpower_bar_fill.png", 5, 48);
		//elements.get(0).addImage("superpower_bar.png", 5, 48);
		elements.get(0).addString(": " + Integer.toString(3), 40, 51);
		/*elements.get(0).addImage("Icons/speed.png", 10, 24);
		elements.get(0).addString(": " + Integer.toString(3), 30, 37);*/
		elements.get(0).addString("Player 1", 10, 20);
		elements.add(new Element(x+25, y+225));
		elements.get(1).addImage("faceOne.png", 0, 0);
		elements.get(1).addImage("heart.png", 10, 33);
		//elements.get(0).addImage("superpower_bar_fill.png", 5, 48);
		//elements.get(0).addImage("superpower_bar.png", 5, 48);
		elements.get(1).addString(": " + Integer.toString(3), 40, 51);
		/*elements.get(0).addImage("Icons/speed.png", 10, 24);
		elements.get(0).addString(": " + Integer.toString(3), 30, 37);*/
		elements.get(1).addString("Player 2", 10, 20);
		elements.add(new Element(x+25, y+350));
		elements.get(2).addImage("faceOne.png", 0, 0);
		elements.get(2).addImage("heart.png", 10, 33);
		//elements.get(0).addImage("superpower_bar_fill.png", 5, 48);
		//elements.get(0).addImage("superpower_bar.png", 5, 48);
		elements.get(2).addString(": " + Integer.toString(3), 40, 51);
		/*elements.get(0).addImage("Icons/speed.png", 10, 24);
		elements.get(0).addString(": " + Integer.toString(3), 30, 37);*/
		elements.get(2).addString("Player 3", 10, 20);
		elements.add(new Element(x+25, y+475));
		elements.get(3).addImage("faceOne.png", 0, 0);
		elements.get(3).addImage("heart.png", 10, 33);
		//elements.get(0).addImage("superpower_bar_fill.png", 5, 48);
		//elements.get(0).addImage("superpower_bar.png", 5, 48);
		elements.get(3).addString(": " + Integer.toString(3), 40, 51);
		/*elements.get(0).addImage("Icons/speed.png", 10, 24);
		elements.get(0).addString(": " + Integer.toString(3), 30, 37);*/
		elements.get(3).addString("Player 4", 10, 20);
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
