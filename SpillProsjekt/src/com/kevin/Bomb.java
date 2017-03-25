package com.kevin;

public class Bomb extends Tile {

	private int delay;
	private final int waitDestruction = 100;
	private Player player;
	
	public Bomb(String imgSrc, int x, int y, Player player) 
	{
		super(imgSrc, x, y);
		delay = waitDestruction;
		this.player = player;
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
			player.destroyBomb();
			//EKSPLOSJON
		}
	}
	

}
