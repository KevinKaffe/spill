package com.kevin;

import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Player {
	private int x, y,hp;
	private double velX, velY;
	private double speed;
	private Board parent;
	private Tile bomb;
	
	private Image image;
	public Player(Board board)
	{
		bomb = null;
		parent = board;
		ImageIcon ii = new ImageIcon("player.png");
		image = ii.getImage();
		x = 1*36;
		hp = 100;
		y = 1*36;
		velX = 0;
		velY = 0;
		speed = 2.0;
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
		return image;
	}
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_LEFT)
		{
			velX = -speed;
		}
		else if(key==KeyEvent.VK_RIGHT)
		{
			velX = speed;
		}
		else if(key==KeyEvent.VK_UP)
		{
			velY = -speed;
		}
		else if(key==KeyEvent.VK_DOWN)
		{
			velY = speed;
		}
		else if (key == KeyEvent.VK_Z)
		{
			if (bomb == null)
			{
				bomb = new Bomb("bomb.png", getX(), getY()+20, this);
			}
		}
	}

	public void destroyBomb()
	{
		bomb = null;
	}
	public Tile getBomb()
	{
		return bomb;
	}
	public void decreaseHealth(int hp)
	{
		this.hp-=hp;
	}
	public void tick()
	{
		x += velX;
		y += velY;
		
		if (parent.isBoulder(x/36, y/36) || parent.isBoulder(x/36 + 1, y/36 + 1))
		{
			x -= velX;
			y -= velY;
		}
		
	}
	
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_LEFT)
		{
			velX = 0;
		}
		else if(key==KeyEvent.VK_RIGHT)
		{
			velX = 0;
		}
		else if(key==KeyEvent.VK_UP)
		{
			velY = 0;
		}
		else if(key==KeyEvent.VK_DOWN)
		{
			velY = 0;
		}
	}
}
