package frontend;

class Friends {

	private String name;
	private int score;
	private int no_more_sets;

	Friends(String name, int score, int no_more_sets) {
		this.name = name;
		this.score = score;
		this.setNo_more_sets(no_more_sets);
	}

	String getName() {
		return name;
	}

	int getScore() {
		return score;
	}

	int getNo_more_sets() {
		return no_more_sets;
	}

	private void setNo_more_sets(int no_more_sets) {
		this.no_more_sets = no_more_sets;
	}
	

}
