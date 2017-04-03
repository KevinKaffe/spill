package com.kevin;

import java.util.concurrent.ThreadLocalRandom;

public class NPC extends Player{
	
	private int lookUp;
	private int lookDown;
	private int lookLeft;
	private int lookRight;
	private int lookLength = 4;
	
	private double lastVelX = 0;
	private double lastVelY = 0;
	
	private int decisionCount = 0;
	private int preGameCap = 0;
	private boolean madeDecision = false;
	private int wait = 40; //tiden NPC venter mellom hvert valg
	private int delay;


	private int randomNumber = 0; //trenger vel muligens en random int et sted her
	
	public NPC(Board board, PlayerType type, int id) 
	{
		super(board, type, id);
		
		maxBombs = 1;
		
		lookUp = lowerTileY - lookLength;
		lookDown = upperTileY + lookLength;
		lookLeft = lowerTileX - lookLength;
		lookRight = upperTileX + lookLength;
		delay = wait;
		speed = 6.0;
	}
	
	
	//Ting som må sjekkes real time
	public void realTimeDecision()
	{
		//oppdaterer verdier
		lookUp = lowerTileY - lookLength;
		lookDown = upperTileY + lookLength;
		lookLeft = lowerTileX - lookLength;
		lookRight = upperTileX + lookLength;
		
		//stopper før han går inn i flamme
		stopBeforeFire(1,0);
		stopBeforeFire(-1,0);
		stopBeforeFire(0,1);
		stopBeforeFire(0,-1);

		
		//starter klokka som tikker ned til neste ikke-real time valg
		delayBeforeDecision();
	}

	//klokka
	public void delayBeforeDecision()
	{
		if (delay <= 0)
		{
			delay = wait;
			nonRealTimeDecision();
			decisionCount++;
		}		
		delay--;
	}
	
	//Ting som ikke må sjekkes for fort (slik at valget ikke blir avbrutt med en gang)
	public void nonRealTimeDecision()
	{
		madeDecision = false;
		
		checkBombs();	
		
		//ser etter bomber
		if (!madeDecision)
		{
			isStoekk();	
		}
			
		if (!madeDecision)
		{
			decision(0);
		}
	}
	
	public void stopBeforeFire(int posOrNegX, int posOrNegY)
	{
		for (int i = 0; i < 5; i++)
	   	{
			for (Fire fire : Fire.getFireList())
			{
				if ((fire.getTileX() + posOrNegX*i >= lookLeft  && fire.getTileY() + posOrNegY*i <= lookDown) || 
						(fire.getTileX() + posOrNegX*i <= lookRight && fire.getTileY() + posOrNegY*i <= lookDown)
						|| (fire.getTileX() + posOrNegX*i >= lookLeft && fire.getTileY() + posOrNegY*i >= lookUp)
						|| (fire.getTileX() + posOrNegX*i <= lookRight && fire.getTileY() + posOrNegY*i >= lookUp))
				{	
					velX = 0;
					velY = 0;
					delay = wait;
				}			
			}
	   	}	
	}
	
	public void isStoekk()
	{
		//traff en boulder
		if (parent.isBoulder(lowerTileX, lowerTileY) || parent.isBoulder(upperTileX, lowerTileY)
				|| parent.isBoulder(lowerTileX, lowerTileY) || parent.isBoulder(upperTileX, lowerTileY))
		{
			decision(1);
			madeDecision = true;
			 
		}

		//traff en boks
		else if (parent.isBox(upperTileX, upperTileY) || parent.isBox(lowerTileX, upperTileY)
				|| parent.isBox(upperTileX, upperTileY) || parent.isBox(lowerTileX, upperTileY))
		{
			decision(2);
			madeDecision = true;
		}
	}
	
	public void checkBombs()
	{		
		for (Bomb bomb : Bomb.getBombList())
		{
			for (int i = 0; i < 5; i++)
			{ 
				//ser bombe til venstre for seg
				if (bomb.getTileX() == lookLeft + i && (bomb.getTileY() == lowerTileY || bomb.getTileY() == upperTileY))
				{						
					decision(3);
					madeDecision = true;
					return;
				}

				//ser en bombe til høyre for seg
				if (bomb.getTileX() == lookRight - i && (bomb.getTileY() == lowerTileY || bomb.getTileY() == upperTileY))
				{
					decision(4);
					madeDecision = true;
					return;
				}

				//ser bombe over seg
				if (bomb.getTileY() == lookUp + i && (bomb.getTileX() == lowerTileX || bomb.getTileX() == upperTileX))
				{
					decision(5);
					madeDecision = true;
					return;
				}
				
				//ser en bombe under seg
				if (bomb.getTileY() == lookDown - i && (bomb.getTileX() == lowerTileX || bomb.getTileX() == upperTileX))
				{
					decision(6);
					madeDecision = true;
					return;
				}
			}
		}	
	}
	
	
	//valget han gjør basert på hendelsen han står ovenfor
	public void decision(int n)
	{
		lastVelX = velX;
		lastVelY = velY;

		velX = 0;
		velY = 0;
		
		if (n == 0)
		{
			if (decisionCount <= preGameCap)
			{
				velX = speed;
			}
			else
			{
				randomNumber = ThreadLocalRandom.current().nextInt(0, 5);
				if (randomNumber == 0)
				{
					velX = 0; 
					velY = -speed;
				}
				else if(randomNumber == 1)
				{
					velX = -speed;
					velY = 0;
				}
				else if (randomNumber == 2)
				{
					velX = speed;
					velY = 0;
				}
				else if (randomNumber == 3)
				{
					velY = speed;
					velX = 0;
				}
			}
			
			return;
		}
		
		
		
		if (n == 1)
		{
			if (y >= 360)
			{
				velY = -speed;
			}
			else
			{
				velY = speed;
			}
		}
		else if (n == 2)
		{
			if (bombs.size()<maxBombs)
			{
				bombs.add(new Bomb("bomb.png", getTileX()*36+ adjustX, getTileY()*36 + adjustY, this, 
						getTileX(), getTileY()));
			}
			
			if (lastVelY == -speed)
			{
				velY = speed;
			}
			
			lastVelY = velY;
		}
		else if (n == 3)
		{
			if (isStoekk2()) //ønsker å gå i y-retning, men er støkk i y-retning -> han må gå i x-retning
			{
				velY = 0;
				velX = speed; //n = 3 betyr bombe på venstre hold -> gå mot høyre
				return;
			}

			if (y >= 360)
			{
				velY = -speed;
			}
			else				
			{
				velY = speed;
			}
			
		}
		else if (n == 4)
		{
			if (isStoekk2()) //ønsker å gå i y-retning, men er støkk i y-retning -> han må gå i x-retning
			{
				velY = 0;
				velX = -speed; //n = 4 betyr bombe på høyre hold -> gå mot venstre
				return;
			}

			if (y >= 360)
			{
				velY = -speed;
			}
			else
			{
				velY = speed;
			}
		}
		else if (n == 5)
		{
			if (isStoekk2()) 
			{
				velX = 0;
				velY = speed; 
				return;
			}
			
			if (x >= 402)
			{
				velX = -speed;
			}
			else	
			{
				velX = speed;
			}
		}
		else if (n == 6)
		{
			if (isStoekk2()) 
			{
				velX = 0;
				velY = -speed; 
				return;
			}

			if (x >= 402)
			{
				velX = -speed;
			}
			else
			{
				velX = speed;
			}
		}
	}
	
	public boolean isStoekk2()
	{
		//støkk i x-retning
		if (parent.isBoulder(lowerTileX, lowerTileY) || parent.isBoulder(upperTileX, lowerTileY))
		{
			return true; 
		}
		//støkk i y-retning
		else if (parent.isBoulder(upperTileX, upperTileY) || parent.isBoulder(lowerTileX, upperTileY))
		{
			return true;
		}
		return false;
	}
}
















