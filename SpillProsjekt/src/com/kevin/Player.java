package com.kevin;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {
	

	private static final double MAXSPEED=4, MINSPEED=1;

	private static final int MAXBOMBS=6, MINBOMBS=1, MINFIRE=2, MAXFIRE=11;

	protected int fireLevel;
	protected boolean[] keysDown ={false,false,false,false};//For � ikke f� delay n�r man f�rst g�r en vei s� snur
	protected int x, y,hp,maxBombs,id,invincibility;
	private boolean dead;
	protected int lowerTileX; // spillers posisjon på "map"
	protected int lowerTileY;
	protected int upperTileX;
	protected int upperTileY;
	protected double prevX, prevY;
	protected double velX, velY;
	protected double speed;
	private boolean isDead = false;
	private PlayerType type;
	protected Board parent;
	protected List<Bomb> bombs = new ArrayList<>();
	private Collection<Bomb> bomb_removal_queue = new ArrayList<>();
	private List<Fire> fires = new ArrayList<>();
	private Collection<Fire> fire_removal_queue = new ArrayList<>();
	public static final int adjustX = -7;   // måte justere for å at det skulle se pent ut 
	public static final int adjustY = -10;
	private String[] sprites;
	private int localTicker;
	private int passageX, passageY;
	private int walkState; //For � vite hvilken side man skal se han fra
	private int spriteState; //For animasjoner
	private final int invincibilityCoolDownForAASjekkeOmSpillerenEndaKanDoEllerOmHanFortsattIkkeSkalMisteLivNaarHanBlirTruffetAvEnFlamme_DenneCooldownSkalVearePaaRundt100MS=110;
	protected final int spriteWidth=18; //Halve vidden, vel � merke
	protected Image image,trueImage;
	private double superPower;
	private Character character;
	public Player(Board board, PlayerType type, int id)
	{
		passageX=-1;
		passageY=-1;
		superPower=600;
		dead =false;
		this.id=id;
		maxBombs=3;
		this.type=type;
		fireLevel = 3;
		parent = board;
		ImageIcon ii;
		ii = new ImageIcon("player.png");
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("player.png");
		try{
			ii = new ImageIcon(ImageIO.read(is));
		}
		catch(Exception e)
		{
			
		}
		if (id == 1)
		{
			ii = new ImageIcon("player.png");
			x = 1*36;
			y = 1*36;
		}
		else if (id == 2){
			ii = new ImageIcon("player.png");
			x = 17*36;
			y = 17*36;
		}
		else if(id==3){
			ii = new ImageIcon("player.png");
			x = 1*36;
			y = 17*36;
		}
		else if(id==4)
		{
			ii = new ImageIcon("player.png");
			x=17*36;
			y=1*36;
		}
		trueImage=ii.getImage();
		image = ii.getImage();

		
		//hp = 200*3;
		spriteState=0;
		walkState=0;
		localTicker=0;
		//x = 4*36;
		hp = 3;
		//y = 4*36;
		velX = 0;
		velY = 0;
		speed = 2;
		lowerTileX = Math.round(x/36);
		upperTileX=Math.round((x+spriteWidth)/36);
		lowerTileY = Math.round(y/36) + 1;
		upperTileY = Math.round((y-image.getHeight(parent)/4)/36)+1;
	}
	
	public int getId()
	{
		return id;
	}

	public void setImage(String input)//ImageIcon ii)
	{
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(input);
		try{
			ImageIcon ii = new ImageIcon(ImageIO.read(is));
			image = ii.getImage();
			trueImage = ii.getImage();
		}
		catch(Exception e)
		{
			
		}

	}
	public boolean getIsDead()
	{
		return dead;
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
	public int getAdjY()
	{
		return y-10;
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
	public int getAvgTileX()
	{
		return Math.round((getX()+spriteWidth/2)/36);
		
	}
	public int getAvgTileY()
	{
		return Math.round((getY()-trueImage.getHeight(parent)/6)/36)+1;
	}
	public Image getImage()
	{
		return image;
	}
	

	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		if (dead)
		{
			return;
		}		
		
		if(type==PlayerType.Player1)
		{
			if(key == KeyEvent.VK_LEFT)
			{
				walkState=2;
				keysDown[1]=true;
				velX = -speed;
			}
			else if(key==KeyEvent.VK_RIGHT)
			{
				walkState=1;
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
			else if (key == KeyEvent.VK_SHIFT)
			{
				if (bombs.size()<maxBombs && !parent.isBomb(getAvgTileX(), getAvgTileY()) && !parent.isPlayer(getAvgTileX(), getAvgTileY(), this))
				{
		

					//bombs.add(new Bomb("bomb.png", Math.round((getX()+spriteWidth)/36)*36+ adjustX, Math.round(getY()/36)*36+36 + adjustY, this, 
							//getTileX(), getTileY()));
					/*bombs.add(new Bomb("bomb.png",Math.round((getX()+spriteWidth)/36)*36,Math.round((getY()+30)/36)*36, this, 
							Math.round((getX()+spriteWidth)/36), Math.round((getY()+30)/36)));
*/
					/*if (parent.isBoulder(getTileX(), getTileY()) || parent.isBox(getTileX(), getTileY()))
					{
						if (keysDown[1])
						{
							bombs.add(new Bomb("bomb.png", (getTileX()+1)*36+ adjustX, (getTileY())*36 + adjustY, this, 
									getTileX()+1, getTileY()));
						}
						else if (keysDown[0])
						{
							bombs.add(new Bomb("bomb.png", (getTileX())*36+ adjustX, (getTileY()-1)*36 + adjustY, this, 
									getTileX(), getTileY()-1));
						}
					}
					else
					{*/
					try {
						  AePlayWave aw = new AePlayWave( "blop.wav" );
					       aw.start();     
					}
					catch (Exception ez) {
					    
					}
					passageX=getAvgTileX();
					passageY=getAvgTileY();
						bombs.add(new Bomb("bomb.png", getAvgTileX()*36+ adjustX, getAvgTileY()*36 + adjustY, this, 
								getAvgTileX(), getAvgTileY()));
					//}
					
//>>>>>>> branch 'master' of https://github.com/KevinKaffe/spill
					//parent.setTile((bomb.getX()+7)/36, (bomb.getY()+10)/36,new Boulder("boulder.png", bomb.getX()+7, bomb.getY()+10));
					//System.out.println("X: " + bomb.getTileX() + "  Y: " + bomb.getTileY());
					//Må justere for å plassere midt på en tile, samtidig som vi må matche med parent.map
				}
			}
			/*else if(key==KeyEvent.VK_ENTER)
			{
				if(superPower>=100)
				{
					superPower();
				}
			}*/
			
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
				if (bombs.size()<maxBombs && !parent.isBomb(getAvgTileX(), getAvgTileY()) && !parent.isPlayer(getAvgTileX(), getAvgTileY(), this))
				{
		

					//bombs.add(new Bomb("bomb.png", Math.round((getX()+spriteWidth)/36)*36+ adjustX, Math.round(getY()/36)*36+36 + adjustY, this, 
							//getTileX(), getTileY()));
					/*bombs.add(new Bomb("bomb.png",Math.round((getX()+spriteWidth)/36)*36,Math.round((getY()+30)/36)*36, this, 
							Math.round((getX()+spriteWidth)/36), Math.round((getY()+30)/36)));
*/
					/*if (parent.isBoulder(getTileX(), getTileY()) || parent.isBox(getTileX(), getTileY()))
					{
						if (keysDown[1])
						{
							bombs.add(new Bomb("bomb.png", (getTileX()+1)*36+ adjustX, (getTileY())*36 + adjustY, this, 
									getTileX()+1, getTileY()));
						}
						else if (keysDown[0])
						{
							bombs.add(new Bomb("bomb.png", (getTileX())*36+ adjustX, (getTileY()-1)*36 + adjustY, this, 
									getTileX(), getTileY()-1));
						}
					}
					else
					{*/
					try {
						  AePlayWave aw = new AePlayWave( "blop.wav" );
					       aw.start();     
					}
					catch (Exception ez) {
					    
					}
					passageX=getAvgTileX();
					passageY=getAvgTileY();
						bombs.add(new Bomb("bomb.png", getAvgTileX()*36+ adjustX, getAvgTileY()*36 + adjustY, this, 
								getAvgTileX(), getAvgTileY()));
					//}
					
//>>>>>>> branch 'master' of https://github.com/KevinKaffe/spill
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
			
				Board.get_ui().getElements().get(id).getIcons().get(2).updateWidth(superPower/6);
			}
			break;
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
			
			if (true)//!(this instanceof NPC))
			{
				Board.get_ui().getElements().get(id-1).getStrings().get(0).updateString("X  "+ Integer.toString(hp));
			}
			
			invincibility=invincibilityCoolDownForAASjekkeOmSpillerenEndaKanDoEllerOmHanFortsattIkkeSkalMisteLivNaarHanBlirTruffetAvEnFlamme_DenneCooldownSkalVearePaaRundt100MS;
		}
	}
	public void addEffect(Effect effect)
	{
		switch(effect)
		{
		case SpeedUp:
			speed+=1;
			if(speed>MAXSPEED)
				speed=MAXSPEED;
			break;
		case SpeedDown:
			speed-=1;
			if(speed<MINSPEED)
				speed=MINSPEED;
			break;
		case SpeedMax:
			speed=MAXSPEED;
			break;
		case SpeedMin:
			speed=MINSPEED;
			break;
		case BombUp:
			maxBombs++;
			if(maxBombs>MAXBOMBS)
			{
				maxBombs=MAXBOMBS;
			}
			break;
		case BombDown:
			maxBombs--;
			if(maxBombs<MINBOMBS)
			{
				maxBombs=MINBOMBS;
			}
			break;
		case BombMax:
			maxBombs=MAXBOMBS;
			break;
		case BombMin:
			maxBombs=MINBOMBS;
			break;
		case FireUp:
			fireLevel++;
			if(fireLevel>MAXFIRE)
				fireLevel=MAXFIRE;
			break;
		case FireDown:
			fireLevel--;
			if(fireLevel<MINFIRE)
				fireLevel=MINFIRE;
			break;
		case FireMin:
			fireLevel=MINFIRE;
			break;
		case FireMax:
			fireLevel=MAXFIRE;
			break;
		}
	}
	public void tick()
	{
		localTicker++;
		if(dead)
		{
			if (!(this instanceof NPC) && !(parent.getMenu().isTwoPlayer()))
			{
				parent.setGameover();
			}
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
			parent.setTile(getAvgTileX(), getAvgTileY(), new Boulder ("rip.png", getAvgTileX()*36, getAvgTileY()*36));
			dead=true;
			if (!(this instanceof NPC) && !(parent.getMenu().isTwoPlayer()))
			{
				parent.setGameover();
			}
			isDead=true;
		
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
		
		if (true)//!(this instanceof NPC))
		{
		if(keysDown[0] || keysDown[1]||keysDown[2]||keysDown[3])
		{
			if(localTicker%(24/speed) ==0)
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
		this.setImage(sprites[spriteState%4 + 4*walkState]);
		}
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
		prevX=x;
		prevY=y;
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
		boolean flag =false;
		if(true)//!(this instanceof NPC))
		{
			//if (parent.isBoulder(lowerTileX, lowerTileY) || parent.isBoulder(upperTileX, lowerTileY)||parent.isBoulder(upperTileX, upperTileY) || parent.isBoulder(lowerTileX, upperTileY))
			if(parent.isBoulder(getAvgTileX(), getAvgTileY()))
			{
				flag=true;
				x -= velX;
				y -= velY;
			}
			//else if (parent.isBox(lowerTileX, lowerTileY) || parent.isBox(upperTileX, lowerTileY)||parent.isBox(upperTileX, upperTileY) || parent.isBox(lowerTileX, upperTileY))
			else if(parent.isBox(getAvgTileX(), getAvgTileY()))
			{
				flag=true;
				x -= velX;
				y -= velY;
			}
			else if (character!=Character.Trump &&(parent.isTrumpWall(lowerTileX, lowerTileY) || parent.isTrumpWall(upperTileX, lowerTileY)||parent.isTrumpWall(upperTileX, upperTileY) || parent.isTrumpWall(lowerTileX, upperTileY)))
			{
				flag=true;
				x -= velX;
				y -= velY;
			}
		}

		if(!(passageX==getAvgTileX() && passageY==getAvgTileY()) && !flag && !(this instanceof NPC))
		{
			passageX=-1;
			passageY=-1;
			//if(parent.isBomb(lowerTileX, lowerTileY) || parent.isBomb(upperTileX, lowerTileY)||parent.isBomb(upperTileX, upperTileY) || parent.isBomb(lowerTileX, upperTileY))
			if(parent.isBomb(getAvgTileX(), getAvgTileY()))
			{

					x-=velX;
					y-=velY;
					while(parent.isBomb(getAvgTileX(), getAvgTileY()))
					{
						if(Math.abs(velX)+Math.abs(velY)==0)
						{
							if (parent.isBomb(lowerTileX, lowerTileY))
							{
								x++;
								//y++;
							}
							else if(parent.isBomb(upperTileX, lowerTileY))
							{
								x--;
								//y++;
							}
							else if(parent.isBomb(lowerTileX, upperTileY))
							{
								x++;
								//y++;
							}
							else
							{
								x--;
								//y--;
							}
						}
						x-=velX;
						y-=velY;
					}

			}
		}
		List<Powerup> powerups = Board.getStaticBoard().getPowerupBoard();
		for(int i =0; i < powerups.size(); i++)
		{
			if(powerups.get(i).getX()/36 == getAvgTileX() && powerups.get(i).getY()/36 == getAvgTileY())
			{
				addEffect(powerups.get(i).removeMe());
			}
		}

	}
	public boolean tileIsSafe(int tileX, int tileY, int lookRange)
	{
		
		for(Bomb bomb: Bomb.getBombList())
		{
			if(bomb.getTileY() ==tileY  && Math.abs(bomb.getTileX()-tileX)<lookRange)
			{
				boolean walled=false;
				if(tileX>bomb.getTileX())
				{
					for(int i = bomb.getTileX(); i<tileX; i++)
					{
						if(Board.getStaticBoard().isObstractle(i, tileY) && !Board.getStaticBoard().isBomb(i, tileY))
						{
							walled=true;
							break;
						}
					}
				}
				else
				{
					for(int i = tileX; i<bomb.getTileX(); i++)
					{
						if(Board.getStaticBoard().isObstractle(i, tileY) && !Board.getStaticBoard().isBomb(i, tileY))
						{
							walled=true;
							break;
						}
					}
				}
				if(!walled)
				{
					return false;
				}
			}
			else if(bomb.getTileX() == tileX &&Math.abs(bomb.getTileY()-tileY)<6)
			{
				boolean walled=false;
				if(tileY>bomb.getTileY())
				{
					for(int i = bomb.getTileY(); i<tileY; i++)
					{
						if(Board.getStaticBoard().isObstractle(tileX, i) && !Board.getStaticBoard().isBomb(tileX, i))
						{
							walled=true;
							break;
						}
					}
				}
				else
				{
					for(int i = tileY; i<bomb.getTileY(); i++)
					{
						if(Board.getStaticBoard().isObstractle(tileX, i) &&!Board.getStaticBoard().isBomb(tileX, i))
						{
							walled=true;
							break;
						}
					}
				}
				if(!walled)
				{
					return false;
				}
			}
		}
		return true;
	}
	public boolean tileIsSafe(int tileX, int tileY)
	{
		if(parent.isBomb(tileX, tileY))
		{
			return false;
		}
		for(Bomb bomb: Bomb.getBombList())
		{
			if(bomb.getTileY() ==tileY  && Math.abs(bomb.getTileX()-tileX)<6)
			{
				boolean walled=false;
				if(tileX>bomb.getTileX())
				{
					for(int i = bomb.getTileX(); i<tileX; i++)
					{
						if(Board.getStaticBoard().isObstractle(i, tileY) && !Board.getStaticBoard().isBomb(i, tileY))
						{
							walled=true;
							break;
						}
					}
				}
				else
				{
					for(int i = tileX; i<bomb.getTileX(); i++)
					{
						if(Board.getStaticBoard().isObstractle(i, tileY) && !Board.getStaticBoard().isBomb(i, tileY))
						{
							walled=true;
							break;
						}
					}
				}
				if(!walled)
				{
					return false;
				}
			}
			else if(bomb.getTileX() == tileX &&Math.abs(bomb.getTileY()-tileY)<6)
			{
				boolean walled=false;
				if(tileY>bomb.getTileY())
				{
					for(int i = bomb.getTileY(); i<tileY; i++)
					{
						if(Board.getStaticBoard().isObstractle(tileX, i) && !Board.getStaticBoard().isBomb(tileX, i))
						{
							walled=true;
							break;
						}
					}
				}
				else
				{
					for(int i = tileY; i<bomb.getTileY(); i++)
					{
						if(Board.getStaticBoard().isObstractle(tileX, i) &&!Board.getStaticBoard().isBomb(tileX, i))
						{
							walled=true;
							break;
						}
					}
				}
				if(!walled)
				{
					return false;
				}
			}
		}
		return true;
	}
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key==KeyEvent.VK_ESCAPE)
		{
			
		}
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
	public void explotion(int x, int y, int lowerTileX, int lowerTileY)
	{
		// både bomber og flammer har en posisjon på "map"  og en faktisk posisjon på lærettet 
		Fire f = new Fire ("fire.png", x-2,y, this, lowerTileX, lowerTileY); 
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
