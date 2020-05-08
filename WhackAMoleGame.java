/*
 WhackAMoleGame.java
 Vivian Liu
 Last modified: 5/26/16
 Plays the carnival game of Whack-a-Mole
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/******************************************************************************

*
* Name: Vivian Liu
* Block: B
* Date: 5/25/16
*
* Program: Whack-A-Mole
* Description:
* This program plays a game of Whack-A-Mole, in which the user tries to 
* click on the randomly appearing moles. I have 6 classes. WhackAMole.java
* serves as my pilot class, Mole.java handles popping up random moles and painting 
* the moles, Mallet.java handles moving and rotating the hammer with the mouse
* movement, TitleImage.java imports the title image and draws it, and AudioPlayer.java
* incorporates sound into my code.
*

******************************************************************************/

public class WhackAMoleGame extends JFrame
	implements ActionListener, MouseListener, MouseMotionListener 
{
	private static final int MAX_WIDTH = 1200;	// Window size
	private static final int MAX_HEIGHT = 850;	//Window size
	private static final int FPS = 30; //frames per second
	private static final int DELAY_IN_MILLISEC = 1000/FPS; //delay is 1 second, used to increment the timer
	private static final int NUM_MOLE_HOLES = 9; //total number of mole holes
	private static final int TITLE_X = 300; //x position of title image
	private static final int TITLE_Y = 20; //y position of title image
	
	private Color lawnBackground = new Color (48, 164, 40);
	
	private Mole [] theMoles; //array of Mole objects
	private Mallet theMallet; //the Mallet
	private TitleImage theTitleImage; //the title image
	
	private int mouseXPos;
	private int mouseYPos;
	private int totalSeconds = 30; //total number of seconds that the user has to play the game
	private int timeElapsed = 0; //total amount of time that has passed since the user started playing
	private int clickNum = 0; //total number of clicks, represents how many times user attempted a hit
	private int hitNum = 0; //total number of hits
	private int framesElapsed = 0; 
	
	private String timerString; //the drawn string that contains the time left
	private String fileName = "whack-mole-sound-effect.wav"; //file name of the sound effect
	
	private boolean playingGame = true;
	
	public static void main(String args[])
	{
		WhackAMoleGame mb = new WhackAMoleGame();
		mb.addMouseListener(mb);
		mb.addMouseMotionListener(mb);
	}
	
	//WhackAMoleGame.java constructor
	public WhackAMoleGame()
	{	
		timerString = "";
		theMoles = new Mole [NUM_MOLE_HOLES];
		
		//creates the first horizontal row of moles/mole holes
		for (int i = 0; i < 3; i++)
		{
			try {
				theMoles[i] = new Mole ((i+1) * 350 - 350, 210);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//creates the second horizontal row of moles/mole holes
		for (int i = 3; i < 6; i++)
		{
			try {
				theMoles[i] = new Mole ((i-2) * 350 - 350, 420);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//creates the third horizontal row of moles/mole holes
		for (int i = 6; i < 9; i++)
		{
			try {
				theMoles[i] = new Mole ((i-5) * 350 - 350, 610);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//creates the mallet
		try {
			theMallet = new Mallet (mouseXPos, mouseYPos);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//creates the title image
		try {
			theTitleImage = new TitleImage (TITLE_X, TITLE_Y);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setSize(MAX_WIDTH, MAX_HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Timer clock = new Timer (DELAY_IN_MILLISEC, this);
	   	clock.start();   	
	}
	
	//paint method, paints background, title image, mole/mole holes, hammer, timerString etc.
	public void paint(Graphics g)
	{
		if (!timerString.equals("0:00")) //this will stop the painting once the timer is done
		{
			g.setColor(lawnBackground);
			g.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT); //make the background
		
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.PLAIN, 40));
			g.drawString(getTimerString(), 425, 250); //draw the timer string
		
			g.setFont(new Font("Arial", Font.PLAIN, 30));;
			g.drawString ("Hits: " + hitNum, 800, 75);
			g.drawString ("Misses: " + (clickNum - hitNum), 800, 125);
			if (clickNum == 0) //if I don't put this condition, I will be dividing by 0, and my percentage will be not a number
			{
				g.drawString("Hit percentage: 0.00%", 800, 175);
			}
			else
			{
				DecimalFormat df = new DecimalFormat("#.00");
				String s = df.format(((double)hitNum/clickNum) * 100);
				g.drawString("Hit percentage: " + s + "%", 800, 175);
			}
			if (playingGame)
			{
				for (int i = 0; i < NUM_MOLE_HOLES; i++)
				{
					theMoles[i].draw(g);
				}
			}
			theTitleImage.draw(g); //paint the titleImage
			theMallet.draw(g); //paint the mallet
		}
		
		else
		{
			g.setColor(lawnBackground);
			g.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.PLAIN, 40));
			g.drawString("Time left: 0:00", 425, 250); //draw the timer string
			g.setFont(new Font("Arial", Font.PLAIN, 75));
			g.drawString("Total moles hit: " + hitNum, 300, 375);
			g.drawString("Total attempted hits: " + clickNum, 225, 500);
			DecimalFormat df = new DecimalFormat("#.00");
			String s = df.format(((double)hitNum/clickNum) * 100);
			if (clickNum == 0)
			{
				s = "0.00";
			}
			g.drawString("Hit percentage: " + s + "%", 225, 625);
			theTitleImage.draw(g); //paint the titleImage
		}
	}

	//plays a sound effect every time the user successfully hits a mole
	public static void playClip(String filename) 
	{
		try
		{
			File audioFile = new File("whack-mole-sound-effect.wav");
			final Clip clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));

			clip.addLineListener(new LineListener()
			{
				@Override
				public void update(LineEvent event)
				{
					if (event.getType() == LineEvent.Type.STOP)
						clip.close();
				}
			});
			
			clip.open(AudioSystem.getAudioInputStream(audioFile));
			clip.start();
		}
		catch (Exception exc)
		{
			exc.printStackTrace(System.out);
		}
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseXPos = e.getX();
		mouseYPos = e.getY();
		theMallet.setCoordinates(mouseXPos, mouseYPos);
	}

	@Override
	//needs to calibrate whether or not the user clicked on the mole hole
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		theMallet.hit();
		clickNum++;	
		for (int i = 0; i < NUM_MOLE_HOLES; i++)
		{
			int leftEdge = theMoles[i].getX(); //the left edge of the mole hole
			int topEdge = theMoles[i].getY(); //the top edge of the mole hole
			int rightEdge = leftEdge + theMoles[i].getImageWidth(); //the right edge of the mole hole
			int bottomEdge = topEdge + theMoles[i].getImageHeight(); //the bottom edge of the mole hole
			if ((mouseXPos < rightEdge && mouseXPos > leftEdge) && (mouseYPos < bottomEdge && mouseYPos > topEdge))
			{
				if (theMoles[i].deactivate())
				{
					hitNum++;
					playClip(fileName);
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	//code for the timer
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		framesElapsed ++;
		timeElapsed = framesElapsed / FPS; //every 1 second, timeElapsed adds 1
		
		for (int i = totalSeconds; i >= 0; i--)
		{
			if ((totalSeconds - timeElapsed) % 60 == 0)
			{
				timerString = (totalSeconds - timeElapsed)/60 + ":" + (totalSeconds - timeElapsed)%60 + "0";
			}
			else if ((totalSeconds - timeElapsed) % 60 >= 10)
			{
				timerString = (totalSeconds - timeElapsed)/60 + ":" + (totalSeconds - timeElapsed)%60;
			}
			else if ((totalSeconds - timeElapsed) % 60 < 10 && (totalSeconds - timeElapsed)%60 > 0)
			{
				timerString = (totalSeconds - timeElapsed)/60 + ":0" + (totalSeconds - timeElapsed)%60;
			}
			else
			{
				timerString = ("0:00");
			}
		}
		repaint();
	}
	
	//returns the timer string
	public String getTimerString()
	{
		return "Time Left: " + timerString;
	}
}
