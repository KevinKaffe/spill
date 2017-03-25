package com.kevin;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Enemy {
	private int dx, dy, x, y, speed, hp;
	private Image image;
	private Player player;
	public Enemy(Player player)
	{
		ImageIcon ii = new ImageIcon("craft.png");
		image = ii.getImage();
		speed =1;
		x=40;
		hp=100;
		y=40;
		this.player = player;
	}
	public void move()
	{
		x+= dx;
		y+= dy;
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
	public void tick()
	{
		if(x>player.getX()) dx=-speed;
		else dx=speed;
		if(y>player.getY()) dy=-speed;
		else dy=speed;
		move();
	}
}
