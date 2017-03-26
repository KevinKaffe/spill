package com.kevin;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ImageIcon;

public class Player {
	private int fireLevel;
	private boolean[] keysDown ={false,false,false,false};//For � ikke f� delay n�r man f�rst g�r en vei s� snur
	private int x, y,hp,maxBombs;
	private int tileX; // spillers posisjon på "map"
	private int tileY;
	private double velX, velY;
	private double speed;
	private PlayerType type;
	private Board parent;
	private List<Bomb> bombs = new ArrayList<>();
	private Collection<Bomb> bomb_removal_queue = new ArrayList<>();
	/*private Fire firePosX;
	private Fire fireNegX;
	private Fire firePosY;
	private Fire fireNegY;*/
	private List<FireSet> fires = new ArrayList<>();
	private Collection<FireSet> fire_removal_queue = new ArrayList<>();
	public static final int adjustX = -7;   // måte justere for å at det skulle se pent ut 
	public static final int adjustY = -10;
	
	private Image image;
	public Player(Board board, PlayerType type)
	{
		maxBombs=3;
		this.type=type;
		fireLevel = 5;
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

	public int getFireLevel()
	{
		return fireLevel;
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
		if(type==PlayerType.Player1)
		{
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
				if (bombs.size()<maxBombs)
				{
					bombs.add(new Bomb("bomb.png", Math.round(getX()/36)*36+36 + adjustX, Math.round(getY()/36)*36+36 + adjustY, this, 
							Math.round(getX()/36)+1, Math.round(getY()/36)+1));
					//parent.setTile((bomb.getX()+7)/36, (bomb.getY()+10)/36,new Boulder("boulder.png", bomb.getX()+7, bomb.getY()+10));
					//System.out.println("X: " + bomb.getTileX() + "  Y: " + bomb.getTileY());
					//Må justere for å plassere midt på en tile, samtidig som vi må matche med parent.map
				}
			}
		}
	}

	public void destroyBomb(Bomb bomb)
	{
		bomb_removal_queue.add(bomb);
		//bombs.remove(bomb);
	}
	public List<Bomb> getBombs()
	{
		return bombs;
	}
	public void decreaseHealth(int hp)
	{
		this.hp-=hp;
	}
	public void tick()
	{
		for(Bomb b:bomb_removal_queue)
		{
			bombs.remove(b);
		}
		for(FireSet set:fire_removal_queue)
		{
			fires.remove(set);
		}
		fire_removal_queue = new ArrayList<>();
		bomb_removal_queue = new ArrayList<>();
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
		Fire f1 = new Fire ("fire.png", x-2,y, this, (x+7)/36, (y+10)/36); 
		Fire f2 = new Fire ("fire.png", x-2,y, this, (x+7)/36, (y+10)/36);
		Fire f3 = new Fire ("fire.png", x-2,y, this, (x+7)/36, (y+10)/36);
		Fire f4 = new Fire ("fire.png", x-2,y, this, (x+7)/36, (y+10)/36);
		fires.add(new FireSet(f1,f2,f3,f4, this));
	}
	
	public void destroyFire(FireSet set)
	{
		fire_removal_queue.add(set);
	}
	public List<FireSet> getFires()
	{
		return fires;
	}
	
}
