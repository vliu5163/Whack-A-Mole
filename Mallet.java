/*
 * Mallet.java
 * Vivian Liu
 * Last modified: 5/26/16
 * Imports an image of a hammer, and moves and rotates with the mouse movement and the click, respectively
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Mallet {
	
	private int x; //x coordinate of where the hammer is drawn
	private int y;	//y coordinate where the hammer is drawn
	
	private boolean mousePressed = false; //boolean for whether or not the mouse was pressed, default is not pressed
	
	private int malletDelay = 10; //I have to delay the redrawing of the mallet because by the time 
	
	BufferedImage image; //image of the mallet
	BufferedImage imageRotated; //image of the mallet rotated 90 degrees
	
	//Mallet.java constructor
	public Mallet(int xIn, int yIn) throws IOException
	{
		x = xIn;
		y = yIn;
		image = ImageIO.read(new File("mallet.png"));
		imageRotated = ImageIO.read(new File("mallet-rotated.png"));
	}
	
	//get the x coordinate of where the mallet is drawn
	public int getX()
	{
		return x;
	}
	
	//get the y coordinate of where the mallet is drawn
	public int getY()
	{
		return y;
	}
	
	//set the new coordinates of where the mallet is drawn
	public void setCoordinates(int xIn, int yIn)
	{
		x = xIn;
		y = yIn;
	}
	
	//draw the mallet or the rotated mallet
	public void draw(Graphics g)
	{
		if (!mousePressed)
		{
			g.drawImage(image, x - 50, y - 200, null);
		}
		else //if the user clicks, then draw the rotated mallet
		{
			g.drawImage(imageRotated, x - 75, y - 125, null);
			malletDelay--;
			//code to revert the rotated mallet back to the normal mallet
			if (malletDelay == 0)
			{
				mousePressed = false;
				malletDelay = 2;
			}
		}
	}
	
	//if hit is true, mouse pressed is true
	public void hit()
	{
		mousePressed = true;
	}
}
