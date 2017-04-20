package com.kevin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {


	private static final long serialVersionUID = 1L;
	private boolean youwin = false;
	private boolean gameover = false;
	private boolean pause = false;
	private AePlayWave aw;
	private Timer timer;
	private Mouse mouse;
    private final int DELAY = 1000/60;
    private List<Player> players;
    private List<List<Tile>> map;
    private static Board staticBoard;
    private static List<Powerup> powerupTable;
    private static List<Double> probableTable;
    private List<Powerup> powerupBoard = new ArrayList<>();
    private String[] TrumpSprites = {"TrumpSprites/Front/0.png",
    		"TrumpSprites/Front/1.png",
    		"TrumpSprites/Front/0.png",
    		"TrumpSprites/Front/2.png",
    		"TrumpSprites/Right/0.png",
    		"TrumpSprites/Right/1.png",
    		"TrumpSprites/Right/0.png",
    		"TrumpSprites/Right/2.png",
    		"TrumpSprites//Left/0.png",
    		"TrumpSprites//Left/1.png",
    		"TrumpSprites//Left/0.png",
    		"TrumpSprites//Left/2.png",
    		"TrumpSprites/Behind/0.png",
    		"TrumpSprites/Behind/1.png",
    		"TrumpSprites/Behind/0.png",
    		"TrumpSprites/Behind/2.png"};
    private String[] HillarySprites = {"HillarySprites/Front/0.png",
    		"HillarySprites/Front/1.png",
    		"HillarySprites/Front/0.png",
    		"HillarySprites/Front/2.png",
    		"HillarySprites/Right/0.png",
    		"HillarySprites/Right/1.png",
    		"HillarySprites/Right/0.png",
    		"HillarySprites/Right/2.png",
    		"HillarySprites//Left/0.png",
    		"HillarySprites//Left/1.png",
    		"HillarySprites//Left/0.png",
    		"HillarySprites//Left/2.png",
    		"HillarySprites/Behind/0.png",
    		"HillarySprites/Behind/1.png",
    		"HillarySprites/Behind/0.png",
    		"HillarySprites/Behind/2.png"};
    private String[] WomanNpcSprites = {"NPCWoman/Front/0.png",
    		"NPCWoman/Front/1.png",
    		"NPCWoman/Front/0.png",
    		"NPCWoman/Front/2.png",
    		"NPCWoman/Right/0.png",
    		"NPCWoman/Right/1.png",
    		"NPCWoman/Right/0.png",
    		"NPCWoman/Right/2.png",
    		"NPCWoman//Left/0.png",
    		"NPCWoman//Left/1.png",
    		"NPCWoman//Left/0.png",
    		"NPCWoman//Left/2.png",
    		"NPCWoman/Behind/0.png",
    		"NPCWoman/Behind/1.png",
    		"NPCWoman/Behind/0.png",
    		"NPCWoman/Behind/2.png"};
    private String[] ManNpcSprites = {"NpcMan/Front/0.png",
    		"NpcMan/Front/1.png",
    		"NpcMan/Front/0.png",
    		"NpcMan/Front/2.png",
    		"NpcMan/Right/0.png",
    		"NpcMan/Right/1.png",
    		"NpcMan/Right/0.png",
    		"NpcMan/Right/2.png",
    		"NpcMan//Left/0.png",
    		"NpcMan//Left/1.png",
    		"NpcMan//Left/0.png",
    		"NpcMan//Left/2.png",
    		"NpcMan/Behind/0.png",
    		"NpcMan/Behind/1.png",
    		"NpcMan/Behind/0.png",
    		"NpcMan/Behind/2.png"};
    private Background bg;
    private static UserInterface UI;
    private Menu menu;
    private boolean inital = false;
    
    
    private State state = State.MENU;
    
    public Board() 
    {
    	staticBoard = this;
    	initMenu();
    	initPlayers();
    	setPowerupTables();
    	
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        timer = new Timer(DELAY, this);
        timer.start();
        bg = new Background("background.png");
        UI = new UserInterface(720-36,0);
		try {
			  aw = new AePlayWave( "mercy.wav");
		       aw.start();     
		}
		catch (Exception ez) {
		    
		}

    }

    public void setPowerupTables()
    {
    	String[] sprites=new String[]{"SpeedUp/0.png", "SpeedUp/1.png", "SpeedUp/2.png", "SpeedUp/3.png", "SpeedUp/4.png", "SpeedUp/5.png"};
    	Powerup speedUp = new Powerup(sprites, Effect.SpeedUp);
    	sprites= new String[]{"SpeedDown/0.png", "SpeedDown/1.png", "SpeedDown/2.png", "SpeedDown/3.png", "SpeedDown/4.png", "SpeedDown/5.png"};
    	Powerup speedDown = new Powerup(sprites, Effect.SpeedDown);
    	sprites=new String[]{"SpeedMax/0.png", "SpeedMax/1.png", "SpeedMax/2.png", "SpeedMax/3.png", "SpeedMax/4.png", "SpeedMax/5.png"};
    	Powerup speedMax = new Powerup(sprites, Effect.SpeedMax);
    	sprites= new String[]{"SpeedMin/0.png", "SpeedMin/1.png", "SpeedMin/2.png", "SpeedMin/3.png", "SpeedMin/4.png", "SpeedMin/5.png"};
    	Powerup speedMin = new Powerup(sprites, Effect.SpeedMin);
    	
    	sprites=new String[]{"FireUp/0.png", "FireUp/1.png", "FireUp/2.png", "FireUp/3.png", "FireUp/4.png", "FireUp/5.png"};
    	Powerup fireUp = new Powerup(sprites, Effect.FireUp);
    	sprites= new String[]{"FireDown/0.png", "FireDown/1.png", "FireDown/2.png", "FireDown/3.png", "FireDown/4.png", "FireDown/5.png"};
    	Powerup fireDown = new Powerup(sprites, Effect.FireDown);
    	sprites=new String[]{"FireMax/0.png", "FireMax/1.png", "FireMax/2.png", "FireMax/3.png", "FireMax/4.png", "FireMax/5.png"};
    	Powerup fireMax = new Powerup(sprites, Effect.FireMax);
    	sprites= new String[]{"FireMin/0.png", "FireMin/1.png", "FireMin/2.png", "FireMin/3.png", "FireMin/4.png", "FireMin/5.png"};
    	Powerup fireMin = new Powerup(sprites, Effect.FireMin);
    	
    	sprites=new String[]{"bombUp/0.png", "bombUp/1.png", "bombUp/2.png", "bombUp/3.png", "bombUp/4.png", "bombUp/5.png"};
    	Powerup bombUp = new Powerup(sprites, Effect.BombUp);
    	sprites= new String[]{"bombDown/0.png", "bombDown/1.png", "bombDown/2.png", "bombDown/3.png", "bombDown/4.png", "bombDown/5.png"};
    	Powerup bombDown = new Powerup(sprites, Effect.BombDown);
    	sprites=new String[]{"bombMax/0.png", "bombMax/1.png", "bombMax/2.png", "bombMax/3.png", "bombMax/4.png", "bombMax/5.png"};
    	Powerup bombMax = new Powerup(sprites, Effect.BombMax);
    	sprites= new String[]{"bombMin/0.png", "bombMin/1.png", "bombMin/2.png", "bombMin/3.png", "bombMin/4.png", "bombMin/5.png"};
    	Powerup bombMin = new Powerup(sprites, Effect.BombMin);
    	
    	sprites=new String[]{"bombPass/0.png", "bombPass/1.png", "bombPass/2.png", "bombPass/3.png", "bombPass/4.png", "bombPass/5.png"};
    	Powerup bombPass = new Powerup(sprites, Effect.BombPass);
    	sprites= new String[]{"softPass/0.png", "softPass/1.png", "softPass/2.png", "softPass/3.png", "softPass/4.png", "softPass/5.png"};
    	Powerup softPass = new Powerup(sprites, Effect.SoftPass);
    	
    	sprites=new String[]{"SpeedUp/0.png", "SpeedUp/1.png", "SpeedUp/2.png", "SpeedUp/3.png", "SpeedUp/4.png", "SpeedUp/5.png"};
    	
    	powerupTable= Arrays.asList(speedUp, speedDown, speedMax, speedMin, fireUp, fireDown, fireMax, fireMin, bombUp, bombDown, bombMax, bombMin, bombPass, softPass, null);
    	probableTable= Arrays.asList(30.0  , 10.0     , 4.0     , 2.0     , 30.0  , 10.0    , 4.0    , 2.0    , 30.0  ,10.0     , 4.0    ,2.0     , 2.0     ,2.0      , 300.0);
    }
    public void initMenu()
    {
    	menu = new Menu(this);
    	mouse= new Mouse(this,menu);
    	this.addMouseListener(mouse);

    }
    public void initPlayers()
    {
    	Bomb.removeBombs();
    	Fire.removeFires();
    	map = new ArrayList<>();
    	players = new ArrayList<>();
    	gameover = false;
    	youwin = false;
    	pause = false;
    	powerupBoard = new ArrayList<>();
  	    /*players.add(new Player(this, PlayerType.Player1,0));
  	    players.add(new Player(this, PlayerType.Player2,1));
  	    players.add(new NPC(this, PlayerType.AI,2));*/
        for(int i =0; i <19; i++)
        {
        	ArrayList<Tile> temp=new ArrayList<>();
        	for(int j =0; j < 19; j++)
        	{
        		if (i == 0)
        		{
        			temp.add(new Boulder("boulder.png", i*36, j*36));
        		}
        		else if (i == 18)
        		{
        			temp.add(new Boulder("boulder.png", i*36, j*36));
        		}
        		else if (j == 0)
        		{
        			temp.add(new Boulder("boulder.png", i*36, j*36));
        		}
        		else if (j == 18)
        		{
        			temp.add(new Boulder("boulder.png", i*36, j*36));
        		}
        		else
        		{
        			temp.add(new Tile("asfalt.jpg", i*36, j*36));
        		}
        	}
        	map.add(temp);
        }
        
        for (int i = 3; i < 16; i += 3)
        {
        	for (int j = 3; j < 16; j += 3)
        	{
        		setTile(i,j,new Boulder("boulder.png", i*36, j*36));
        	}
        	 
        }
        

        for (int i = 1; i < 19; i+= 3)
        {
        	for (int j = 1; j < 18; j += 3)
        	{
        		if (!(j == 1 && i == 1 ) && !(i == 1 && j == 16) && !(i == 16 && j == 1) && !(i == 16 && j == 16))
        		{
            	  	setTile(i,j, new Box ("box.png", (i)*36, (j)*36));
                	setTile(i,j+1, new Box ("box.png", (i)*36, (j+1)*36));
                	setTile(i+1,j, new Box ("box.png", (i+1)*36, (j)*36));
                	setTile(i+1,j+1, new Box ("box.png", (i+1)*36, (j+1)*36));
        		}
        	}    
        }


    }
    public List<Powerup> getPowerupBoard()
    {
    	return powerupBoard;
    }
    public void addPowerup(Powerup p)
    {
    	powerupBoard.add(p);
    }
    public void removePowerup(Powerup p)
    {
    	powerupBoard.remove(p);
    }
    public void setTile(int x, int y, Tile t)
    {
    	map.get(x).set(y, t);
    }
    public static Board getStaticBoard()
    {
    	return staticBoard;
    }
    
    public Tile getTileMap(int x, int y)
    {
    	return map.get(x).get(y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Verdana", Font.BOLD, 14));

        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }

    public void setState(State state)
    {
    	this.state = state;
    }
    public List<Player> getPlayers() {
		return players;
	}
    private void doDrawing(Graphics g) {
    	
    	Graphics2D g2d = (Graphics2D) g;
    	
    	if (state == State.MENU)
    	{
    		menu.setGraphics(g);
    		menu.draw();
    	}
    	
    	else
    	{
    		
	        g2d.drawImage(bg.getImage(), 0, 0, this);
	
	        for(List<Tile> line:map)
	        {
	        	for(Tile tile:line)
	        	{
	            	g2d.drawImage(tile.getImage(), tile.getX(), tile.getY(), this);
	        	}
	        }
    		for(Powerup p: powerupBoard)
    		{
    			g2d.drawImage(p.getImage(), p.getX(), p.getY(), this);
    		}
	        for (Player current : players)
	        {
	        	 g2d.drawImage(current.getImage(), current.getX(), current.getAdjY(), this);
	        }
	       
	        
	        //---------- BOMBS'N EXPLOTIONS -----------------------
	        for (Player current : players)
	        {
	            for(Bomb bomb:current.getBombs())
	            {
	            	g2d.drawImage(bomb.getImage(),bomb.getX(), bomb.getY(), this);
	            }
	            for(Fire fire: current.getFires())
	            {
	
	       	   	 explotionRealTime(g2d, fire, 1,0, 0, current);
	
	      	   	 explotionRealTime(g2d, fire, -1,0, 1, current);
	
	      	   	 explotionRealTime(g2d, fire, 0,1, 2, current);
	
	      	   	 explotionRealTime(g2d, fire, 0,-1, 3, current);
	            }
	        	if (current.getIsDead())
	        	{
	        		players.remove(current);
	        	}
	        }
	       
	
	
		   //-----------------------------------------
		   	g2d.drawImage(UI.getImage(),UI.getX(), UI.getY(), this);
		   	for(Element elem:UI.getElements())
		   	{
		   		for(Triplette icon:elem.getIcons())
		   		{
		   			g2d.drawImage(icon.getImg(),icon.getVal1(), icon.getVal2(), this);
		   		}
		   		for(Triplette strings:elem.getStrings())
		   		{
		   			g2d.drawString(strings.getString(), strings.getVal1(), strings.getVal2());
		   		}
		   	}
		   	
		   	if (!(menu.isTwoPlayer()))
		   	{
			if (pause && !gameover && players.size() != 1)
    		{
				g2d.setFont(new Font("TimesRoman", Font.BOLD, 24));
				g2d.setColor(Color.WHITE);
    			g2d.drawString("PAUSE", 300+5,720/2);
    			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
    			g2d.drawString("press 'P' to continue", 300-35,720/2 + 20);
    		}
			else if (gameover)
			{
				pause = true;
				g2d.setFont(new Font("TimesRoman", Font.BOLD, 24));
				g2d.setColor(Color.WHITE);
    			g2d.drawString("GAME OVER", 300+5,720/2);
    			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
    			g2d.drawString("press escape to exit", 300-35,720/2 + 20);
			}
			else if (players.size() == 1)
			{
				pause = true;
				g2d.setFont(new Font("TimesRoman", Font.BOLD, 24));
				g2d.setColor(Color.WHITE);
    			g2d.drawString("You win!", 300+5,720/2);
    			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
    			g2d.drawString("press escape to exit", 300-35,720/2 + 20);
			}
		   	}
		   	else
		   	{
		   		boolean player1Dead = true;
		   		boolean player2Dead = true;
		   		for (Player player : players)
		   		{
		   			if (player.getId() == 1)
		   			{
		   				player1Dead = false;
		   			}
		   			else if (player.getId() == 2)
		   			{
		   				player2Dead = false;
		   			}
		   				
		   		}
		   		
		   		if (pause && (!player1Dead  || !player2Dead) && players.size() != 1)
	    		{
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 24));
					g2d.setColor(Color.WHITE);
	    			g2d.drawString("PAUSE", 300+5,720/2);
	    			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
	    			g2d.drawString("press 'P' to continue", 300-35,720/2 + 20);
	    		}
				else if (!player1Dead && player2Dead && players.size() == 1)
				{
					pause = true;
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 24));
					g2d.setColor(Color.WHITE);
	    			g2d.drawString("Player 1 won!", 300+5,720/2);
	    			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
	    			g2d.drawString("press escape to exit", 300-35,720/2 + 20);
				}
				else if (!player2Dead && player1Dead && players.size() == 1)
				{
					pause = true;
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 24));
					g2d.setColor(Color.WHITE);
	    			g2d.drawString("Player 2 won!", 300+5,720/2);
	    			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
	    			g2d.drawString("press escape to exit", 300-35,720/2 + 20);
				}	
				else if (player2Dead && player1Dead)
				{
					pause = true;
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 24));
					g2d.setColor(Color.WHITE);
	    			g2d.drawString("Game over", 300+5,720/2);
	    			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
	    			g2d.drawString("press escape to exit", 300-35,720/2 + 20);
				}	
		   	}
			
       }
   }


	public boolean isBoulder(int posX, int posY)
	{
		return (map.get(posX).get(posY) instanceof Boulder);
	}
	public boolean isPlayer(int posX, int posY, Player player)
	{
		for(Player p:players)
		{
			if(p.getAvgTileX()==posX && p.getAvgTileY()==posY && player!=p)
			{
				return true;
			}
		}
		return false;
	}
	public boolean isTrumpWall(int posX, int posY)
	{
		return (map.get(posX).get(posY) instanceof TrumpWall);
	}
	public boolean isBomb(int posX, int posY)
	{
		for(Bomb b:Bomb.getBombList())
		{
			if(b.getTileX()==posX && b.getTileY()==posY)
			{
				return true;
			}
		}
		return false;
	}
    public void addMyListener()
    {
    	this.addMouseListener(mouse);
    }
	public boolean isBox(int posX, int posY)
	{
		return (map.get(posX).get(posY) instanceof Box);
	}
	public boolean isEmpty(int posX, int posY)
	{
		return !(isBoulder(posX, posY) || isTrumpWall(posX, posY) || isBox(posX, posY) || isBomb(posX, posY));
	}
	
	public boolean isObstractle(int posX, int posY)
	{
		return isBoulder(posX, posY) || isBox(posX, posY) || isBomb(posX, posY);
	}
    public static List<Powerup> getPowerupTable()
    {
    	return powerupTable;
    }
    public static List<Double> getProbableTable()
    {
    	return probableTable;
    }
    public void setGameover()
    {
    	gameover = true;
    }
    public Menu getMenu()
    {
    	return menu;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       
    	if (state == State.GAME && !pause)
    	{
    		for(Powerup p: powerupBoard)
    		{
    			p.tick();
    		}
    		for(List<Tile> line:map)
            {
            	for(Tile tile:line)
            	{
                	tile.tick();
            	}
            }
       
            for (Player player : players)
            {
            	

            	 if (player instanceof NPC)
                 {
                  	((NPC) player).realTimeDecision();
                 }
                for(Bomb bomb:player.getBombs())
                {
                	bomb.tick();
                }
                for(Fire fire:player.getFires())
                {
                	fire.tick();
                }
                player.tick();
                
              
            }

    	}
        repaint();

}
    
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
    		if (e.getKeyCode() == KeyEvent.VK_P)
    		{
    			pause = !pause;		
    		}
        	for (Player player : players)
        	{
        		if (!(player instanceof NPC))
        		{
        			player.keyReleased(e);
        		}
        		
        	}
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
        	if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
        	{
        		players.clear();
        		menu.resetMenu();
        		aw.stop();
        		aw = new AePlayWave( "mercy.wav");
 		       aw.start(); 
        		state=State.MENU;
            	Board.getStaticBoard().addMyListener();
        	}
        	for (Player player : players)
        	{
        		if (!(player instanceof NPC))
        		{
        			player.keyPressed(e);
        		}
        	}
        }
    }
    public static UserInterface get_ui()
    {
    	return UI;
    }
    private void explotionRealTime(Graphics2D g2d,Fire fire, int posOrNegX, int posOrNegY, int index, Player player)
    {
    	for (int i = 0; i < player.getFireLevel(); i++)
	   	{
	    	if (i < fire.getBoundry(index))
		   	 {
	    		for(int j =0; j < powerupBoard.size(); j++)
	    		{
	    			if(powerupBoard.get(j).getX()/36==fire.getTileX()+posOrNegX*i && powerupBoard.get(j).getY()/36 == fire.getTileY() + posOrNegY*i)
	    			{
	    				powerupBoard.get(j).removeMe();
	    			}
	    		}
		   		if (isBoulder(fire.getTileX()+posOrNegX*i, fire.getTileY() + posOrNegY*i))
		   		{
		   			break;
		   		}
		   		for(Bomb bomb:Bomb.getBombList())
		   		{
		   			if(fire.getTileX()+posOrNegX*i==bomb.getTileX() &&  fire.getTileY() + posOrNegY*i==bomb.getTileY())
		   			{
		   				bomb.explodeMe();
		   			}
		   		}
		   		if (isBox(fire.getTileX() + posOrNegX*i, fire.getTileY() + posOrNegY*i))
		   		{
		   			if(map.get(fire.getTileX()+posOrNegX*i).get(fire.getTileY()+posOrNegY*i) instanceof Box)
		   				((Box)(map.get(fire.getTileX()+posOrNegX*i).get(fire.getTileY()+posOrNegY*i))).ripMe();
		   			setTile(fire.getTileX() + posOrNegX*i, fire.getTileY() + posOrNegY*i,
			   				new Tile("asfalt.jpg", fire.getX() - player.adjustX+2 + 36*i*posOrNegX,fire.getY() + i*36*posOrNegY - player.adjustY));
		   			fire.setBoundry(i, index);
		   		}
		   		if (isTrumpWall(fire.getTileX() + posOrNegX*i, fire.getTileY() + posOrNegY*i))
		   		{  		
		   			setTile(fire.getTileX() + posOrNegX*i, fire.getTileY() + posOrNegY*i,
			   				new Tile("asfalt.jpg", fire.getX() - player.adjustX+2 + 36*i*posOrNegX,fire.getY() + i*36*posOrNegY - player.adjustY));
		   			fire.setBoundry(i, index);
		   		}
		   		for (Player playerCurrent : players)
		   		{
		   			if ((fire.getTileX() + posOrNegX*i == playerCurrent.getAvgTileX() && fire.getTileY() + posOrNegY*i == playerCurrent.getAvgTileY()) /*|| 
		   					/*(fire.getTileX() + posOrNegX*i == playerCurrent.getUpperTileX() && fire.getTileY() + posOrNegY*i == playerCurrent.getTileY())
		   					|| (fire.getTileX() + posOrNegX*i == playerCurrent.getTileX() && fire.getTileY() + posOrNegY*i == playerCurrent.getUpperTileY())
		   					|| (fire.getTileX() + posOrNegX*i == playerCurrent.getUpperTileX() && fire.getTileY() + posOrNegY*i == playerCurrent.getUpperTileY())*/)
		   			{

		   				playerCurrent.decreaseHealth();
		   			}
		   		}
		       	g2d.drawImage(fire.getImage(), fire.getX()+posOrNegX*i*36,fire.getY()+posOrNegY*i*36, this);
		   	 }
	    	else if (i == fire.getBoundry(index))
	    	{
	    		g2d.drawImage(fire.getImage(), fire.getX()+posOrNegX*i*36,fire.getY()+posOrNegY*i*36, this);
	    		break;
	    	}
	   	}
    }
    
    public void ready()
    {    
    	players.clear();
		initPlayers();
    	if (!menu.isTwoPlayer())
    	{
    		players.add(new Player(this, PlayerType.Player1,1));
			players.add(new NPC(this, PlayerType.AI,2));
			players.add(new NPC(this, PlayerType.AI,3));
			players.add(new NPC(this, PlayerType.AI,4));
    		//players.remove(1);
    		if (menu.isTrump())
        	{
    			
        		//players.get(0).setImage(new ImageIcon("TrumpSprites/TrumpFront.png"));
        		players.get(0).setSprites(TrumpSprites,Character.Trump);
        	}
    		else
    		{
    			//players.get(0).setImage(new ImageIcon("hillaryHead.png"));
    			players.get(0).setSprites(HillarySprites,Character.Hillary);
    		}
    	}
    	else
    	{    	
    		
    		if (menu.isTrump())
    		{
    			players.add(new Player(this, PlayerType.Player1,1));
    			players.add(new Player(this, PlayerType.Player2,2));
    			players.add(new NPC(this, PlayerType.AI,3));
    			players.add(new NPC(this, PlayerType.AI,4));

        		//players.get(0).setImage(new ImageIcon("TrumpSprites/TrumpFront.png"));
        		players.get(0).setSprites(TrumpSprites,Character.Trump);
        		players.get(1).setSprites(HillarySprites, Character.Hillary);
        	}
    		else
    		{
    			players.add(new Player(this, PlayerType.Player1,1));
    			players.add(new Player(this, PlayerType.Player2,2));
    			players.add(new NPC(this, PlayerType.AI,3));
    			players.add(new NPC(this, PlayerType.AI,4));

        		//players.get(0).setImage(new ImageIcon("TrumpSprites/TrumpFront.png"));
    			players.get(1).setSprites(TrumpSprites,Character.Trump);
        		players.get(0).setSprites(HillarySprites, Character.Hillary);
    		}
    	}
    	  
    	for (Player npc : players)
    	{
    		if (npc.getId() == 2)
    		{
    			if (menu.isTrump())
    			{
        			npc.setSprites(HillarySprites, Character.Hillary);
    			}
    			else{
        			npc.setSprites(TrumpSprites, Character.Hillary);
    			}
    			
    		}
    		else if (npc.getId() == 3)
    		{
    			npc.setSprites(WomanNpcSprites, Character.Hillary);
    		}
    		else if (npc.getId() == 4)
			{
    			npc.setSprites(ManNpcSprites, Character.Hillary);
			}			
    	}
		try {
				aw.stop();
			  aw = new AePlayWave( "theDon.wav");
		       aw.start();     
		}
		catch (Exception ez) {
		    
		}
    	this.removeMouseListener(mouse);
    	state = State.GAME;
    }

}















