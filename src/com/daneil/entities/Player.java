package com.daneil.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.daneil.main.Game;
import com.daneil.world.World;

public class Player extends Entity
{
	public boolean right,up,left,down;
	public int dir = 0;
	
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;
	private boolean moved = false;

	private BufferedImage[] sprite_right;
	private BufferedImage[] sprite_left;
	private BufferedImage[] sprite_up;
	private BufferedImage[] sprite_down;
	
	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite)
	{
		super(x, y, width, height, speed, sprite);
		
		sprite_right = new BufferedImage[2];
		sprite_left = new BufferedImage[2];
		sprite_up = new BufferedImage[2];
		sprite_down = new BufferedImage[2];

		for(int i = 0; i < 2; i++)
		{
			sprite_right[i] = Game.spritesheet.getSprite(64, 0 + (i*16), 16, 16);
		}
		
		for(int i = 0; i < 2; i++)
		{
			sprite_left[i] = Game.spritesheet.getSprite(48, 0 + (i*16), 16, 16);
		}
		
		for(int i = 0; i < 2; i++)
		{
			sprite_up[i] = Game.spritesheet.getSprite(80, 0 + (i*16), 16, 16);
		}
		
		for(int i = 0; i < 2; i++)
		{
			sprite_down[i] = Game.spritesheet.getSprite(32, 0 + (i*16), 16, 16);
		}
	}
	
	public void verifEye()
	{
		for(int i = 0; i < Game.entities.size(); i++)
		{
			Entity current = Game.entities.get(i);
			if(current instanceof EYE)
			{
				if(Entity.isColliding(this, current))
				{
					Game.eyeCur++;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}
	
	
	/*public void verifGhoster()
	{
		for(int i = 0; i < Game.entities.size(); i++)
		{
			Entity current = Game.entities.get(i);
			if(current instanceof GHOSTER)
			{
				if(Entity.isColliding(this, current))
				{
					Game.ghosterCur++;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}*/
	
	public void tick()
	{
		moved = false;
		depth = 1;
		if(right && World.isFree((int)(x+speed),this.getY()))
		{
			moved = true;
			dir = 1;
			x+=speed;
		}	
		
		else if(left && World.isFree((int)(x-speed),this.getY()))
		{
			moved = true;
			dir = 2;
			x-=speed;
		}
		
		if(up && World.isFree(this.getX(),(int)(y-speed)))
		{
			moved = true;
			dir = 3;
			y-=speed;
		}	
		
		else if(down && World.isFree(this.getX(),(int)(y+speed)	))
		{
			moved = true;
			dir = 4;
			y+=speed;
		}
		
		if(moved)
		{
			frames++;
			if(frames == maxFrames)
			{
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
		}
		verifEye();
		//verifGhoster();
		
		if(Game.eyeCount == Game.eyeCur)
		{
			World.restartGame();
		}
	}
	
	public void render(Graphics g)
	{
		switch(dir)
		{
		case 1:
			g.drawImage(sprite_right[index],this.getX(),this.getY(), null);
			break;
		case 2:
			g.drawImage(sprite_left[index],this.getX(),this.getY(), null);
			break;
		case 3:
			g.drawImage(sprite_up[index],this.getX(),this.getY(), null);
			break;
		case 4:
			g.drawImage(sprite_down[index],this.getX(),this.getY(), null);
			break;
		default:
			g.drawImage(sprite_down[index],this.getX(),this.getY(), null);
		}
	}
}
