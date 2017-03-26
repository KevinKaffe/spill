package com.kevin;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends Tile {

	private int delay;
	private final int waitDestruction = 100;
	private Player player;
	private int tileX;
	private int tileY;
	private static List<Bomb> global_bomb_list = new ArrayList<>();
	public Bomb(String imgSrc, int x, int y, Player player, int tileX, int tileY) 
	{
		super(imgSrc, x, y);
		delay = waitDestruction;
		this.player = player;
		this.tileX = tileX;
		this.tileY = tileY;
		global_bomb_list.add(this);
	}
	
	
	@Override
	public void tick()
	{
		destroy();
		delay--;
	}
	
	private void destroy()
	{
		if (delay <= 0)
		{
			global_bomb_list.remove(this);
			delay = waitDestruction;
			player.explotion(getX(), getY());
			player.destroyBomb(this);
		}
	}
	public void explodeMe()
	{
		delay=0;
	}
	public int getTileX()
	{
		return tileX;
	}
	
	public int getTileY()
	{
		return tileY;
	}
	public static List<Bomb> getBombList()
	{
		return global_bomb_list;
	}
}
