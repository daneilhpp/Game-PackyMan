package com.daneil.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.daneil.main.Game;

public class UI 
{

	public void render(Graphics g)
	{
		g.setColor(Color.lightGray);
		g.setFont(new Font("arial",Font.BOLD,18));
		g.drawString("Points : "+Game.eyeCur+"/"+Game.eyeCount,10, 23);
	}
}
