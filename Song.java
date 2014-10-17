public class Song
{
	private Object title;
	private Object artist;
	private Object album;
	private String path;
	//private String artPath;

	public Song(Object t, Object ar, Object al, String pathused)
	{
		title = t;
		artist = ar;
		album = al;
		path = pathused;
		//artPath = artpath;
	}

	public Object getTitle()
	{
		return title;
	}

	public Object getArtist()
	{
		return artist;
	}

	public Object getAlbum()
	{
		return album;
	}

	public String getPath()
	{
		return path;
	}

	/*public String getArtPath()
	{
		return artPath;
	}

	public void setArtwork(String pathToArt)
	{
		artPath = pathToArt;
	}*/
}
