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
	private int x, y,hp,maxBombs,id,invincibility;
	private int lowerTileX; // spillers posisjon på "map"
	private int lowerTileY;
	private int upperTileX;
	private int upperTileY;
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
	private List<Fire> fires = new ArrayList<>();
	private Collection<Fire> fire_removal_queue = new ArrayList<>();
	public static final int adjustX = -7;   // måte justere for å at det skulle se pent ut 
	public static final int adjustY = -10;
	private final int invincibilityCoolDownForAASjekkeOmSpillerenEndaKanDoEllerOmHanFortsattIkkeSkalMisteLivNaarHanBlirTruffetAvEnFlamme_DenneCooldownSkalVearePaaRundt100MS=110;
	
	private Image image,trueImage;
	public Player(Board board, PlayerType type, int id)
	{
		this.id=id;
		maxBombs=3;
		this.type=type;
		fireLevel = 5;
		parent = board;
		ImageIcon ii;
		if (type == PlayerType.Player1)
		{
			ii = new ImageIcon("player.png");
		}
		else{
			ii = new ImageIcon("craft.png");
		}
		trueImage=ii.getImage();
		image = ii.getImage();
		x = 4*36;
		hp = 3;
		y = 4*36;
		velX = 0;
		velY = 0;
		speed = 4.0;
		lowerTileX = Math.round(x/36);
		upperTileX=Math.round((x+image.getWidth(parent)/2)/36);
		lowerTileY = Math.round(y/36) + 1;
		upperTileY = Math.round((y-image.getHeight(parent)/4)/36)+1;
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
		return lowerTileX;
	}
	public int getTileY()
	{
		return lowerTileY;
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
			else if (key == KeyEvent.VK_SHIFT)
			{
				if (bombs.size()<maxBombs)
				{
		
					bombs.add(new Bomb("bomb.png", Math.round((getX()+image.getWidth(parent)/2)/36)*36+ adjustX, Math.round(getY()/36)*36+36 + adjustY, this, 
							Math.round((getX()+image.getWidth(parent)/2)/36), Math.round(getY()/36)+1));
					//parent.setTile((bomb.getX()+7)/36, (bomb.getY()+10)/36,new Boulder("boulder.png", bomb.getX()+7, bomb.getY()+10));
					//System.out.println("X: " + bomb.getTileX() + "  Y: " + bomb.getTileY());
					//Må justere for å plassere midt på en tile, samtidig som vi må matche med parent.map
				}
			}
			
		}
		else if(type==PlayerType.Player2)
		{
			if(key == KeyEvent.VK_A)
			{
				velX = -speed;
			}
			else if(key==KeyEvent.VK_D)
			{
				velX = speed;
			}
			else if(key==KeyEvent.VK_W)
			{
				velY = -speed;
			}
			else if(key==KeyEvent.VK_S)
			{
				velY = speed;
			}
			else if (key == KeyEvent.VK_T)
			{
				if (bombs.size()<maxBombs)
				{
		
					bombs.add(new Bomb("bomb.png", Math.round(getX()/36)*36 + adjustX, Math.round(getY()/36)*36+36 + adjustY, this, 
							Math.round(getX()/36), Math.round(getY()/36)+1));
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
	public void decreaseHealth()
	{
		if(invincibility<=0)
		{
			this.hp--;
			Board.get_ui().getElements().get(0).getStrings().get(0).updateString("X  "+ Integer.toString(hp));
			invincibility=invincibilityCoolDownForAASjekkeOmSpillerenEndaKanDoEllerOmHanFortsattIkkeSkalMisteLivNaarHanBlirTruffetAvEnFlamme_DenneCooldownSkalVearePaaRundt100MS;
		}
	}
	public void tick()
	{
		for(Bomb b:bomb_removal_queue)
		{
			bombs.remove(b);
		}
		for(Fire set:fire_removal_queue)
		{
			fires.remove(set);
		}
		if(!fire_removal_queue.isEmpty())
			fire_removal_queue = new ArrayList<>();
		if(!bomb_removal_queue.isEmpty())
			bomb_removal_queue = new ArrayList<>();
		if(invincibility>0)
		{
			invincibility--;
			if(invincibility%6<=2)
				image=new ImageIcon("").getImage();
			else
				image=trueImage;
		}
		else
		{
			image=trueImage;
		}
		x += velX;
		y += velY;
		lowerTileX = Math.round(x/36);
		upperTileX=Math.round((x+image.getWidth(parent)/2)/36);
		lowerTileY = Math.round(y/36) + 1;
		upperTileY = Math.round((y-image.getHeight(parent)/4)/36)+1;
		//System.out.println(tileX + "   " + tileY); //printer hele tiden spilleren sin posisjon i "map"
		
		// spilleren kan ikke gå utafor kanten (y-retning er litt rar)
		if (lowerTileX == 0)
		{
			x -= velX;
		}
		if (upperTileY == 1)
		{
			y -= velY;
		}
		else if (upperTileX == 19)
		{
			x -= velX;
		}
		else if (lowerTileY == 18)
		{
			y -= velY;
		}
		
		//spiller kan ikke gå på "boulder" på brettet 
		else if (parent.isBoulder(lowerTileX, lowerTileY) || parent.isBoulder(upperTileX, lowerTileY)||parent.isBoulder(upperTileX, upperTileY) || parent.isBoulder(lowerTileX, upperTileY))
		{
			this.decreaseHealth();
			x -= velX;
			y -= velY;
		}
		else if (parent.isBox(lowerTileX, lowerTileY) || parent.isBox(upperTileX, lowerTileY)||parent.isBox(upperTileX, upperTileY) || parent.isBox(lowerTileX, upperTileY))
		{
			x -= velX;
			y -= velY;
		}
		
	}
	
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if (type == PlayerType.Player1)
		{
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
		else if (type == PlayerType.Player2)
		{
			if(key == KeyEvent.VK_A)
			{
				velX = 0;
			}
			else if(key==KeyEvent.VK_D)
			{
				velX = 0;
			}
			else if(key==KeyEvent.VK_W)
			{
				velY = 0;
			}
			else if(key==KeyEvent.VK_S)
			{
				velY = 0;
			}
		}
		
	}
	
	//lager flammer i hver retning
	public void explotion(int x, int y)
	{
		// både bomber og flammer har en posisjon på "map"  og en faktisk posisjon på lærettet 
		Fire f = new Fire ("fire.png", x-2,y, this, (x+7)/36, (y+10)/36); 
		fires.add(f);
	}
	
	public void destroyFire(Fire set)
	{
		fire_removal_queue.add(set);
	}
	public List<Fire> getFires()
	{
		return fires;
	}
	
}
