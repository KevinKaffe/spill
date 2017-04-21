package com.kevin;

import java.awt.Image;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Background {
	private Image image;
	public Background(String imgSrc)
	{
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(imgSrc);
		try{
			ImageIcon ii = new ImageIcon(ImageIO.read(is));
			image = ii.getImage();
		}
		catch(Exception e)
		{
			
		}
		image =image.getScaledInstance(720, 720, Image.SCALE_DEFAULT);
	}
	public Image getImage()
	{
		return image;
	}
}
