package com.kevin;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
public class Menu{
	
	public boolean start;
	private boolean music;
	private boolean isTrump;
	private boolean playerAmount;
	private boolean howToPlay;
	private boolean twoPlayer;
	private Graphics2D g2d;
	
	private Element startBtn;
	private Element startBtnHover;
	private Element hillary;
	private Element trump;
	private Element hillaryHover;
	private Element trumpHover;
	private Element onePlayerBtn;
	private Element onePlayerBtnHover;
	private Element twoPlayerBtnHover;
	private Element howToPlayBtn;
	private Element howToPlayBtnHover;
	private Element backBtn;
	private Element backBtnHover;
	
	private Stack<Element> backStack = new Stack<>();
	
	private List<Element> buttons = new ArrayList<>();

	private Board parent;
	private Image background;

	/*private static void getAllFiles(File curDir)
	{
		File[] fileList = curDir.listFiles();
		for(File f : fileList)
		{
			if(f.isDirectory())
				System.out.println(f.getName());
			if(f.isFile())
				System.out.println(f.getName());
		}
	}*/
	public Menu (Board parent) 
	{
		/*File curDir = new File("./src");
		getAllFiles(curDir);*/
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("greenBlueBack.jpg");
		try{
			ImageIcon ii = new ImageIcon(ImageIO.read(is));
			background = ii.getImage();
		}
		catch(Exception e)
		{
			
		}

		
		this.parent = parent;
		
		startBtn = new Element(35, 200);
		startBtn.addImage("StartBtn.png", 0, 0);
		
		
		startBtnHover = new Element(35, 200);
		startBtnHover.addImage("StartBtnHover.png", 0, 0);
		
		trump = new Element(100, 400);
		trump.addImage("trumpHead.png", 0, 0);
		hillary = new Element(500, 400);
		hillary.addImage("hillaryHead.png", 0, 0);
		trumpHover = new Element(-78, 228);
		trumpHover.addImage("trumpHeadHover.png", 0, 0);
		hillaryHover = new Element(322, 228);
		hillaryHover.addImage("hillaryHeadHover.png", 0, 0);
		
		onePlayerBtn = new Element (300, 400);
		onePlayerBtn.addImage("OnePlayeBtn.png", 0, 0);
		onePlayerBtnHover = new Element(300, 400);
		onePlayerBtnHover.addImage("OnePlayerBtnHover.png", 0, 0);
		twoPlayerBtnHover = new Element(300, 400);
		twoPlayerBtnHover.addImage("twoPlayerBtnHover.png", 0, 0);
		
		howToPlayBtn = new Element(610, 200);
		howToPlayBtn.addImage("HowToPlayBtn.png", 0, 0);
		howToPlayBtnHover = new Element(610, 200);
		howToPlayBtnHover.addImage("HowToPlayBtnHover.png", 0, 0);
	
		
		backBtn = new Element(50, 50);
		backBtn.addImage("BackBtn.png", 0, 0);
		backBtnHover = new Element(50, 50);
		backBtnHover.addImage("BackBtnHover.png", 0, 0);
		
		resetMenu();
	}
	
	public void resetMenu()
	{
		g2d = null;
		start = false;
		music = true;
		isTrump = false;
		playerAmount = false;
		howToPlay = false;
		twoPlayer = false;
		buttons.clear();
		buttons.add(startBtn);
		buttons.add(howToPlayBtn);
	}

	public void setGraphics(Graphics g)
	{
		g2d = (Graphics2D) g;
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 24));
	}
	public void draw()
	{	
		g2d.drawImage(getImage(), 0, 0, parent);
		if (!start && !howToPlay)
		{
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("Trump.png");
			try{
				ImageIcon ii = new ImageIcon(ImageIO.read(is));
				g2d.drawImage(ii.getImage(), 25, 300, parent);
				is = this.getClass().getClassLoader().getResourceAsStream("snakkeboble.png");
				ii = new ImageIcon(ImageIO.read(is));
				g2d.drawImage(ii.getImage(), 170, 250, parent);
			}
			catch(Exception e)
			{
				
			}
			//g2d.drawImage(new ImageIcon("Trump.png").getImage(), 25, 300, parent);
			//g2d.drawImage(new ImageIcon("Hillary.png").getImage(), 76, 470, parent);
			//g2d.drawImage(new ImageIcon("bomb.png").getImage(), 140, 570, parent);
			//g2d.drawImage(new ImageIcon("snakkeboble.png").getImage(), 170, 250, parent);
			g2d.drawString("Grab em'" ,200, 325);
			g2d.drawString("by the pussy!" ,200, 355);
		}

		else if (!start && howToPlay)
		{
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("Controls_Player1.png");
			try{
				ImageIcon ii = new ImageIcon(ImageIO.read(is));
				g2d.drawImage(ii.getImage(), 100, 250, parent);
				is = this.getClass().getClassLoader().getResourceAsStream("Controls_Player2.png");
				ii = new ImageIcon(ImageIO.read(is));
				g2d.drawImage(ii.getImage(), 500, 250, parent);
				is = this.getClass().getClassLoader().getResourceAsStream("otherInstructions.png");
				ii = new ImageIcon(ImageIO.read(is));
				g2d.drawImage(ii.getImage(), 100, 450, parent);
			}
			catch(Exception e)
			{
				
			}
			g2d.setFont(new Font("TimesRoman", Font.BOLD, 36));
			g2d.setColor(Color.WHITE);
			g2d.drawString("How to play" ,295, 50);
			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 28));
			g2d.drawString("Controls" ,335, 185);
			g2d.setFont(new Font("TimesRoman", Font.ITALIC, 24));
			g2d.drawString("Player 1:" ,100, 235);
			//g2d.drawImage(new ImageIcon("Controls_Player1.png").getImage(), 100, 250, parent);
			g2d.drawString("Player 2:" ,500, 235);
			//g2d.drawImage(new ImageIcon("Controls_Player2.png").getImage(), 500, 250, parent);
			//g2d.drawImage(new ImageIcon("otherInstructions.png").getImage(), 100, 450, parent);
			//g2d.drawString("Player 2:" ,100, 550);
		}
		else if (start && !playerAmount)
		{
			g2d.drawString("How many players?" ,275, 340);
		}
		
		else if (!twoPlayer)
		{
			g2d.drawString("Character selection" ,275, 340);
		}
		else
		{
			g2d.drawString("Player 1 picks a character," ,240, 340);
			g2d.drawString("while player 2 automaticly playes the other character" ,125, 380);
		}
		
		if(!howToPlay)
		{
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("Title.png");
			try{
				ImageIcon ii = new ImageIcon(ImageIO.read(is));
				g2d.drawImage(ii.getImage(), 200, 5, parent);
			}
			catch(Exception e)
			{
				
			}
			
		}

		for (Element btn : buttons)
		{
			for(Triplette icon : btn.getIcons())
	   		{
	   			g2d.drawImage(icon.getImg(),icon.getVal1(), icon.getVal2(), parent);
	   		}
		}
	}

	public boolean isTwoPlayer()
	{
		return twoPlayer;
	}
	public boolean getHowToPlay()
	{
		return howToPlay;
	}
	public Stack<Element> getBackStack()
	{
		return backStack;
	}
	public boolean getStart()
	{
		return start;
	}
	public boolean getPlayerAmount()
	{
		return playerAmount;
	}
	
	public void setBackBtn()
	{
		Element lastBtn = backStack.pop();
		if (lastBtn == howToPlayBtn)
		{
			buttons.set(0, startBtn);
			buttons.add(howToPlayBtn);
			howToPlay = false;
		}
		else if (lastBtn == startBtn)
		{
			buttons.set(0, startBtn);
			buttons.set(1, howToPlayBtn);
			start = false;
		}
		else if (lastBtn == onePlayerBtn)
		{
			buttons.set(0, backBtn);
			buttons.set(1, onePlayerBtn);
			buttons.remove(2);
			playerAmount = false;
			twoPlayer = false;
		}
	}
	
	public void setHowToPlayBtnHover()
	{
		buttons.set(1, howToPlayBtnHover);
	}
	public void setHowToPlayBtn()
	{
		backStack.push(howToPlayBtn);
		buttons.set(0, backBtn);
		buttons.remove(1);
		howToPlay = true;
	}
	
	public void setBackBtnHover()
	{
		buttons.set(0, backBtnHover);
	}
	public void setStartBtnHover()
	{
		buttons.set(0, startBtnHover);
	}
	public void setStartBtn()
	{
		buttons.set(0, backBtn);
		buttons.set(1, onePlayerBtn);
		backStack.push(startBtn);
		start = true;
	}	
	
	public void setOnePlayerBtnHover()
	{
		buttons.set(1, onePlayerBtnHover);
		
	}
	public void setTwoPlayerBtnHover()
	{
		buttons.set(1, twoPlayerBtnHover);
	}
	public void setTwoPlayerBtn()
	{
		twoPlayer = true;
		setOnePlayerBtn();
	}
	public void setOnePlayerBtn()
	{
		playerAmount = true;
		buttons.set(0, backBtn);	
		buttons.set(1, trump);
		buttons.add(hillary);
		backStack.push(onePlayerBtn);
	}
	
	public void setTrumpHeadHover()
	{
		buttons.set(1, trumpHover);	
	}
	public void setTrumpHead()
	{
		buttons.set(1, trump);
		isTrump = true;
		parent.ready();
	}
	
	public void setHillaryHeadHover()
	{
		buttons.set(2, hillaryHover);
	}
	public void setHillaryHead()
	{
		buttons.set(2, hillary);
		parent.ready();
	}
	
	public boolean isTrump()
	{
		return isTrump;
	}
	
	public Image getImage()
	{
		return background;
	}
	
	public List<Element> getButtons()
	{
		return buttons;
	}
}
