package frontend;

public class Friends {

	private String name;
	private int score;
	private int no_more_sets;
	
	public Friends(String name, int score, int no_more_sets){
		this.name = name;
		this.score = score;
		this.setNo_more_sets(no_more_sets);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

	public int getNo_more_sets() {
		return no_more_sets;
	}

	public void setNo_more_sets(int no_more_sets) {
		this.no_more_sets = no_more_sets;
	}
	

}
