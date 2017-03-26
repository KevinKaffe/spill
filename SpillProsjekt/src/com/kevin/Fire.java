package com.kevin;

public class Fire extends Tile {
	

	private int tileX;
	private int tileY;
	public Fire(String imgSrc, int x, int y, Player player, int tileX, int tileY)
	{
		super(imgSrc, x, y); 
		this.tileX = tileX;
		this.tileY = tileY;
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
