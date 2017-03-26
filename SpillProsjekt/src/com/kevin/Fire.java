package com.kevin;

public class Fire extends Tile {
	
	private Player player;
	private int delay;
	private final int waitDestruction = 100;
	private int tileX;
	private int tileY;

	public Fire(String imgSrc, int x, int y, Player player, int tileX, int tileY)
	{
		super(imgSrc, x, y); 
		this.player = player;
		delay = waitDestruction;
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
			player.destroyFire();
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
