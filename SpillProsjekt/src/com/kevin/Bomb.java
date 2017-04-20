package com.kevin;

import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Bomb extends Tile {

	private int delay;
	private final int waitDestruction = 100;
	private Player player;
	private int tileX;
	private int tileY;
	private static List<Bomb> global_bomb_list = new ArrayList<>();
	public Bomb(String imgSrc, int x, int y, Player player, int tileX, int tileY) 
	{
		super(imgSrc, x, y);
		delay = waitDestruction;
		this.player = player;
		this.tileX = tileX;
		this.tileY = tileY;
		global_bomb_list.add(this);
	}
	
	
	@Override
	public void tick()
	{

		destroy();
		delay--;
	}
	public static void removeBombs()
	{
		global_bomb_list = new ArrayList<>();
	}
	private void destroy()
	{
		if (delay <= 0)
		{
			try {
				  AePlayWave aw = new AePlayWave( "explosion.wav" );
			       aw.start();     
			}
			catch (Exception ez) {
			    System.out.println("f");
			}
			global_bomb_list.remove(this);
			delay = waitDestruction;
			player.explotion(getX(), getY(), getTileX(), getTileY());
			player.destroyBomb(this);
		}
	}
	
	public void explodeMe()
	{
		delay=0;
		destroy();
	}
	public void destructor()
	{
		global_bomb_list.remove(this);
		player.destroyBomb(this);
	}
	public int getTileX()
	{
		return tileX;
	}
	
	public int getTileY()
	{
		return tileY;
	}
	public static List<Bomb> getBombList()
	{
		return global_bomb_list;
	}
}
