package com.kevin;

import java.awt.Color;
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

import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private Player player;
    private final int DELAY = 10;
    private List<Enemy> enemies = new ArrayList<>();
    private List<List<Tile>> map = new ArrayList<>();
    private Background bg;
    private UserInterface UI;

    public Board() 
    {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);

        player = new Player(this, PlayerType.Player1);
        timer = new Timer(DELAY, this);
        timer.start();
        bg = new Background("background.png");
        UI = new UserInterface(0,720);
        for(int i =0; i <20; i++)
        {
        	ArrayList<Tile> temp=new ArrayList<>();
        	for(int j =0; j < 20; j++)
        	{
        		if (i == 0)
        		{
        			temp.add(new Boulder("boulder.png", i*36, j*36));
        		}
        		else if (i == 19)
        		{
        			temp.add(new Boulder("boulder.png", i*36, j*36));
        		}
        		else if (j == 0)
        		{
        			temp.add(new Boulder("boulder.png", i*36, j*36));
        		}
        		else if (j == 19)
        		{
        			temp.add(new Boulder("boulder.png", i*36, j*36));
        		}
        		else
        		{
        			temp.add(new Tile("tile.png", i*36, j*36));
        		}
        	}
        	map.add(temp);
        }
        
        for (int i = 3; i < 20; i += 3)
        {
        	for (int j = 3; j < 20; j += 3)
        	{
        		setTile(i,j,new Boulder("boulder.png", i*36, j*36));
        	}
        	 
        }
    }

    public void setTile(int x, int y, Tile t)
    {
    	map.get(x).set(y, t);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bg.getImage(), 0, 0, this);
        for(Enemy enemy:enemies)
        	g2d.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
        for(List<Tile> line:map)
        {
        	for(Tile tile:line)
        	{
            	g2d.drawImage(tile.getImage(), tile.getX(), tile.getY(), this);
        	}
        }
        g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);
        
        //---------- BOMBS'N EXPLOTIONS -----------------------
        for(Bomb bomb:player.getBombs())
        {
        	g2d.drawImage(bomb.getImage(),bomb.getX(), bomb.getY(), this);
        }
        for(FireSet fires: player.getFires())
        {
   	     Fire fire1 = fires.getFirePos(0);
   	     Fire fire2 = fires.getFirePos(1);
   	     Fire fire3 = fires.getFirePos(2);
   	   	 Fire fire4 = fires.getFirePos(3);

   	   	 explosion(g2d, fire1, 1,0);

   	   	 explosion(g2d, fire2, -1,0);

   	   	 explosion(g2d, fire3, 0,1);

   	   	 explosion(g2d, fire4, 0,-1);
        }


	   //-----------------------------------------
	   	g2d.drawImage(UI.getImage(),0, 720, this);
	   	for(Image i:UI.getImageIcons())
	   	{
		   	g2d.drawImage(i,100, 730, this);
	   	}
	   	for(Element elem:UI.getElements())
	   	{
	   		for(Triplette icon:elem.getIcons())
	   		{
	   			g2d.drawImage(icon.getImg(),icon.getVal1(), icon.getVal2(), this);
	   		}
	   	}
   }


	public boolean isBoulder(int posX, int posY)
	{
		return (map.get(posX).get(posY) instanceof Boulder);
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        for(Enemy enemy:enemies)
        {
        	enemy.tick();
        }
        for(List<Tile> line:map)
        {
        	for(Tile tile:line)
        	{
            	tile.tick();
        	}
        }
        
        for(Bomb bomb:player.getBombs())
        {
        	bomb.tick();
        }
        for(FireSet fires:player.getFires())
        {
        	fires.tick();
        }
        player.tick();
        repaint();  
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }
    
    private void explosion(Graphics2D g2d,Fire fire, int posOrNegX, int posOrNegY)
    {
    	for (int i = 0; i < player.getFireLevel(); i++)
	   	{
	    	if (fire != null)
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
		       	 g2d.drawImage(fire.getImage(), fire.getX()+posOrNegX*i*36,fire.getY()+posOrNegY*i*36, this);
		       	 setTile(fire.getTileX() + posOrNegX*i, fire.getTileY() + posOrNegY*i,
		       			 new Tower("road.png", fire.getX() - player.adjustX+2 + 36*i*posOrNegX,fire.getY() + i*36*posOrNegY - player.adjustY));
		   	 }
	   	}
    }
}