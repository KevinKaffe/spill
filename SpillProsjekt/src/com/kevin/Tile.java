package com.kevin;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Tile implements Cloneable{
	private Image image;
	protected int x;
	protected int y;
	private int priority;
	public Tile(String imgSrc,int x,int y)
	{
		ImageIcon ii = new ImageIcon(imgSrc);
		image = ii.getImage();
		this.x=x;
		this.y=y;
	}
	public Tile(Tile tile)
	{
		this.image=tile.image;
		this.x=tile.x;
		this.y=tile.y;
		this.priority=tile.priority;
	}
	/*public Tile clone()
	{
		//Tile newTile = new Tile(image., x, y);
	}*/
	public int getX(){return x;};
	public int getY(){return y;};
	public void setX(int x){this.x=x;};
	public void setY(int y){this.y=y;};
	public Image getImage(){return image;};
	public void tick()
	{
		
	}
}
