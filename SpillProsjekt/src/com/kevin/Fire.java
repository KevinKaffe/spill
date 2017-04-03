package com.kevin;

import java.util.ArrayList;
import java.util.List;

public class Fire extends Tile {
	
	private Player player;
	private int delay;
	private final int waitDestruction = 40;
	private int tileX;
	private int tileY;
	private int[] boundary = {100,100,100,100};
	private static List<Fire> global_fire_list = new ArrayList<>();

	public Fire(String imgSrc, int x, int y, Player player, int tileX, int tileY)
	{
		super(imgSrc, x, y); 
		this.player = player;
		delay = waitDestruction;
		this.tileX = tileX;
		this.tileY = tileY;
		global_fire_list.add(this);
	}

	@Override
	public void tick()
	{
		destroy();
		delay--;
	}
	
	private void destroy()
	{
		if (player.getIsDead())
		{
			global_fire_list.remove(this);
			player.destroyFire(this);
		}
		if (delay <= 0)
		{
			global_fire_list.remove(this);
			delay = waitDestruction;
			player.destroyFire(this);
		}
	}
	public int getTileX()
	{
		return tileX;
	}
	
	public int getTileY()
	{
		return tileY;
	}
	
	public void setBoundry(int n, int index)
	{
		boundary[index] = n;
	}
	public int getBoundry(int index)
	{
		return boundary[index];
	}
	
	public static List<Fire> getFireList()
	{
		return global_fire_list;
	}
	
}