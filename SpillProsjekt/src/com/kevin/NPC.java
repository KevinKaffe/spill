package com.kevin;

import java.util.ArrayList;
import java.util.List;
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
	private List<Pair> avoidDupes;
	private int testX, testY;

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
		if(!tileIsSafe(getTileX(), getTileY()))
		{
			gotoTile(getClosestSafeTile(getTileX(), getTileY()));
		}
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
	private boolean tileIsSafe(int tileX, int tileY)
	{
		for(Bomb bomb: Bomb.getBombList())
		{
			if(bomb.getTileY() ==tileY  && Math.abs(bomb.getTileX()-tileX)<6)
			{
				boolean walled=false;
				if(tileX>bomb.getTileX())
				{
					for(int i = bomb.getTileX(); i<tileX; i++)
					{
						if(Board.getStaticBoard().isObstractle(i, tileY))
						{
							walled=true;
							break;
						}
					}
				}
				else
				{
					for(int i = tileX; i<bomb.getTileX(); i++)
					{
						if(Board.getStaticBoard().isObstractle(i, tileY))
						{
							walled=true;
							break;
						}
					}
				}
				if(!walled)
				{
					return false;
				}
			}
			else if(bomb.getTileX() == tileX &&Math.abs(bomb.getTileY()-tileY)<6)
			{
				boolean walled=false;
				if(tileY>bomb.getTileY())
				{
					for(int i = bomb.getTileY(); i<tileY; i++)
					{
						if(Board.getStaticBoard().isObstractle(tileX, i))
						{
							walled=true;
							break;
						}
					}
				}
				else
				{
					for(int i = tileY; i<bomb.getTileY(); i++)
					{
						if(Board.getStaticBoard().isObstractle(tileX, i))
						{
							walled=true;
							break;
						}
					}
				}
				if(!walled)
				{
					return false;
				}
			}
		}
		return true;
	}
	private Pair getClosestSafeTile(int tileX, int tileY)
	{
		avoidDupes=new ArrayList<>();
		if(tileIsSafe(tileX, tileY))
		{
			return new Pair(tileX, tileY);
		}
		//tileX-=1;
		//tileY-=1;
		System.out.println(tileX+"... "+tileY);
		avoidDupes.add(new Pair(tileX, tileY));
		Pair left=recursiveSearch(tileX-1, tileY);
		Pair up = recursiveSearch(tileX, tileY-1);
		Pair right = recursiveSearch(tileX+1, tileY);
		Pair down = recursiveSearch(tileX, tileY+1);
		return minPair(minPair(left,right),minPair(up,down));
	}
	private void gotoTile(Pair p)
	{
		if(p.getFirst()<0)
		{
			return;
		}
		x=p.getFirst()*36;
		y=p.getSecond()*36;
		
	}
	private Pair recursiveSearch(int tileX, int tileY)
	{
		if(tileX>18 || tileX<0 || tileY<0 || tileY>18)
		{
			System.out.println(tileX+""+ tileY);
			System.out.println(testX+"."+testY);
			return new Pair(-2,-2);
		}
		for(Pair p: avoidDupes)
		{
			if(p.getFirst()==tileX && p.getSecond() ==tileY)
			{
				return new Pair(-6,-6);
			}
		}
		if(Board.getStaticBoard().isObstractle(tileX, tileY))
		{
			return new Pair(-4,-4);
		}
		if(tileIsSafe(tileX, tileY))
		{
			return new Pair(tileX, tileY);
		}
		avoidDupes.add(new Pair(tileX,tileY));
		Pair left=recursiveSearch(tileX-1, tileY);
		Pair up = recursiveSearch(tileX, tileY-1);
		Pair right = recursiveSearch(tileX+1, tileY);
		Pair down = recursiveSearch(tileX, tileY+1);
		return minPair(minPair(left,right),minPair(up,down));
		
	}
	private Pair minPair(Pair p1, Pair p2)
	{
		if(p1.getFirst()<0)
			return p2;
		else if(p2.getFirst()<0)
			return p1;
		else
		{
			if(p1.getCalls()>p2.getCalls())
			{
				return p2;
			}
			else
			{
				return p1;
			}
		}
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
















