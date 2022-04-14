package com.daneil.entities;

import java.awt.image.BufferedImage;

public class EYE extends Entity
{

	public EYE(double x, double y, int width, int height, double speed, BufferedImage sprite) 
	{
		super(x, y, width, height, speed, sprite);
		depth = 0;
	}

}
