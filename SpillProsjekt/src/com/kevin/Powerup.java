package com.kevin;

import java.awt.Image;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Powerup {
	private String[] sprites;
	private Effect effect;
	private int x,y, ticker, tacker;
	private Image img;
	public Powerup(String[] sprites, Effect effect)
	{
		this.sprites=sprites;
		this.effect=effect;
		ticker=0;
		changeImg(sprites[0]);
	}
	public void tick()
	{

		if(tacker++%4==0)
		{
			if(ticker%12<6)
			{
				changeImg(sprites[ticker%6]);
			}
			else
			{
				changeImg(sprites[5-ticker%6]);
			}
			ticker++;
		}

	}
	public void changeImg(String imgSrc)
	{
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(imgSrc);
		try{
			ImageIcon ii = new ImageIcon(ImageIO.read(is));
			img=ii.getImage();
		}
		catch(Exception e)
		{
			
		}
	}
	public Effect removeMe()
	{
		Board.getStaticBoard().removePowerup(this);
		return effect;
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
		this.x=x;
	}
	public void setY(int y)
	{
		this.y=y;
	}
	
	public Powerup clone()
	{
		return new Powerup(sprites, effect);
	}
}
