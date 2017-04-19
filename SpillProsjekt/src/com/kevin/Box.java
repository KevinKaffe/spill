package com.kevin;

import java.util.List;
import java.util.Random;

public class Box extends Tile {
	private List<Powerup> powerupTable;
	private List<Double> probabilityTable;
	public Box(String imgSrc, int x, int y) 
	{
		super(imgSrc, x, y);
	}
	public void ripMe()
	{
		System.out.println("AAA");
		powerupTable = Board.getPowerupTable();
		probabilityTable = Board.getProbableTable();
		double sum=0;
		System.out.println(probabilityTable.get(0));
		for(double d:probabilityTable)
		{
			System.out.println(d);
			sum+=d;
		}
		Random rand = new Random();
		double random = rand.nextDouble()*sum;
		for(int i =0; i < probabilityTable.size(); i++)
		{
			random-=probabilityTable.get(i);
			if(random<=0)
			{
				if(probabilityTable.get(i)==null)
				{
					return;
				}
				else
				{
					Powerup p = powerupTable.get(i).clone();
					p.setX(x);
					p.setY(y);
					Board.getStaticBoard().addPowerup(p);
					return;
				}
			}
		}
	}
}
