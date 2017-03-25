package com.kevin;

import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Player {
	private final int DELAY=10;
	private int dx, dy, x, y, speed, hp,moveDelay, posX, posY;
	private Board parent;
	
	private Image image;
	public Player(Board board)
	{
		parent=board;
		ImageIcon ii = new ImageIcon("player.png");
		image = ii.getImage();
		speed =1;
		x=0;
		hp=100;
		y=10;
		posX=0;
		posY=1;
		moveDelay=0;
	}
	public void move()
	{
		if(moveDelay<=0)
		{
			if(parent.isBoulder(posX+dx, posY+dy))
			{
				return;
			}
			x+= 36*dx;
			posX+=dx;
			y+= 36*dy;
			posY+=dy;
			moveDelay=DELAY;
		}
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
			dx=-speed;
		}
		else if(key==KeyEvent.VK_RIGHT)
		{
			dx=speed;
		}
		else if(key==KeyEvent.VK_UP)
		{
			dy=-speed;
		}
		else if(key==KeyEvent.VK_DOWN)
		{
			dy=speed;
		}
	}
	public void decreaseHealth(int hp)
	{
		this.hp-=hp;
	}
	public void tick()
	{
		move();
		moveDelay--;
	}
	
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_LEFT)
		{
			dx=0;
		}
		else if(key==KeyEvent.VK_RIGHT)
		{
			dx=0;
		}
		else if(key==KeyEvent.VK_UP)
		{
			dy=-0;
		}
		else if(key==KeyEvent.VK_DOWN)
		{
			dy=0;
		}
	}
}
