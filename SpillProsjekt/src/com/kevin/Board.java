package com.kevin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

    public Board() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);

        craft = new Player(this);
        //enemies.add(new Enemy(craft));
        timer = new Timer(DELAY, this);
        timer.start();
        bg = new Background("background.png");
        for(int i =0; i <20; i++)
        {
        	ArrayList<Tile> temp=new ArrayList<>();
        	for(int j =0; j < 20; j++)
        	{
        		temp.add(new Tile("tile.png", i*36, j*36));
        	}
        	map.add(temp);
        		//map.get()(new Tile("tile.png", (i%20)*36, 36*(i-(i%20))/20));
        }
        map.set(4*20+5,new Boulder("boulder.png",5*36,4*36));
        map.set(5*20+5,new Boulder("boulder.png",5*36,5*36));
        map.set(6*20+5,new Boulder("boulder.png",5*36,6*36));
        map.set(7*20+6, new Boulder("boulder.png",6*36,7*36));
        setArea(0,3,10,3, new Boulder("boulder.png", 0,0));
        setArea(0,2,10,4,new Tile("block.png",0,0));
        setArea(0,3,10,5, new Tile("ls.png", 0,0));
        //setArea(10,2,12,10,new Tile("block.png",0,0));
    }
    private void setArea(int startX, int startY, int endX, int endY, Tile t)
    {
    	for(int i = startX; i<endX; i++)
    	{
    		for(int j = startY; j<endY; j++)
    		{
    			map.set(20*j+i, new Tile(t));
    			map.get(20*j+i).setX(i*36);
    			map.get(20*j+i).setY(j*36);
    		}
    	}
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
    }
	public boolean isBoulder(int posX, int posY)
	{
		return (map.get(posY*20+posX) instanceof Boulder);
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        for(Enemy enemy:enemies)
        {
        	enemy.tick();
        }
        for(Tile tile:map)
        	tile.tick();
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
}