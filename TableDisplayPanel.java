import java.awt.BorderLayout;
import javax.swing.*;
//import java.awt.*;
//import java.awt.geom.*;
//import java.awt.event.*;
import java.util.*;
import javazoom.jlgui.basicplayer.*;
import java.io.*;
import javax.swing.event.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;


public class TableDisplayPanel extends JPanel
{
	private JTable songdatatable;
	private SongTable mySongTable;

	private GraphicsFrame myParent;
	private PlayControlPanel player;

	private BasicController myControls;
	public File songfile;

	private SAXBuilder builder;
	private PrintWriter out;

	private ArtPanel myArtPanel;

        private String xmlpath;

	public TableDisplayPanel(GraphicsFrame parentFrame, PlayControlPanel playerPanel, String xml, SongTable mytable)
	{
		super();

		player = playerPanel;
		myParent = parentFrame;
                xmlpath = xml;

                
		mySongTable = mytable;

                songdatatable = new JTable(mySongTable);
                songdatatable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                songdatatable.setAutoCreateRowSorter(true); //Determines whether or not table items can be rearranged.  Do not set to true until you fix it!

                songdatatable.getSelectionModel().addListSelectionListener(new TableListener());

		JScrollPane scpane = new JScrollPane(songdatatable);
                this.add(scpane);



		//Now for reading from the XML file.
		builder = new SAXBuilder();
		try
		{
			String exfile = xmlpath;
			Document doc = builder.build(new File(exfile));

			Element root = doc.getRootElement();
			List songlist = root.getChildren();

			for (Object aSong : songlist)
			{
				Element song = (Element)aSong;
				String title = song.getChildText("title");
				//System.out.println("Title is " + title);

				String artist = song.getChildText("artist");
				//System.out.println("Artist is " + artist);

				String album = song.getChildText("album");
				//System.out.println("Album is " + album);

				String path = song.getChildText("path");
				//System.out.println("Path is " + path);

				//String pathToArt = song.getChildText("artPath");
				//System.out.println("pathToArt is " + pathToArt);

				Song tempSong = new Song(title, artist, album, path);
				mySongTable.add(tempSong);

			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}


		BasicPlayer player = new BasicPlayer();
                myControls = (BasicController)player;
		PlayerListener plistener = new PlayerListener();
		player.addBasicPlayerListener(plistener);
	}

	public void updateStoredArt(Song currentSong)
	{
		
	}

	private class TableListener implements ListSelectionListener
	{
		public TableListener()
		{

		}

		public void valueChanged(ListSelectionEvent e)
		{
			if (!e.getValueIsAdjusting())
			{
				File songToBePlayed = mySongTable.getSongFile(songdatatable.convertRowIndexToModel(songdatatable.getSelectedRow()));
				player.stopPlayback();
				player.setSongFileFromLib(songToBePlayed);
				player.startPlayback();

				//myParent.giveCurrentSong(mySongTable.returnSong(songdatatable.convertRowIndexToModel(songdatatable.getSelectedRow())));
			}
		}
	}

	public void addSongfile(File newfile)
	{
		songfile = newfile;
		try
		{
			myControls.open(newfile);
		}
		catch (BasicPlayerException bpe)
		{
			bpe.printStackTrace();
		}
		this.saveLibrary();
	}

        public String getxmlpath()
        {
            return xmlpath;
        }

	public File returnFile(int index)
	{
		return (mySongTable.getSongFile(index));
	}

        public SongTable returnsongtable()
    {
            return mySongTable;
        }

	public void saveLibrary()
    {
		try
		{
			out = new PrintWriter(this.getxmlpath());
		}
		catch(Exception e)
		{
			System.out.println("The file 'lib.xml' could not be accessed.");
		}

		out.println("<?xml version='1.0' encoding='UTF-8'?>");
		out.write("\n");
		out.println("<!-- This XML document contains the library information for AtlasPlayer. -->");
		out.println();
		out.println("<root>");

                ArrayList<Song> songArr = mySongTable.returnSongList();


		for (Song aSong : songArr)
		{

			out.println("	<song>");
			out.println("		<path>" + aSong.getPath() + "</path>");
			//out.println("		<artPath>" + aSong.getArtPath() + "</path>");
			out.println("		<title>" + aSong.getTitle() + "</title>");
			out.println("		<artist>" + aSong.getArtist() + "</artist>");
			out.println("		<album>" + aSong.getAlbum() + "</album>");
			out.println("	</song>");
		}

		out.println();
		out.write("</root>");
		out.close();
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
			System.out.println("IMPORTING"+properties);
			//System.out.println("Total number of frames is " + properties.get("audio.length.frames"));

			Song newsong = new Song(properties.get("title"), properties.get("author"), properties.get("album"), songfile.getPath());

                        for (TableDisplayPanel tdp : myParent.getpanellist())
                        {
                           // if (tdp.returnsongtable().gettableindex() == songdata);
                                Integer currenttab = myParent.gettabbedpane().getSelectedIndex() + 1;
                                System.out.println("This is currenttab: " + currenttab);
                                System.out.println("This is tableindex: " + tdp.mySongTable.gettableindex());
                                if (tdp.mySongTable.gettableindex() == currenttab)
                                {
                                    tdp.mySongTable.add(newsong);
                                }
                        }
			songdatatable.updateUI();
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
			//System.out.println("PROGRESS"+properties); ///////////////////////////////////put back!
			//System.out.println("Current frame is " + properties.get("mp3.frame"));
			try
			{
				//myParent.setCurrentProgress((Long) (properties.get("mp3.frame")));
			}
			catch (NullPointerException npe)
			{
				//System.out.println("Cannot update progress bar");
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
			}
		}

		public void setController(BasicController controller)
		{
			//System.out.println("SET CONTROLLER:"+controller);
		}

	}
}
