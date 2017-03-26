package com.kevin;

public class Bomb extends Tile {

	private int delay;
	private final int waitDestruction = 100;
	private Player player;
	private int tileX;
	private int tileY;
	public Bomb(String imgSrc, int x, int y, Player player, int tileX, int tileY) 
	{
		super(imgSrc, x, y);
		delay = waitDestruction;
		this.player = player;
		this.tileX = tileX;
		this.tileY = tileY;
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
			delay = waitDestruction;
			player.explotion(getX(), getY());
			player.destroyBomb();
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

}