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
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {


	private static final long serialVersionUID = 1L;

	private boolean pause = false;

	private Timer timer;
    private final int DELAY = 1000/60;
    private List<Player> players;
    private List<List<Tile>> map;
    private static Board staticBoard;
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

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        timer = new Timer(DELAY, this);
        timer.start();
        bg = new Background("background.png");
        UI = new UserInterface(720-36,0);
        

    }
    public void initMenu()
    {
    	menu = new Menu(this);
    	this.addMouseListener(new Mouse(this, menu));

    }
    public void initPlayers()
    {
    	map = new ArrayList<>();
    	players = new ArrayList<>();
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
	        for (Player current : players)
	        {
	        	 g2d.drawImage(current.getImage(), current.getX(), current.getY(), this);
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
		   	
			if (pause)
    		{
				g2d.setFont(new Font("TimesRoman", Font.BOLD, 24));
				g2d.setColor(Color.WHITE);
    			g2d.drawString("PAUSE", 300+5,720/2);
    			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
    			g2d.drawString("press 'P' to continue", 300-35,720/2 + 20);
    		}
       }
   }


	public boolean isBoulder(int posX, int posY)
	{
		return (map.get(posX).get(posY) instanceof Boulder);
	}

	public boolean isBox(int posX, int posY)
	{
		return (map.get(posX).get(posY) instanceof Box);
	}
	
	public boolean isObstractle(int posX, int posY)
	{
		return isBoulder(posX, posY) || isBox(posX, posY);
	}
	
    @Override
    public void actionPerformed(ActionEvent e) {
       
    	if (state == State.GAME && !pause)
    	{
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
        		initPlayers();
        		state=State.MENU;
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
		   			setTile(fire.getTileX() + posOrNegX*i, fire.getTileY() + posOrNegY*i,
			   				new Tile("asfalt.jpg", fire.getX() - player.adjustX+2 + 36*i*posOrNegX,fire.getY() + i*36*posOrNegY - player.adjustY));
		   			fire.setBoundry(i, index);
		   		}
		   		for (Player playerCurrent : players)
		   		{
		   			if ((fire.getTileX() + posOrNegX*i == playerCurrent.getTileX() && fire.getTileY() + posOrNegY*i == playerCurrent.getTileY()) || 
		   					(fire.getTileX() + posOrNegX*i == playerCurrent.getUpperTileX() && fire.getTileY() + posOrNegY*i == playerCurrent.getTileY())
		   					|| (fire.getTileX() + posOrNegX*i == playerCurrent.getTileX() && fire.getTileY() + posOrNegY*i == playerCurrent.getUpperTileY())
		   					|| (fire.getTileX() + posOrNegX*i == playerCurrent.getUpperTileX() && fire.getTileY() + posOrNegY*i == playerCurrent.getUpperTileY()))
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
    	if (!menu.isTwoPlayer())
    	{
    		players.add(new Player(this, PlayerType.Player1,0));
			players.add(new NPC(this, PlayerType.AI,2));
    		//players.remove(1);
    		if (menu.isTrump())
        	{
    			System.out.println("ALRIGHT");
        		//players.get(0).setImage(new ImageIcon("TrumpSprites/TrumpFront.png"));
        		players.get(0).setSprites(TrumpSprites);
        	}
    		else
    		{
    			players.get(0).setImage(new ImageIcon("hillaryHead.png"));
    		}
    	}
    	else
    	{    	
    		
    		if (menu.isTrump())
    		{
    			players.add(new Player(this, PlayerType.Player1,0));
    			players.add(new NPC(this, PlayerType.AI,2));

        		//players.get(0).setImage(new ImageIcon("TrumpSprites/TrumpFront.png"));
        		players.get(0).setSprites(TrumpSprites);
        		players.get(1).setImage(new ImageIcon("hillaryHead.png"));
        	}
    		else
    		{
    			//players.get(1).setImage(new ImageIcon("TrumpSprites/TrumpFront.png"));
        		players.get(0).setSprites(TrumpSprites);
        		players.get(0).setImage(new ImageIcon("hillaryHead.png"));
    		}
    	}
    	  
    	for (Player npc : players)
    	{
    		if (npc.getId() == 2)
    		{
    			npc.setImage(new ImageIcon("player.png"));
    		}
    		else if (npc.getId() == 3)
    		{
    			//sette image til npc nr 2
    		}
    		else if (npc.getId() == 4)
			{
    			//sette image til npc nr 3
			}			
    	}
    	state = State.GAME;
    }

}















