/*
 * Mole.java
 * Vivian Liu
 * Last modified: 5/26/16
 * Creates the mole and handles popping out of randomly generated holes
 * I had to combine my former MoleHole class into one class because there was 
 * no way to separate the moles and the mole holes in the images
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
public class Mole {

	private static final int[] moleSpeeds = {5, 10, 15}; //different speeds at which the mole will pop up
	private static final int FPS = 30; //frames per second
	private static final int NUM_MOLES_IMAGES = 6; //total of 6 different mole images
	
	private static BufferedImage[] moleImages = new BufferedImage[NUM_MOLES_IMAGES]; //an array of buffered images
	private static int delay = 0;
	
	private int x; //x coordinate of where the image is drawn
	private int y; //y coordinate of where the image is drawn
	private int current = 0; //for the index of the moleImages array
	private int frames = 0; //keeps track of number of frames
	private int imageWidth; //width of the mole hole
	private int imageHeight; //height of the mole hole
	private int moleSpeed;
	
	private boolean up = true; //to see whether or not the mole is going up or down
	private boolean active = false; //default setting for a mole hole is not active
	
	//Mole.java constructor
	public Mole (int xIn, int yIn) throws IOException
	{
		x = xIn;
		y = yIn;	
		
		//for loop to import the 6 different mole/mole hole images
		for (int i = 0; i < NUM_MOLES_IMAGES; i++)
		{
			moleImages[i] = ImageIO.read(new File("mole-image-" + (i+1) + ".png"));
		}
		imageWidth = moleImages[0].getWidth(); //the image width is the same for all 6 images
		imageHeight = moleImages[0].getHeight(); //the image height is roughly the same for all images
	}
	
	//draw the mole hole
	public void draw (Graphics g)
	{
		g.drawImage(moleImages[current], x, y, null); //draw the mole at the current index
		
		if (active)
		{
			if (frames == FPS/moleSpeed) //creates a small delay in printing the different images
			{
				updateIndex();
				frames = 0;
			}
			frames++;
		}
		else 
		{
			randomActivate();
		}

	}
	
	//method to random activate a mole hole with a mole
	public void randomActivate() 
	{
		// I got the idea to randomly activate from my dad
		if (delay <= 0)
		{
			Random gen = new Random();
			int randomNumber = gen.nextInt(2); //a third of the moles will be active
			if (randomNumber == 0)
			{
				active = true;
				delay = 300;
				int rand = gen.nextInt(2);
				moleSpeed = moleSpeeds[rand]; // pick a random mole speed
			}
		}
		
		if (delay > 0) 
		{
			delay--;
		}
	}
	
	//deactivate the mole hole once the mole has popped up
	public boolean deactivate()
	{
		// I got the idea for this method from my dad too
	   if (active)
	   {
	      active = false;
	      current = 0; // return the index of the image back to 0
	      up = true; // the index will go up
	      return true;
	   } 
	   else 
	   {
	      return false;
	   }
	}
	
	//update the index of the moleImage array after it prints each image
	public void updateIndex()
	{
		if (up == true && current < NUM_MOLES_IMAGES - 1) // if the current index is going up
		{
			current++;
		}
		else 
		{
			if (current > 0)  {
				current --;
			}
			up = false;
			if (current == 0 && !up)
			{
				deactivate();
			}
		}
	}
	
	//return the image width
	public int getImageWidth()
	{
		return imageWidth;
	}
	
	//return the image height
	public int getImageHeight()
	{
		return imageHeight;
	}
	
	//get the x coordinate of where the mole/mole hole is drawn
	public int getX()
	{
		return x;
	}
	
	//get the y coordinate of where the mole/mole hole is drawn
	public int getY()
	{
		return y;
	}
}
