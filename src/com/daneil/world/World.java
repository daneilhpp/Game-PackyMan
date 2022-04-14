package com.daneil.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.daneil.entities.EYE;
import com.daneil.entities.Enemy;
import com.daneil.entities.Entity;
import com.daneil.entities.Player;
import com.daneil.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 16;
	
		public World(String path){
			try {
				BufferedImage map = ImageIO.read(getClass().getResource(path));
				int[] pixels = new int[map.getWidth() * map.getHeight()];
				WIDTH = map.getWidth();
				HEIGHT = map.getHeight();
				tiles = new Tile[map.getWidth() * map.getHeight()];
				map.getRGB(0, 0, map.getWidth(), map.getHeight(),pixels, 0, map.getWidth());
				for(int xx = 0; xx < map.getWidth(); xx++){
					for(int yy = 0; yy < map.getHeight(); yy++){
						int pixelAtual = pixels[xx + (yy * map.getWidth())];
						
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*TILE_SIZE,yy*TILE_SIZE,Tile.TILE_FLOOR);
						
						//Floor
						if(pixelAtual == 0xFF000000)
						{
							tiles[xx + (yy * WIDTH)] = new FloorTile(xx*TILE_SIZE,yy*TILE_SIZE,Tile.TILE_FLOOR);
						}
						//Wall
							else if(pixelAtual == 0xFFFFFFFF)
							{
								tiles[xx + (yy * WIDTH)] = new WallTile(xx*TILE_SIZE,yy*TILE_SIZE,Tile.TILE_WALL_1);
							}
							
						//Player
						else if(pixelAtual == 0xFF4800FF)
						{
							Game.player.setX(xx*16);
							Game.player.setY(yy*16);
						}
						//Enemies
						else if(pixelAtual == 0xFFB75C56)
						{
							Enemy enemy = new Enemy(xx*16,yy*16,16,16,1,Entity.ENEMY1);
							Game.entities.add(enemy);
							
						}
						else if(pixelAtual == 0xFFB78356)
						{
							Enemy enemy = new Enemy(xx*16,yy*16,16,16,1,Entity.ENEMY2);
							Game.entities.add(enemy);
							
						}
						//EYE
						else if(pixelAtual == 0xFFFFD800)
						{
							EYE eye = new EYE(xx*16,yy*16,16,16,0,Entity.EYE_SPRITE);
							Game.entities.add(eye);
							Game.eyeCount++;
						}
						//GHOSTER
						else if(pixelAtual == 0xFFCCAA00)
						{
							EYE eye = new EYE(xx*16,yy*16,16,16,0,Entity.GHOSTER_SPRITE);
							Game.entities.add(eye);
							Game.eyeCount++;
						}
					}
				}
				} catch (IOException e) {
					e.printStackTrace();
					}
			}
		public static boolean isFree(int xnext,int ynext){
			int x1 = xnext / TILE_SIZE;
			int y1 = ynext / TILE_SIZE;
			
			int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
			int y2 = ynext / TILE_SIZE;
			
			int x3 = xnext / TILE_SIZE;
			int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
			
			int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
			int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
			
			return!((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile ||
					 tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile ||
					 tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile ||
					 tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
		}
		
		public static void restartGame(){
			Game.player = new Player(0,0 , 16,16 , 2 , Game.spritesheet.getSprite(32,0 , 16,16));
			Game.entities.clear();
			Game.entities.add(Game.player);
			Game.eyeCur = 0;
			Game.eyeCount = 0;
			Game.world = new World("/level1.png");
			return;
		}
		
		
		public void render(Graphics g) {
			int xstart = Camera.x / TILE_SIZE;
			int ystart = Camera.y / TILE_SIZE;
			int xfinal = xstart + (Game.WIDTH / TILE_SIZE);
			int yfinal = ystart + (Game.HEIGHT / TILE_SIZE) + 1;

			for(int xx = xstart; xx <= xfinal; xx++){
				for(int yy = ystart; yy <= yfinal; yy++){
					if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
						continue;
					Tile tile = tiles[xx + (yy * WIDTH)];
					tile.render(g);
					
				}
			}
		}
}
