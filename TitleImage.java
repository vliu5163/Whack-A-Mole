/*
 * TitleImage.java
 * Vivian Liu
 * Last modified: 5/25/16
 * Imports the a title image for Whack-A-Mole, and paints it.
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TitleImage {

	private int x, y; //x and y coordinates of where the image is drawn
	
	BufferedImage image; //the title image
	
	//TitleImage.java constructor
	public TitleImage (int xIn, int yIn) throws IOException
	{
		x = xIn;
		y = yIn;
		image = ImageIO.read(new File("whack-a-mole-title-image.png")); //the title image
	}
	
	//returns x coordinate of the image
	public int getX()
	{
		return x;
	}
	
	//returns y coordinate of the image
	public int getY()
	{
		return y;
	}
	
	//draw the image using the x and y coordinates
	public void draw(Graphics g)
	{
		g.drawImage(image, x, y, null);
	}
}
