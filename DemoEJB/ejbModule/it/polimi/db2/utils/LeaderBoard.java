package it.polimi.db2.utils;

public class LeaderBoard {
	
	private String  username;
	private Long points;
	public LeaderBoard(String username, Long points) {
		super();
		this.username = username;
		this.points = points;
	}
	
	public LeaderBoard(String username, int points) {
		super();
		this.username = username;
		this.points = (long) points;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}
	
	
	
	
	

}
