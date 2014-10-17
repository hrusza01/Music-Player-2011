import java.util.*;
import javax.swing.table.*;
import java.io.*;

public class SongTable extends AbstractTableModel
{
    private ArrayList<Song> classdata;
    private Integer tableindex;

    public SongTable(Integer index)
    {
        classdata = new ArrayList<Song>();
        tableindex = index;
    }

    public int getRowCount()
    {
        return classdata.size();
    }

    public int getColumnCount()
    {
        return 3;
    }

    public Object getValueAt(int row, int col)
    {
        if (col == 0)
            return classdata.get(row).getTitle();
        else
            if (col == 1)
                return classdata.get(row).getArtist();
            else
                if (col == 2)
                    return classdata.get(row).getAlbum();
                else
                    return 0;

    }

	public String getColumnName(int col)
	{
		String[] cnames = {"TITLE", "ARTIST", "ALBUM"};
		return cnames[col];
	}

	public File getSongFile(int index)
	{
		Song tempSong = classdata.get(index);
		String path = tempSong.getPath();
		return (new File(path));
	}

	public Song returnSong(int index)
	{
		return classdata.get(index);
	}

	public void add(Song newdata)
	{
		classdata.add(newdata);
	}

	public ArrayList returnSongList()
	{
		return classdata;
	}

        public Integer gettableindex()
    {
            return tableindex;
        }


}
