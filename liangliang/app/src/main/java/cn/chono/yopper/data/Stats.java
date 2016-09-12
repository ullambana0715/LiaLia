package cn.chono.yopper.data;

import java.io.Serializable;

public class Stats implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private int attraction;
	private int totalRewards;
	private int totalPoints;
	
	
	public int getAttraction() {
		return attraction;
	}
	public void setAttraction(int attraction) {
		this.attraction = attraction;
	}
	public int getTotalRewards() {
		return totalRewards;
	}
	public void setTotalRewards(int totalRewards) {
		this.totalRewards = totalRewards;
	}
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}


}
