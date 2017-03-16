package frontend;

public class GameListing {
	
	private int gid;
	private String gname;
	private int numplayers;
	
	public GameListing(int gidin, String gnamein, int numplayersin) {
		gid = gidin;
		gname = gnamein;
		numplayers = numplayersin;
	}
	
	public int getGid() {
		return gid;
	}
	
	public void setGid(int gidin) {
		gid = gidin;
	}
	
	public String getGname() {
		return gname;
	}

	public void setGname(int gnamein) {
		gname = gnamein;
	}	
	
	public int getNumplayers() {
		return numplayers;
	}
	
	public void setNumplayers(int numplayersin) {
		numplayers = numplayersin;
	}
	
}