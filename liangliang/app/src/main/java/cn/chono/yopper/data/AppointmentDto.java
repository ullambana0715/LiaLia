package cn.chono.yopper.data;

import cn.chono.yopper.Service.Http.DatingPublish.Dine;
import cn.chono.yopper.Service.Http.DatingPublish.Marriage;
import cn.chono.yopper.Service.Http.DatingPublish.Movie;
import cn.chono.yopper.Service.Http.DatingPublish.Other;
import cn.chono.yopper.Service.Http.DatingPublish.Singing;
import cn.chono.yopper.Service.Http.DatingPublish.Sports;
import cn.chono.yopper.Service.Http.DatingPublish.Travel;
import cn.chono.yopper.Service.Http.DatingPublish.WalkTheDog;

public class AppointmentDto {

	private String datingId;

	private AppointOwner owner;

	private double distance;

	private String createTime;

	private int datingStatus;

	private int joinCount;

	private int  activityType;

	private Marriage marriage;

	private Travel travel;

	private Dine dine;

	private Movie movie;

	private Sports sports;

	private WalkTheDog walkTheDog;

	private Singing singing;

	private Other other;

	public AppointOwner getOwner() {
		return owner;
	}

	public void setOwner(AppointOwner owner) {
		this.owner = owner;
	}

	public String getDatingId() {
		return datingId;
	}

	public void setDatingId(String datingId) {
		this.datingId = datingId;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getDatingStatus() {
		return datingStatus;
	}

	public void setDatingStatus(int datingStatus) {
		this.datingStatus = datingStatus;
	}

	public int getJoinCount() {
		return joinCount;
	}

	public void setJoinCount(int joinCount) {
		this.joinCount = joinCount;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public Marriage getMarriage() {
		return marriage;
	}

	public void setMarriage(Marriage marriage) {
		this.marriage = marriage;
	}

	public Travel getTravel() {
		return travel;
	}

	public void setTravel(Travel travel) {
		this.travel = travel;
	}

	public Dine getDine() {
		return dine;
	}

	public void setDine(Dine dine) {
		this.dine = dine;
	}

	public Sports getSports() {
		return sports;
	}

	public void setSports(Sports sports) {
		this.sports = sports;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public WalkTheDog getWalkTheDog() {
		return walkTheDog;
	}

	public void setWalkTheDog(WalkTheDog walkTheDog) {
		this.walkTheDog = walkTheDog;
	}

	public Singing getSinging() {
		return singing;
	}

	public void setSinging(Singing singing) {
		this.singing = singing;
	}

	public Other getOther() {
		return other;
	}

	public void setOther(Other other) {
		this.other = other;
	}

}
