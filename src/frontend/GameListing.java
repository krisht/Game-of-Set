package frontend;

public class GameListing {
	
	private int gid;
	private String gname;
	private String player1;
	private String player2;
	private String player3;
	private String player4;




	public GameListing(int gidin, String gnamein, String player1in, String player2in, String player3in, String player4in) {
		gid = gidin;
		gname = gnamein;
		player1 = player1in;
		player2 = player2in;
		player3 = player3in;
		player4 = player4in;
	}

	String getPlayer1() {
		return player1;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	String getPlayer2() {
		return player2;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	String getPlayer3() {
		return player3;
	}

	public void setPlayer3(String player3) {
		this.player3 = player3;
	}

	String getPlayer4() {
		return player4;
	}

	public void setPlayer4(String player4) {
		this.player4 = player4;
	}
	
	public int getGid() {
		return gid;
	}
	
	public void setGid(int gidin) {
		gid = gidin;
	}
	
	String getGname() {
		return gname;
	}

    public void setGname(String gnamein) {
        gname = gnamein;
	}
	
}