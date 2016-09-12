package cn.chono.yopper.data;

public class BlockDto {

	private BlockUser user;


	private String blockedTime;


	public BlockUser getUser() {
		return user;
	}


	public void setUser(BlockUser user) {
		this.user = user;
	}


	public String getBlockedTime() {
		return blockedTime;
	}


	public void setBlockedTime(String blockedTime) {
		this.blockedTime = blockedTime;
	}

	
	
}
