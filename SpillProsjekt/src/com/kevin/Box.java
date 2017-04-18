package com.kevin;

import java.util.List;

public class Box extends Tile {
	private List<Powerup> powerupTable;
	private List<Double> probabilityTable;
	public Box(String imgSrc, int x, int y) 
	{
		super(imgSrc, x, y);
	}
	public void ripMe()
	{
		
	}
}
