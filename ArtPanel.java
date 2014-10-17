import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import javazoom.jlgui.basicplayer.*;
import java.io.*;
import javax.swing.event.*;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


import java.awt.*;
import java.awt.image.*;

public class ArtPanel extends JPanel
{
	private int width;
	private int height;

	private BufferedImage theArtwork;

	private File artfile;
	private JButton atlaslogobutton;
	private JLabel atlaslogo;

	private BasicController myControls;
	private File songfile;
	private String currentState;

	public ArtPanel()
	{
		super();

		this.setSize(10,10);
		
		width = 175;
		height = 175;

		artfile = new File("atlas.jpg");
		currentState = "STOP";

		BasicPlayer player = new BasicPlayer();
		myControls = (BasicController)player;
		PlayerListener plistener = new PlayerListener();
		player.addBasicPlayerListener(plistener);

		atlaslogobutton = new JButton(new ImageIcon("atlaslogo.png"));

		this.add(atlaslogobutton);
		atlaslogobutton.addActionListener(new gmansoundListener());

		songfile = new File("gmansound.wav");
		
	}

	public void setArtwork(File artworkPath)
	{
		artfile = artworkPath;
		repaint();
	}

	public File getArtwork()
	{
		return artfile;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		try
		{
			theArtwork = ImageIO.read(artfile);
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}

		g2.drawImage(theArtwork, 24, 200, width, height, null);

	}

	private class gmansoundListener implements ActionListener
	{
		public gmansoundListener()
		{
		}
		public void actionPerformed(ActionEvent ae)
		{
try
			{
				if (currentState=="STOP")
				{
					currentState = "PLAY";
					System.out.println("This is the current state: " + currentState);

					System.out.println("The state is:" + currentState);
					System.out.println("Now play this song:" + songfile);
					myControls.open(songfile);

					myControls.play();
					myControls.setGain(0.85);
					myControls.setPan(0.0);

				}
			}

			catch (BasicPlayerException bpe)
			{
				bpe.printStackTrace();
			}
		}
	}


	public class PlayerListener implements BasicPlayerListener
	{
	    public PlayerListener()
	    {
	    }

		public void opened(Object stream, Map properties)
		{
		}

		public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties)
		{
		}

		public void stateUpdated(BasicPlayerEvent event)
		{
			// Notification of BasicPlayer states (opened, playing, end of media, ...)
			//System.out.println("UPDATE:"+event);

			if (event.getCode()==BasicPlayerEvent.STOPPED)
			{
			    currentState = "STOP";
			}
		}

		public void setController(BasicController controller)
		{
		}
	}



}
