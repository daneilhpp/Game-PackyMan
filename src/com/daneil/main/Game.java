package com.daneil.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;

import com.daneil.entities.Enemy;
import com.daneil.entities.Entity;
import com.daneil.entities.Player;
import com.daneil.graphics.Spritesheet;
import com.daneil.graphics.UI;
import com.daneil.world.World;

	public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener
	{
		private static final long serialVersionUID = 1L;
		
		public static JFrame frame;
		private Thread thread;
		private boolean isRunning = true;
		public static final int WIDTH = 240;
		public static final int HEIGHT = 240;
		public static final int SCALE = 2;
		
		public static int eyeCur = 0;
		public static int eyeCount = 0;
		public static int ghosterCur = 0;
		//public static int shutDownCount = 0;
		
		private BufferedImage image;
		
		public static List<Entity> entities;
		public static List<Enemy> enemies;
		
		public static Spritesheet spritesheet;
		public static World world;
		public static Player player;
		public UI ui;
		
	public Game()
	{
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));

		initFrame();
		
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		player = new Player(0,0 , 16,16 , 2 , spritesheet.getSprite(32,0 , 16,16));
		world = new World("/level1.png");
		ui = new UI();	
		
		
		entities.add(player);
	}
	
	public void initFrame()
	{
		frame = new JFrame("PackyMen");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		
		
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	
	}
	
	public synchronized void start()
	{
		thread = new Thread (this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop()
	{
		isRunning = false;
		try 
		{
			thread.join();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		stop();
	}
	
	public static void main(String [] args)
	{
		Game game = new Game();
		game.start();
	}

	public void tick()
	{
		
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			e.tick();
		}
	}
	
	public void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) 
		{
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		
		g.setColor(new Color(155,0,255));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		world.render(g);
		
		Collections.sort(entities,Entity.nodeSorter);
		
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			e.render(g);
		}
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		ui.render(g);
		
		if(Game.eyeCount == Game.eyeCur)
		{
			g.setColor(Color.lightGray);
			g.setFont(new Font("arial",Font.BOLD,18));
			g.drawString("YOU WON",(WIDTH/2)*SCALE, (HEIGHT/2)*SCALE);
			/*for(int i = 0; i < 5; i++) 
			{
				shutDownCount = shutDownCount++;
				System.out.println(shutDownCount);
				if(shutDownCount == 4)
					System.exit(0);
			}*/
		}
			
		bs.show();
	}
	
	public void run()
	{	
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		@SuppressWarnings("unused")
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >=1)
			{
				requestFocus();
				tick();
				render();
				frames++;
				delta--;
			}
		if (System.currentTimeMillis() - timer >= 1000)
		{
			frames = 0;
			timer+= 1000;
		}
		}
	}

	public void keyTyped(KeyEvent e)
	{
	
	}

	public void keyPressed(KeyEvent e)
	{	
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||  e.getKeyCode() == KeyEvent.VK_D)
		{
			player.right = true;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
		{
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP ||  e.getKeyCode() == KeyEvent.VK_W)
		{
			player.up = true;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
		{
			player.down = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			System.exit(0);
		}
	}

	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
		{
			player.right = false;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
		{
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
		{
			player.up = false;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
		{
			player.down = false;
		}
	}

	public void mouseClicked(MouseEvent e)
	{
		
	}

	public void mousePressed(MouseEvent e)
	{
		
	}
		

	public void mouseReleased(MouseEvent e)
	{
		
	}

	public void mouseEntered(MouseEvent e)
	{
		
	}

	public void mouseExited(MouseEvent e)
	{
		
	}

	
	
	public void mouseDragged(MouseEvent e) 
	{
		
	}

	public void mouseMoved(MouseEvent e)
	{
		
	}
}