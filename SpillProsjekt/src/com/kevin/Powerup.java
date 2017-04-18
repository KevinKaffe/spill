package com.kevin;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Powerup {
	private String[] sprites;
	private Effect effect;
	private int x,y, ticker;
	private Image img;
	public Powerup(String[] sprites, Effect effect)
	{
		this.sprites=sprites;
		this.effect=effect;
		ticker=0;
	}
	public void tick()
	{

		if(ticker%12<6)
		{
			img=(new ImageIcon(sprites[ticker%6])).getImage();
		}
		else
		{
			img=(new ImageIcon(sprites[5-ticker%6])).getImage();
		}
		ticker++;
	}
	public Image getImage()
	{
		return img;
	}
	public Effect getEffect()
	{
		return effect;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public void setX(int x)
	{
		this.x=x*36;
	}
	public void setY(int y)
	{
		this.y=y*36;
	}
	
	public Powerup clone()
	{
		return new Powerup(sprites, effect);
	}
}
