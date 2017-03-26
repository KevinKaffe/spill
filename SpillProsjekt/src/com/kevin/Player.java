package com.kevin;

import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Player {
	private int x, y,hp;
	private int tileX; // spillers posisjon på "map"
	private int tileY;
	private double velX, velY;
	private double speed;
	private Board parent;
	private Bomb bomb;
	private Fire firePosX;
	private Fire fireNegX;
	private Fire firePosY;
	private Fire fireNegY;
	public static final int adjustX = -7;   // måte justere for å at det skulle se pent ut 
	public static final int adjustY = -10;
	
	private Image image;
	public Player(Board board)
	{
		bomb = null;
		parent = board;
		ImageIcon ii = new ImageIcon("player.png");
		image = ii.getImage();
		x = 2*36;
		hp = 100;
		y = 2*36;
		velX = 0;
		velY = 0;
		speed = 2.0;
		tileX = Math.round(x/36);
		tileY = Math.round(y/36) + 1;
	}

	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public int getTileX()
	{
		return tileX;
	}
	public int getTileY()
	{
		return tileY;
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
			velX = -speed;
		}
		else if(key==KeyEvent.VK_RIGHT)
		{
			velX = speed;
		}
		else if(key==KeyEvent.VK_UP)
		{
			velY = -speed;
		}
		else if(key==KeyEvent.VK_DOWN)
		{
			velY = speed;
		}
		else if (key == KeyEvent.VK_Z)
		{
			if (bomb == null)
			{
				bomb = new Bomb("bomb.png", Math.round(getX()/36)*36+36 + adjustX, Math.round(getY()/36)*36+36 + adjustY, this, 
						Math.round(getX()/36)+1, Math.round(getY()/36)+1);
				//parent.setTile((bomb.getX()+7)/36, (bomb.getY()+10)/36,new Boulder("boulder.png", bomb.getX()+7, bomb.getY()+10));
				System.out.println("X: " + bomb.getTileX() + "  Y: " + bomb.getTileY());
				//Må justere for å plassere midt på en tile, samtidig som vi må matche med parent.map
			}
		}
	}

	public void destroyBomb()
	{
		bomb = null;
	}
	public Tile getBomb()
	{
		return bomb;
	}
	public void decreaseHealth(int hp)
	{
		this.hp-=hp;
	}
	public void tick()
	{
		x += velX;
		y += velY;
		tileX = Math.round(x/36);
		tileY = Math.round(y/36) + 1; 
		System.out.println(tileX + "   " + tileY); //printer hele tiden spilleren sin posisjon i "map"
		
		// spilleren kan ikke gå utafor kanten (y-retning er litt rar)
		if (tileX == 0)
		{
			x -= velX;
		}
		if (tileY == 1)
		{
			y -= velY;
		}
		else if (tileX == 19)
		{
			x -= velX;
		}
		else if (tileY == 18)
		{
			y -= velY;
		}
		
		//spiller kan ikke gå på "boulder" på brettet 
		else if (parent.isBoulder(tileX, tileY) || parent.isBoulder(tileX+1, tileY))
		{
			x -= velX;
			y -= velY;
		}
		
	}
	
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_LEFT)
		{
			velX = 0;
		}
		else if(key==KeyEvent.VK_RIGHT)
		{
			velX = 0;
		}
		else if(key==KeyEvent.VK_UP)
		{
			velY = 0;
		}
		else if(key==KeyEvent.VK_DOWN)
		{
			velY = 0;
		}
	}
	
	//lager flammer i hver retning
	public void explotion(int x, int y)
	{
		// både bomber og flammer har en posisjon på "map"  og en faktisk posisjon på lærettet 
		firePosX = new Fire ("fire.png", x-2,y, this, (x+7)/36, (y+10)/36); 
		fireNegX = new Fire ("fire.png", x-2,y, this, (x+7)/36, (y+10)/36);
		firePosY = new Fire ("fire.png", x-2,y, this, (x+7)/36, (y+10)/36);
		fireNegY = new Fire ("fire.png", x-2,y, this, (x+7)/36, (y+10)/36);
	}
	
	public void destroyFire()
	{
		firePosX = null;
		fireNegX = null;
		firePosY = null;
		fireNegY = null;
	}
	
	public Fire getFirePosX()
	{
		return firePosX;
	}
	public Fire getFireNegX()
	{
		return fireNegX;
	}
	public Fire getFirePosY()
	{
		return firePosY;
	}
	public Fire getFireNegY()
	{
		return fireNegY;
	}
	
}
