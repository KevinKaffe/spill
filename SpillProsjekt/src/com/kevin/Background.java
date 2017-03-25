package com.kevin;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Background {
	private Image image;
	public Background(String imgSrc)
	{
		ImageIcon ii = new ImageIcon(imgSrc);
		image = ii.getImage();
		image =image.getScaledInstance(720, 720, Image.SCALE_DEFAULT);
	}
	public Image getImage()
	{
		return image;
	}
}
