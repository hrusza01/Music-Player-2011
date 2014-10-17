import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import javazoom.jlgui.basicplayer.*;
import java.io.*;
import javax.swing.event.*;


public class PlayControlPanel extends JPanel
{
	private JButton playButton;
	private JButton stopButton;

        private JSlider volumeSlider;
        private int currentvolume;
	private double tempGain;

        private BasicController myControls;
	private File songfile;
	private File tempsong;
	private GraphicsFrame myParent;
	private String currentState;
	private String windowTitle;


        /*private JTable songdatatable;
        private SongTable mySongTable;*/




	public PlayControlPanel(GraphicsFrame parentFrame)
	{
		 super();
                 BasicPlayer player = new BasicPlayer();
                 myControls = (BasicController)player;
                 PlayerListener plistener = new PlayerListener();
                 player.addBasicPlayerListener(plistener);

		 playButton = new JButton(new ImageIcon("continue16.png"));
		 stopButton = new JButton(new ImageIcon("stop-red16.png"));


                 volumeSlider = new JSlider(0,90,85);
                 volumeSlider.setMajorTickSpacing(10);
                 volumeSlider.setMinorTickSpacing(10);
                 volumeSlider.setPaintLabels(false);
                 volumeSlider.setPaintTicks(false);

                 playButton.addActionListener(new playButtonListener());
                 stopButton.addActionListener(new stopButtonListener());
                 volumeSlider.addChangeListener(new volumeSliderListener());

		 this.add(stopButton);
		 this.add(playButton);
                 this.add(volumeSlider);

		 currentState = "STOP";

		 myParent = parentFrame;
                 currentvolume = volumeSlider.getValue();
	}

	private class playButtonListener implements ActionListener
	{
		public playButtonListener()
		{
		}
		public void actionPerformed(ActionEvent ae)
		{
			
			try
			{
				if (currentState=="PLAY")
				{
					myControls.pause();
					currentState = "PAUSE";
					myParent.setTitle(windowTitle + " (Paused)");
                                        myParent.stopcolorchanger();
					playButton.setIcon(new ImageIcon("continue16.png"));
                                        System.out.println("The state is:" + currentState);
				}
				
				else
				{
					if(currentState == "PAUSE")
					{
						myControls.resume();
						
						myParent.setTitle(windowTitle + " (Playing)");
                                                myParent.startcolorchanger();
						playButton.setIcon(new ImageIcon("pause16.png"));

						currentState = "PLAY";
                                                System.out.println("The state is:" + currentState);
					}

					if (currentState == "STOP")
					{
                                                currentState = "PLAY";
                                                System.out.println("This is the current state: " + currentState);

                                                playButton.setIcon(new ImageIcon("pause16.png"));
                                                myParent.setTitle(windowTitle + " (Playing)");
                                                System.out.println("The state is:" + currentState);
						System.out.println("Now play this song:" + songfile);
						myControls.open(songfile);

                                                myControls.play();
                                                myParent.startcolorchanger();
						tempGain = (double) currentvolume;
						tempGain = tempGain/100;
						myControls.setGain(tempGain);
						myControls.setPan(0.0);

					}
				}
			}

			catch (BasicPlayerException bpe)
			{
				bpe.printStackTrace();
			}
		}
	}

	private class stopButtonListener implements ActionListener
	{
		public stopButtonListener()
		{
		}
		public void actionPerformed(ActionEvent ae)
		{
			 try
                          {
				if (currentState == "STOP")
				{
				}

				else
				{
					myControls.stop();  //////////////////////////////This is where it breaks!
					myParent.resetCurrentProgress();
					currentState = "STOP";
				 
					myParent.setTitle(windowTitle + " (Stopped)");

					playButton.setIcon(new ImageIcon("continue16.png"));
                                        myParent.stopcolorchanger();
                                        System.out.println("The state is:" + currentState);
				  }
                          }

                          catch (BasicPlayerException bpe)
                          {
				  bpe.printStackTrace();
                          }
		}

	}

        private class volumeSliderListener implements ChangeListener
	{
		public volumeSliderListener()
		{
		}
		public void stateChanged(ChangeEvent ce)
		{
			try
			{
				currentvolume = volumeSlider.getValue();
				tempGain = (double) currentvolume;
				tempGain = tempGain/100;
				myControls.setGain(tempGain);
			}
			catch (BasicPlayerException bpe)
			{
				bpe.printStackTrace();
			}

		}
	}



	public void setsongfile(File newfile)
	{
		songfile = newfile;
		try
		{
			myControls.stop();
			myParent.resetCurrentProgress();
		}
		catch (BasicPlayerException bpe)
		{
			bpe.printStackTrace();
		}
	}

	public void setSongFileFromLib(File newfile)
	{
		tempsong = newfile;
		/*try
		{
			myControls.stop();
			myParent.resetCurrentProgress();
		}
		catch (BasicPlayerException bpe)
		{
			bpe.printStackTrace();
		}*/
	}

	public void stopPlayback()
	{
		try
		{
			if (currentState=="STOP")
			{
			}

			else
			{
				myControls.stop();  //////////////////////////////This is where it breaks!
				myParent.resetCurrentProgress();
				currentState = "STOP";

				playButton.setIcon(new ImageIcon("continue16.png"));
                                myParent.stopcolorchanger();
			}
		}

		catch (BasicPlayerException bpe)
		{
			bpe.printStackTrace();
		}
	}

	public File getCurrentFile()
	{
		return songfile;
	}

	public void startPlayback()
	{
		try
		{

			if (currentState=="STOP")
			{
				songfile = tempsong;
				myControls.open(songfile);
				myParent.setTitle(windowTitle + " (Playing)");
				playButton.setIcon(new ImageIcon("pause16.png"));

				myControls.play();

				try
				{
					currentvolume = volumeSlider.getValue();
					tempGain = (double) currentvolume;
					tempGain = tempGain/100;
					myControls.setGain(tempGain);
					System.out.println("tempGain is " + tempGain);
                                        myParent.startcolorchanger();
				}
				catch (BasicPlayerException bpe)
				{
					bpe.printStackTrace();
				}


				
				currentState = "PLAY";
			}

			if (currentState=="PLAY")
			{
				myControls.stop();
				currentState = "STOP";
				songfile = tempsong;
				myControls.open(songfile);
				myParent.setTitle(windowTitle + " (Playing)");
				playButton.setIcon(new ImageIcon("pause16.png"));
				myControls.play();
				currentState = "PLAY";
			}



		}
		catch (BasicPlayerException bpe)
		{
			bpe.printStackTrace();
		}
	}


	
	public class PlayerListener implements BasicPlayerListener
	{
	    public PlayerListener()
	    {
	    }

	    /**
		 * Open callback, stream is ready to play.
		 *
		 * properties map includes audio format dependant features such as
		 * bitrate, duration, frequency, channels, number of frames, vbr flag,
		 * id3v2/id3v1 (for MP3 only), comments (for Ogg Vorbis), ...
		 *
		 * @param stream could be File, URL or InputStream
		 * @param properties audio stream properties.
		 */
		public void opened(Object stream, Map properties)
		{
			// Pay attention to properties. It's useful to get duration,
			// bitrate, channels, even tag such as ID3v2.
			System.out.println("OPENED"+properties);
			myParent.setProgressBarMax((Integer) (properties.get("audio.length.frames")));
			//System.out.println("Total number of frames is " + properties.get("audio.length.frames"));
			windowTitle = (String)(properties.get("title"));
			windowTitle = ("Atlas - " + windowTitle);
			//System.out.println("windowTitle is " + windowTitle);

			/*if (importMode)
			{
				Song newsong = new Song(properties.get("title"), properties.get("author"), properties.get("album"));
				mySongTable.add(newsong);
				songdatatable.updateUI();
				importMode = false;
			}*/
		}

		/**
		 * Progress callback while playing.
		 *
		 * This method is called severals time per seconds while playing.
		 * properties map includes audio format features such as
		 * instant bitrate, microseconds position, current frame number, ...
		 *
		 * @param bytesread from encoded stream.
		 * @param microseconds elapsed (<b>reseted after a seek !</b>).
		 * @param pcmdata PCM samples.
		 * @param properties audio stream parameters.
		 */
		public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties)
		{
			// Pay attention to properties. It depends on underlying JavaSound SPI
			// MP3SPI provides mp3.equalizer.
			//System.out.println("PROGRESS"+properties);
			//System.out.println("Current frame is " + properties.get("mp3.frame"));
			try
			{
				myParent.setCurrentProgress((Long) (properties.get("mp3.frame")));
			}
			catch (NullPointerException npe)
			{
				System.out.println("Cannot update progress bar");
			}
		}

		/**
		 * Notification callback for basicplayer events such as opened, eom ...
		 *
		 * @param event
		 */
		public void stateUpdated(BasicPlayerEvent event)
		{
			// Notification of BasicPlayer states (opened, playing, end of media, ...)
			//System.out.println("UPDATE:"+event);

			if (event.getCode()==BasicPlayerEvent.STOPPED)
			{
                            //System.exit(0);
			    //System.out.println("I'm done here");
				myParent.resetCurrentProgress();
				myParent.setTitle(windowTitle + " (Stopped)");
				playButton.setIcon(new ImageIcon("continue16.png"));
			}
		}

		public void setController(BasicController controller)
		{
			//System.out.println("SET CONTROLLER:"+controller);
		}
	}

        /*private class TableListener implements ListSelectionListener
	{
		public TableListener()
		{

		}

		public void valueChanged(ListSelectionEvent e)
		{
			if (!e.getValueIsAdjusting())
			{
				System.out.println(songdatatable.getSelectedRow());

			}
		}
	}*/
}