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

import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private final int DELAY = 1000/60;
    private List<Player> players = new ArrayList<>();
    private List<List<Tile>> map = new ArrayList<>();
    private Background bg;
    private static UserInterface UI;

    public Board() 
    {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);

        players.add(new Player(this, PlayerType.Player1,0));
        players.add(new Player(this, PlayerType.Player2,1));
        timer = new Timer(DELAY, this);
        timer.start();
        bg = new Background("background.png");
        UI = new UserInterface(720,0);
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
        			temp.add(new Tile("road.png", i*36, j*36));
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
        setTile(10,10, new Box ("box.png", 10*36, 10*36));
        setTile(11,11, new Box ("box.png", 11*36, 11*36));
        setTile(11,10, new Box ("box.png", 11*36, 10*36));
        setTile(10,11, new Box ("box.png", 10*36, 11*36));
    }

    public void setTile(int x, int y, Tile t)
    {
    	map.get(x).set(y, t);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Century", Font.BOLD, 16));
        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
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
        	
        }
       
  
//        }


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
   }


	public boolean isBoulder(int posX, int posY)
	{
		return (map.get(posX).get(posY) instanceof Boulder);
	}

	public boolean isBox(int posX, int posY)
	{
		return (map.get(posX).get(posY) instanceof Box);
	}
	
    @Override
    public void actionPerformed(ActionEvent e) {
       
        for(List<Tile> line:map)
        {
        	for(Tile tile:line)
        	{
            	tile.tick();
        	}
        }
        
        for (Player player : players)
        {
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
    
        repaint();  
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
        	for (Player player : players)
        	{
        		player.keyReleased(e);
        	}
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
        	for (Player player : players)
        	{
        		player.keyPressed(e);
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
	    		System.out.println(i);
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
			   				new Tile("road.png", fire.getX() - player.adjustX+2 + 36*i*posOrNegX,fire.getY() + i*36*posOrNegY - player.adjustY));
		   			fire.setBoundry(i, index);
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

}