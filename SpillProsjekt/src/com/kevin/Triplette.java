package com.kevin;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Triplette {
	private Image img;
	private Image newImage;
	private Image ogImage;
	private String string;
	private int val1, val2;
	private int widthScale;
	private int imageWidth, imageHeight;
	private int counter;
	public Triplette(Image img, int val1, int val2)
	{
		counter=0;
		this.img = img;
		this.newImage = img;
		this.ogImage=img;
		widthScale=-1;
		imageHeight=img.getHeight(Board.getStaticBoard());
		imageWidth=img.getWidth(Board.getStaticBoard());
		this.val1=val1;
		this.val2=val2;
	}
	public Triplette(String string, int val1, int val2)
	{
		this.string = string;
		this.val1=val1;
		this.val2=val2;
	}
	public void updateString(String string)
	{
		this.string = string;
	}
	public void updateWidth(double percentage)
	{
		//widthScale=percentage;
		img =img.getScaledInstance((int)Math.round(imageWidth*percentage/100), imageHeight, Image.SCALE_FAST);
		//img =img.getScaledInstance(imageWidth*percentage/100, img.getHeight(Board.getStaticBoard()), Image.SCALE_SMOOTH);
	}
	public String getString()
	{
		return string;
	}
	public Image getImg()
	{
		/*if(widthScale!=-1)
		{
			counter++;
			if(counter%100<50)
			{
				img=ogImage.getScaledInstance(imageWidth*100/100, imageHeight, Image.SCALE_FAST);
				return newImage;
			}
			else
			{
				newImage=ogImage.getScaledInstance(imageWidth*100/100, imageHeight, Image.SCALE_FAST);
				return img;
			}

		}*/
		return img;
	}
	public int getVal1()
	{
		return val1;
	}
	public int getVal2()
	{
		return val2;
	}
}
