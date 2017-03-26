package com.kevin;

public class FireSet {
	private final int waitDestruction = 100;
	private int delay;
	private Fire[] set= new Fire[4];
	private Player player;
	public FireSet(Fire f1, Fire f2, Fire f3, Fire f4, Player player)
	{
		//f1 FirePosX, f2 FireNegX, f3 FirePosY, f3 FireNegY
		delay = waitDestruction;
		set[0]=f1;
		set[1]=f2;
		set[2]=f3;
		set[3]=f4;
		this.player=player;
	}
	public Fire getFirePos(int pos)
	{
		return set[pos];
	}
	public void tick()
	{
		if (delay <= 0)
		{
			destroy();
		}
		else
		{
			delay--;
		}
	}

	private void destroy()
	{
		if (delay <= 0)
		{
			delay = waitDestruction;
			player.destroyFire(this);
		}
	}
	
}
