import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GraphicsFrame extends JFrame
{
	private PlayControlPanel playController;
        private JTabbedPane tabbedPane;
        private ArtPanel myArtPanel;
	private JMenuBar menubar;
	private JMenu fileMenu;
	private JMenuItem open;
	private JMenuItem importButton;
        private JMenuItem backgroundButton;
        private JRadioButtonMenuItem colorchangerButton;
        private JMenuItem libraryButton;
	private JMenuItem exit;
        private JProgressBar musicProgress;

	private JMenu editMenu;
	private JMenuItem chooseArtwork;

        private ArrayList<Color> colorlist;
        private ArrayList<TableDisplayPanel> panelList;
        private Timer mytimer;
        private Integer colorindex;

        private Integer xmltracker;
        private Integer finalfound;
        private PrintWriter out2;
        private ArrayList<SongTable> tablelist;
        private Boolean colorchangervalue;

	private PrintWriter artSaver;
	private Scanner artLoader;



	public GraphicsFrame()
	{
		super();



		this.setSize(700,500);
		this.setTitle("Atlas");

		playController = new PlayControlPanel(this);
		this.add(playController, BorderLayout.NORTH);

                tabbedPane = new JTabbedPane();
                this.add(tabbedPane, BorderLayout.EAST);

                panelList = new ArrayList<TableDisplayPanel>();
                xmltracker = 1;
                while (xmltracker < 100)
                {
                    File searchforfile = new File(xmltracker + "lib.xml");
                    if(searchforfile.exists())
                    {
                         TableDisplayPanel libraryDisplay = new TableDisplayPanel(this, playController, xmltracker + "lib.xml", new SongTable(xmltracker));
                         libraryDisplay.setVisible(true);
                         panelList.add(libraryDisplay);
                         tabbedPane.addTab("Library" + xmltracker, libraryDisplay);
                         xmltracker = xmltracker + 1;
                    }

                    if (!searchforfile.exists())
                    {
                    finalfound = xmltracker;
                    xmltracker = 1000;
                    }
               }

		
                myArtPanel = new ArtPanel();
                this.add(myArtPanel, BorderLayout.CENTER);
		try
		{
			artLoader = new Scanner(new FileReader("art.config"));

			myArtPanel.setArtwork(new File(artLoader.nextLine()));
		}
		catch(IOException e)
		{
			System.out.println("Oh no, the reader broke!");
		}


		musicProgress = new JProgressBar();
                this.add(musicProgress, BorderLayout.SOUTH);

		menubar = new JMenuBar();
		this.setJMenuBar(menubar);

		
		fileMenu = new JMenu("File");
		menubar.add(fileMenu);

		editMenu = new JMenu("Edit");
		menubar.add(editMenu);


		open = new JMenuItem("Open song...");
		open.addActionListener(new openButtonListener());
		fileMenu.add(open);

		chooseArtwork = new JMenuItem("Change the current display picture...");
		chooseArtwork.addActionListener(new chooseArtworkListener());
		editMenu.add(chooseArtwork);

		importButton = new JMenuItem("Import song...");
		importButton.addActionListener(new importButtonListener());
		fileMenu.add(importButton);

                backgroundButton = new JMenuItem("Change Background Color...");
                backgroundButton.addActionListener(new backgroundButtonListener());
                editMenu.add(backgroundButton);

                libraryButton = new JMenuItem("Add new library...");
                libraryButton.addActionListener(new libraryButtonListener());
                fileMenu.add(libraryButton);

                colorchangerButton = new JRadioButtonMenuItem("Toggle visualizer");
                colorchangerButton.setSelected(false);
                colorchangerButton.addActionListener(new colorchangerButtonListener());
                editMenu.add(colorchangerButton);

		fileMenu.addSeparator();


		exit = new JMenuItem("Exit");
		exit.addActionListener(new exitButtonListener());
		fileMenu.add(exit);

                colorlist = new ArrayList<Color>();
                colorlist.add(Color.BLACK);
                colorlist.add(Color.BLUE);
                colorlist.add(Color.CYAN);
                colorlist.add(Color.DARK_GRAY);
                colorlist.add(Color.GRAY);
                colorlist.add(Color.GREEN);
                colorlist.add(Color.LIGHT_GRAY);
                colorlist.add(Color.MAGENTA);
                colorlist.add(Color.ORANGE);
                colorlist.add(Color.PINK);
                colorlist.add(Color.RED);
                colorlist.add(Color.WHITE);
                colorlist.add(Color.YELLOW);

                mytimer = new Timer(100, new MyTimerListener());

                colorindex = 0;
                colorchangervalue = false;
    }


	private class openButtonListener implements ActionListener
	{
		public openButtonListener()
		{
		}
		public void actionPerformed(ActionEvent ae)
		{
			JFileChooser chooser = new JFileChooser();
                        int returnVal = chooser.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				playController.setsongfile(chooser.getSelectedFile());
			}
		}
	}

	private class importButtonListener implements ActionListener
	{
		public importButtonListener()
		{
		}
		public void actionPerformed(ActionEvent ae)
		{
			JFileChooser importChooser = new JFileChooser();
			importChooser.setDialogTitle("Import");
                        int returnVal = importChooser.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION)
			{
                                    panelList.get(tabbedPane.getSelectedIndex()).addSongfile(importChooser.getSelectedFile());
			}

                        for (TableDisplayPanel tdp : panelList)
                        {
                            for (Object s : tdp.returnsongtable().returnSongList())
                            {
                               Song ts = (Song)s;
                            }
                        }
		}
	}

        private class backgroundButtonListener implements ActionListener
	{
		public backgroundButtonListener()
		{
		}
		public void actionPerformed(ActionEvent ae)
		{
			Color backgroundcolor = JColorChooser.showDialog(null, "Set Background Color", Color.BLACK);
			myArtPanel.setBackground(backgroundcolor);
			tabbedPane.setBackground(backgroundcolor);
			playController.setBackground(backgroundcolor);

		}
	}


         private class libraryButtonListener implements ActionListener
	 {
		public libraryButtonListener()
		{
		}
		public void actionPerformed(ActionEvent ae)
		{
                    File newxml = new File(finalfound + "lib.xml");

                    try
                    {
                        newxml.createNewFile();

                            try
                            {
                                out2 = new PrintWriter(newxml);
                            }

                            catch(Exception e)
                            {
                             System.out.println("The file 'lib.xml' could not be accessed.");
                            }

                          out2.println("<?xml version='1.0' encoding='UTF-8'?>");
                          out2.write("\n");
                          out2.println("<!-- This XML document contains the library information for AtlasPlayer. -->");
                          out2.println();
                          out2.println("<root>");
                          out2.println();
                          out2.write("</root>");
                          out2.close();
                    }


                    catch (IOException ioe)
                    {
                        System.out.println("Something went wrong!");
                    }

                    
                    TableDisplayPanel libraryDisplay = new TableDisplayPanel(new GraphicsFrame(), playController,finalfound + "lib.xml", new SongTable(finalfound));
                    libraryDisplay.setVisible(true);
                    panelList.add(libraryDisplay);
                    Integer insertvar = finalfound - 1;
                    tabbedPane.insertTab("Library" + finalfound, null, libraryDisplay, null, insertvar);
                    finalfound = finalfound + 1;
		}
	}

        private class colorchangerButtonListener implements ActionListener
	{
		public colorchangerButtonListener()
		{
		}
		public void actionPerformed(ActionEvent ae)
		{
                        if (colorchangervalue == true)
                        {
                            colorchangerButton.setSelected(false);
                            colorchangervalue = false;
                        }
                        else
                        {
                            colorchangerButton.setSelected(true);
                            colorchangervalue = true;
                        }

                }
	}

	private class exitButtonListener implements ActionListener
	{
		public exitButtonListener()
		{
		}
		public void actionPerformed(ActionEvent ae)
		{

                    for (TableDisplayPanel tdp : panelList)
                    {
			tdp.saveLibrary();
		    }
                    System.exit(0);
            }
	}

	private class chooseArtworkListener implements ActionListener
	{
		public chooseArtworkListener()
		{
		}
		public void actionPerformed(ActionEvent ae)
		{
			JFileChooser artChooser = new JFileChooser();
			artChooser.setDialogTitle("Choose song art...");
                        int returnVal = artChooser.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				try
				{
					artSaver = new PrintWriter("art.config");
				}
				catch(IOException e)
				{
					System.out.println("Oh no, art.config broke!");
				}
				File artToSet;
				artToSet = (artChooser.getSelectedFile());
				myArtPanel.setArtwork(artToSet);
				artSaver.println(artToSet.getPath());
				artSaver.close();
			}
		}
	}

        public void setProgressBarMax(Integer max)
	{
		musicProgress.setMaximum((int) (max));
	}

	public void setCurrentProgress(Long progressValue)
	{
		musicProgress.setValue(progressValue.intValue());
	}

        public ArrayList<TableDisplayPanel> getpanellist()
    {
            return panelList;
        }

	public void resetCurrentProgress()
	{
		musicProgress.setValue(0);
	}

        public void startcolorchanger()
        {
            if (colorchangervalue == true)
            {
            mytimer.start();
            }
            else
            {
            }

        }

        public void stopcolorchanger()
        {
            if (colorchangervalue == true)
            {
            mytimer.stop();
            playController.setBackground(Color.WHITE);
            tabbedPane.setBackground(Color.WHITE);
            myArtPanel.setBackground(Color.WHITE);
            repaint();
            }
            else
            {
            }
        }

        public JTabbedPane gettabbedpane()
        {
            return tabbedPane;
        }

        private class MyTimerListener implements ActionListener
	{

		public MyTimerListener()
		{
		}

		public void actionPerformed(ActionEvent ae)
		{

		playController.setBackground(colorlist.get(colorindex));
                tabbedPane.setBackground(colorlist.get(colorindex));
                myArtPanel.setBackground(colorlist.get(colorindex));
                repaint();

                    if (colorindex != 12)
                    {
                         colorindex = colorindex + 1;
                    }

                    else
                    {
                        colorindex = 0;
                    }
                }
        }
}
