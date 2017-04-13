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
	private boolean dead;
	private int lowerTileX; // spillers posisjon på "map"
	private int lowerTileY;
	private int upperTileX;
	private int upperTileY;
	private double velX, velY;
	private double speed;
	private boolean isDead = false;
	private PlayerType type;
	private Board parent;
	private List<Bomb> bombs = new ArrayList<>();
	private Collection<Bomb> bomb_removal_queue = new ArrayList<>();
	private List<Fire> fires = new ArrayList<>();
	private Collection<Fire> fire_removal_queue = new ArrayList<>();
	public static final int adjustX = -7;   // måte justere for å at det skulle se pent ut 
	public static final int adjustY = -10;
	private String[] sprites;
	private int localTicker;
	private int walkState; //For � vite hvilken side man skal se han fra
	private int spriteState; //For animasjoner
	private final int invincibilityCoolDownForAASjekkeOmSpillerenEndaKanDoEllerOmHanFortsattIkkeSkalMisteLivNaarHanBlirTruffetAvEnFlamme_DenneCooldownSkalVearePaaRundt100MS=110;
	private final int spriteWidth=18; //Halve vidden, vel � merke
	private Image image,trueImage;
	private double superPower;
	private Character character;
	public Player(Board board, PlayerType type, int id)
	{
		superPower=600;
		dead =false;
		this.id=id;
		maxBombs=3;
		this.type=type;
		fireLevel = 5;
		parent = board;
		ImageIcon ii;
		if (type == PlayerType.Player1)
		{
			ii = new ImageIcon("player.png");
			x = 2*36;
			y = 2*36;
		}
		else{
			ii = new ImageIcon("player.png");
			x = 16*36;
			y = 16*36;
		}
		trueImage=ii.getImage();
		image = ii.getImage();

		
		hp = 200*3;
		spriteState=0;
		walkState=0;
		localTicker=0;
		x = 4*36;
		hp = 3;
		y = 4*36;
		velX = 0;
		velY = 0;
		speed = 4.0;
		lowerTileX = Math.round(x/36);
		upperTileX=Math.round((x+spriteWidth)/36);
		lowerTileY = Math.round(y/36) + 1;
		upperTileY = Math.round((y-image.getHeight(parent)/4)/36)+1;
	}

	public void setImage(ImageIcon ii)
	{
		image = ii.getImage();
		trueImage = ii.getImage();
	}
	public boolean getIsDead()
	{
		return isDead;
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
	public int getUpperTileX()
	{
		return upperTileX;
	}
	public int getUpperTileY()
	{
		return upperTileY;
	}
	public Image getImage()
	{
		return image;
	}
	public void keyPressed(KeyEvent e)
	{
		if (dead)
		{
			return;
		}
		int key = e.getKeyCode();
		
		if(type==PlayerType.Player1)
		{
			if(key == KeyEvent.VK_LEFT)
			{
				walkState=1;
				keysDown[1]=true;
				velX = -speed;
			}
			else if(key==KeyEvent.VK_RIGHT)
			{
				walkState=2;
				keysDown[2]=true;
				velX = speed;
			}
			else if(key==KeyEvent.VK_UP)
			{
				walkState=3;
				keysDown[3]=true;
				velY = -speed;
			}
			else if(key==KeyEvent.VK_DOWN)
			{
				walkState=0;
				keysDown[0]=true;
				velY = speed;
			}
			else if (key == KeyEvent.VK_Z)
			{
				if (bombs.size()<maxBombs)
				{
		
					//bombs.add(new Bomb("bomb.png", Math.round((getX()+spriteWidth)/36)*36+ adjustX, Math.round(getY()/36)*36+36 + adjustY, this, 
							//getTileX(), getTileY()));
					bombs.add(new Bomb("bomb.png",Math.round((getX()+spriteWidth)/36)*36,Math.round((getY()+30)/36)*36, this, 
							Math.round((getX()+spriteWidth)/36), Math.round((getY()+30)/36)));
					//parent.setTile((bomb.getX()+7)/36, (bomb.getY()+10)/36,new Boulder("boulder.png", bomb.getX()+7, bomb.getY()+10));
					//System.out.println("X: " + bomb.getTileX() + "  Y: " + bomb.getTileY());
					//Må justere for å plassere midt på en tile, samtidig som vi må matche med parent.map
				}
			}
			else if(key==KeyEvent.VK_ENTER)
			{
				if(superPower>=100)
				{
					superPower();
				}
			}
			
		}
		else if(type==PlayerType.Player2)
		{
			if(key == KeyEvent.VK_A)
			{
				walkState=1;
				keysDown[1]=true;
				velX = -speed;
			}
			else if(key==KeyEvent.VK_D)
			{
				walkState=2;
				keysDown[2]=true;
				velX = speed;
			}
			else if(key==KeyEvent.VK_W)
			{
				walkState=3;
				keysDown[3]=true;
				velY = -speed;
			}
			else if(key==KeyEvent.VK_S)
			{
				walkState=0;
				keysDown[0]=true;
				velY = speed;
			}
			else if (key == KeyEvent.VK_T)
			{
				if (bombs.size()<maxBombs && !parent.isTrumpWall(Math.round(getX()/36),Math.round(getY()/36)+1))
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
	private void superPower()
	{
		switch(character)
		{
		case Trump:
			if(parent.isEmpty(lowerTileX, lowerTileY))
			{
				parent.setTile(lowerTileX, lowerTileY, new TrumpWall("wall.png", lowerTileX*36, lowerTileY*36));
				superPower-=100;
				System.out.println(superPower);
				Board.get_ui().getElements().get(id).getIcons().get(2).updateWidth(superPower/6);
			}
		}
	}
	public void setSprites(String[] sprites, Character character)
	{
		this.character=character;
		this.sprites = sprites;
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
			Board.get_ui().getElements().get(id).getStrings().get(0).updateString("X  "+ Integer.toString(hp));
			invincibility=invincibilityCoolDownForAASjekkeOmSpillerenEndaKanDoEllerOmHanFortsattIkkeSkalMisteLivNaarHanBlirTruffetAvEnFlamme_DenneCooldownSkalVearePaaRundt100MS;
		}
	}
	public void tick()
	{
		localTicker++;
		if(dead)
		{
			for(Bomb b:bomb_removal_queue)
			{
				bombs.remove(b);
			}//
			for(Fire set:fire_removal_queue)
			{
				fires.remove(set);
			}
			return;
		}
		if(hp<=0)
		{
			parent.setTile(lowerTileX, lowerTileY, new Boulder ("rip.png", lowerTileX*36, lowerTileY*36));
			dead=true;
		
		}
		for(Bomb b:bomb_removal_queue)
		{
			bombs.remove(b);
		}//
		for(Fire set:fire_removal_queue)
		{
			fires.remove(set);
		}
		if(!fire_removal_queue.isEmpty())
			fire_removal_queue = new ArrayList<>();
		if(!bomb_removal_queue.isEmpty())
			bomb_removal_queue = new ArrayList<>();
		
		if(keysDown[0] || keysDown[1]||keysDown[2]||keysDown[3])
		{
			if(localTicker%6 ==0)
				spriteState++;
			if(keysDown[0])
				walkState=0;
			else if(keysDown[1])
				walkState=2;
			else if(keysDown[2])
				walkState=1;
			else if(keysDown[3])
				walkState=3;
		}
		else
		{
			spriteState=0;
		}
		if(localTicker%500==0 &&superPower+4<600)
		{
			superPower+=100;
			Board.get_ui().getElements().get(id).getIcons().get(2).updateWidth(superPower/6+0.1);
		}
		this.setImage(new ImageIcon(sprites[spriteState%4 + 4*walkState]));
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
		lowerTileX = Math.round(getX()/36);
		upperTileX=Math.round((getX()+spriteWidth)/36);
		lowerTileY = Math.round(getY()/36) + 1;
		upperTileY = Math.round((getY()-trueImage.getHeight(parent)/4)/36)+1;
		

		
		if (getY() <= 15)
		{
			y -= velY;
		}
		//spiller kan ikke gå på "boulder" på brettet 
		if (parent.isBoulder(lowerTileX, lowerTileY) || parent.isBoulder(upperTileX, lowerTileY)||parent.isBoulder(upperTileX, upperTileY) || parent.isBoulder(lowerTileX, upperTileY))
		{
			x -= velX;
			y -= velY;
		}
		else if (parent.isBox(lowerTileX, lowerTileY) || parent.isBox(upperTileX, lowerTileY)||parent.isBox(upperTileX, upperTileY) || parent.isBox(lowerTileX, upperTileY))
		{
			x -= velX;
			y -= velY;
		}
		else if (character!=Character.Trump &&(parent.isTrumpWall(lowerTileX, lowerTileY) || parent.isTrumpWall(upperTileX, lowerTileY)||parent.isTrumpWall(upperTileX, upperTileY) || parent.isTrumpWall(lowerTileX, upperTileY)))
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
				velX=speed;
				if(!keysDown[2])
					velX = 0;
				keysDown[1]=false;
			}
			else if(key==KeyEvent.VK_RIGHT)
			{
				velX=-speed;
				if(!keysDown[1])
					velX = 0;
				keysDown[2]=false;
			}
			else if(key==KeyEvent.VK_UP)
			{
				velY=speed;
				if(!keysDown[0])
					velY = 0;
				keysDown[3]=false;
			}
			else if(key==KeyEvent.VK_DOWN)
			{
				velY=-speed;
				if(!keysDown[3])
					velY = 0;
				keysDown[0]=false;
			}
		}
		else if (type == PlayerType.Player2)
		{
			if(key == KeyEvent.VK_A)
			{
				if(!keysDown[2])
					velX = 0;
				keysDown[1]=false;
			}
			else if(key==KeyEvent.VK_D)
			{
				if(!keysDown[1])
					velX = 0;
				keysDown[2]=false;
			}
			else if(key==KeyEvent.VK_W)
			{
				if(!keysDown[0])
					velY = 0;
				keysDown[3]=false;
			}
			else if(key==KeyEvent.VK_S)
			{
				if(!keysDown[3])
					velY = 0;
				keysDown[0]=false;
			}
		}
		
	}
	
	//lager flammer i hver retning
	public void explotion(int x, int y, int tileX, int tileY)
	{
		// både bomber og flammer har en posisjon på "map"  og en faktisk posisjon på lærettet 
		Fire f = new Fire ("fire.png", x-2,y, this, tileX, tileY); 
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
