package com.kevin;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener{

	private Board parent;
	private Menu menu;
	
	public Mouse(Board parent, Menu menu) 
	{
		this.menu = menu;
		this.parent = parent;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{		
		parent.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		if (!menu.getBackStack().isEmpty())
		{
			if (e.getX() > 50 && e.getY() > 100 && e.getX() < 190 && e.getY() < 160)
			{
				menu.setBackBtnHover();
			}
		}
		if (!menu.getStart() && !menu.getHowToPlay())
		{
			if (e.getX() > 40 && e.getY() > 205 && e.getX() < 175 && e.getY() < 260)
			{
				menu.setStartBtnHover();
			}

			else if (e.getX() > 615 && e.getY() > 205 && e.getX() < 750 && e.getY() < 260)
			{
				menu.setHowToPlayBtnHover();
			}		
		}
		
		if (menu.getStart())
		{
			if (e.getX() > 300 && e.getY() > 400 && e.getX() < 465 && e.getY() < 465)
			{
				menu.setOnePlayerBtnHover();
			}
			else if (e.getX() > 300 && e.getY() > 490 && e.getX() < 465 && e.getY() < 530)
			{
				menu.setTwoPlayerBtnHover();
			}
		}
	
		if (menu.getPlayerAmount())
		{
			if (e.getX() > 115 && e.getY() > 405 && e.getX() < 225 && e.getY() < 540)
			{
				menu.setTrumpHeadHover();
			}
			else if (e.getX() > 500 && e.getY() > 405 && e.getX() < 642 && e.getY() < 553)
			{
				menu.setHillaryHeadHover();
			}
		}
	
		parent.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if (!menu.getBackStack().isEmpty())
		{
			if (e.getX() > 50 && e.getY() > 100 && e.getX() < 190 && e.getY() < 160)
			{
				menu.setBackBtn();
			}
		}
		
		if (!menu.getStart() && !menu.getHowToPlay())
		{
			if (e.getX() > 40 && e.getY() > 205 && e.getX() < 175 && e.getY() < 260)
			{
				menu.setStartBtn();
			}
			
			else if (e.getX() > 615 && e.getY() > 205 && e.getX() < 750 && e.getY() < 260)
			{
				menu.setHowToPlayBtn();
			}
		}
	
		if (menu.getStart())
		{
			if (e.getX() > 300 && e.getY() > 400 && e.getX() < 465 && e.getY() < 465)
			{
				menu.setOnePlayerBtn();
			}
			else if (e.getX() > 300 && e.getY() > 490 && e.getX() < 465 && e.getY() < 530)
			{
				menu.setTwoPlayerBtn();
			}
		}
		
		if (menu.getPlayerAmount())
		{
			if (e.getX() > 115 && e.getY() > 405 && e.getX() < 225 && e.getY() < 540)
			{
				menu.setTrumpHead();
			}
			else if (e.getX() > 500 && e.getY() > 405 && e.getX() < 642 && e.getY() < 553)
			{
				menu.setHillaryHead();
			}
		}
	
		parent.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		parent.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		parent.repaint();
		
	}

}
