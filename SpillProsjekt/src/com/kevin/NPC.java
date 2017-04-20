package com.kevin;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class NPC extends Player{
	
	private int lookUp;
	private int lookDown;
	private int lookLeft;
	private int lookRight;
	private int lookLength = 4;
	private Stack<Node> path= new Stack<>();
	private double lastVelX = 0;
	private double lastVelY = 0;

	private int decisionCount = 0;
	private int preGameCap = 0;
	private boolean madeDecision = false;
	private int wait = 40; //tiden NPC venter mellom hvert valg
	private int delay, checkDangerDelay, moveDelay; //For � optimalisere koden for maksimal effektivitet og minimal un�dvendig bruk av ressurser
	private List<Pair> avoidDupes;
	private int testX, testY;

	private int randomNumber = 0; //trenger vel muligens en random int et sted her
	
	public NPC(Board board, PlayerType type, int id) 
	{
		super(board, type, id);
		
		maxBombs = 1;
		checkDangerDelay=0;
		lookUp = lowerTileY - lookLength;
		lookDown = upperTileY + lookLength;
		lookLeft = lowerTileX - lookLength;
		lookRight = upperTileX + lookLength;
		delay = wait;
		moveDelay=0;
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

		/*if(!parent.isObstractle(getTileX(),getTileY()))
		{
			validTileX=getTileX();
			validTileY=getTileY();
		}
		else if(!parent.isObstractle(getTileX(), getUpperTileY()))
		{
			validTileX=getTileX();
			validTileY=getUpperTileY();
		}
		else if(!parent.isObstractle(getUpperTileX(), getTileY()))
		{
			validTileX=getUpperTileX();
			validTileY=getTileY();
		}
		else
		{
			validTileX=getUpperTileX();
			validTileY=getTileY();
		}*/
		if(!tileIsSafe(getAvgTileX(), getAvgTileY()) &&checkDangerDelay<=0 && path.isEmpty())
		{
			gotoTile(getClosestSafeTile(getAvgTileX(), getAvgTileY()));
			checkDangerDelay=20;
			if(!path.isEmpty())
				path.pop();
		}
		checkDangerDelay--;
		//stopper før han går inn i flamme
		/*stopBeforeFire(1,0);
		stopBeforeFire(-1,0);
		stopBeforeFire(0,1);
		stopBeforeFire(0,-1);
*/
		if(!path.isEmpty())
		{
			boolean[] flag={false, false};
			if(path.get(path.size()-1).getX() -this.x> this.speed*0.7)
			{
				keysDown[2]=true;
				keysDown[1]=false;
				velX=speed;
			}
			else if(path.get(path.size()-1).getX() -this.x< -this.speed*0.7)
			{
				keysDown[1]=true;
				keysDown[2]=false;
				velX=-speed;
			}
			else
			{
				keysDown[2]=false;
				keysDown[1]=false;
				flag[0]=true;
				velX=0;
			}
			if(path.get(path.size()-1).getY() -this.y> this.speed*0.7)
			{
				keysDown[0]=true;
				keysDown[3]=false;
				velY=speed;
			}
			else if(path.get(path.size()-1).getY() -this.y< -this.speed*0.7)
			{
				keysDown[3]=true;
				keysDown[0]=false;
				velY=-speed;
			}
			else
			{
				keysDown[3]=false;
				keysDown[0]=false;
				flag[1]=true;
				velY=0;
			}
			
			if(flag[0] && flag[1])
			{
				path.pop();
			}
			
		}
		else
		{
			//iSmart();
		}
		/*if(!path.isEmpty()&&moveDelay<=0)
		{
			Node n = path.pop();
			this.x=n.getX();
			this.y=n.getY();
			moveDelay=20;
		}*/
		moveDelay--;
		//starter klokka som tikker ned til neste ikke-real time valg
		//delayBeforeDecision();
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
	private boolean priOne()
	{
		return false;
	}
	private boolean priTwo()
	{
		return false;
	}
	private boolean priThree()
	{
		return false;
	}
	private void iSmart()
	{
		if(priOne())
			return;
		else if(priTwo())
			return;
		else if(priThree())
			return;
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
	//Funksjon som finner en vei til en tile. Ukjent hva den vil gjøre dersom det ikke finnes en vei
	// AKA Throw new IllegalRIPGameException();
	private void gotoTile(Pair p)
	{
		if(p.getFirst()<0)
		{
			return;
		}
		Node dummy = new Node(false, -1, -1);
		List<Node> temp;
		List<List<Node>> nodeMap = new ArrayList<>();
		for(int i =0; i < 19;i++)
		{
			temp= new ArrayList<>();
			for (int j =0; j<19;j++)
			{
				temp.add(new Node(Board.getStaticBoard().isObstractle(i, j), i*36, j*36));
			}
			nodeMap.add(temp);
		}
		for(int i =0; i < 19;i++)
		{
			for (int j =0; j<19;j++)
			{
				//System.out.println(nodeMap.get(i).get(j));
				if(i==0)
				{
					if(j==0)
					{
						nodeMap.get(i).get(j).setRelations(dummy, dummy, nodeMap.get(i+1).get(j), nodeMap.get(i).get(j+1));
					}
					else if(j==18)
					{
						nodeMap.get(i).get(j).setRelations(dummy, nodeMap.get(i).get(j-1), nodeMap.get(i+1).get(j), dummy);
					}
					else
					{
						nodeMap.get(i).get(j).setRelations(dummy, nodeMap.get(i).get(j-1), nodeMap.get(i+1).get(j),  nodeMap.get(i).get(j+1));
					}
				}
				else if(i==18)
				{
					if(j==0)
					{
						nodeMap.get(i).get(j).setRelations(nodeMap.get(i-1).get(j), dummy, dummy, nodeMap.get(i).get(j+1));
					}
					else if(j==18)
					{
						nodeMap.get(i).get(j).setRelations(nodeMap.get(i-1).get(j), nodeMap.get(i).get(j-1), dummy, dummy);
					}
					else
					{
						nodeMap.get(i).get(j).setRelations(nodeMap.get(i-1).get(j), nodeMap.get(i).get(j-1), dummy,  nodeMap.get(i).get(j+1));
					}
				}
				else
				{
					if(j==0)
					{
						nodeMap.get(i).get(j).setRelations(nodeMap.get(i-1).get(j), dummy, nodeMap.get(i+1).get(j), nodeMap.get(i).get(j+1));
					}
					else if(j==18)
					{
						nodeMap.get(i).get(j).setRelations(nodeMap.get(i-1).get(j), nodeMap.get(i).get(j-1), nodeMap.get(i+1).get(j), dummy);
					}
					else
					{
						nodeMap.get(i).get(j).setRelations(nodeMap.get(i-1).get(j), nodeMap.get(i).get(j-1), nodeMap.get(i+1).get(j),  nodeMap.get(i).get(j+1));
					}
				}
			}
			
		}
		nodeMap.get(getAvgTileX()).get(getAvgTileY()).setEnabled(true);
		nodeMap.get(p.getFirst()).get(p.getSecond()).setGoal();
		path=new Stack<>();
		for(int i =0; i < 19*19; i++)
		{
			for(List<Node> nodeList : nodeMap)
			{
				for(Node node:nodeList)
				{
					//System.out.println("AAA"+node);
					if(node.tick())
					{
						walkThePath(node);
						return;
					}
				}
			}
		}
		/*x=p.getFirst()*36;
		y=p.getSecond()*36;*/
		
	}
	private void walkThePath(Node node)
	{
		path.push(node);
		if(!node.terminate())
		{
			walkThePath(node.getParent());
		}
		/*while(x!=node.getX() && y!=node.getY())
		{
			if(x<node.getX())
			{
				x++;
			}
			else if(x!=node.getX())
			{
				x--;
			}
			if(y<node.getY())
			{
				y++;
			}
			else if(y!=node.getY())
			{
				y--;
			}
		}*/
	}
	private Pair recursiveSearch(int tileX, int tileY)
	{
		if(tileX>18 || tileX<0 || tileY<0 || tileY>18)
		{
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
		return minPair(minPair(left,right),minPair(up,down)).addCall();
		
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
				if (parent.isBoulder(getTileX(), getTileY()) || parent.isBox(getTileX(), getTileY()))
				{
					if ((lastVelX==-speed))
					{
						bombs.add(new Bomb("bomb.png", (getTileX()+1)*36+ adjustX, (getTileY())*36 + adjustY, this, 
								getTileX()+1, getTileY()));
					}
					else if (lastVelY==-speed)
					{
						bombs.add(new Bomb("bomb.png", (getTileX())*36+ adjustX, (getTileY()-1)*36 + adjustY, this, 
								getTileX(), getTileY()-1));
					}
				}
				else
				{
					bombs.add(new Bomb("bomb.png", getTileX()*36+ adjustX, getTileY()*36 + adjustY, this, 
							getTileX(), getTileY()));
				}
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
















