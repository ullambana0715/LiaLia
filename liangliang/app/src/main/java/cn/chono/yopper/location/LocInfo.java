package cn.chono.yopper.location;

import android.location.Location;

public class LocInfo {
	
	private Location loc;
	private String info;
	private String District;
	private String Street;
	private String AddrStr;

	private String city;
	private String province;
	
	public Location getLoc() {
		return loc;
	}
	public void setLoc(Location loc) {
		this.loc = loc;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDistrict() {
		return District;
	}
	public void setDistrict(String district) {
		District = district;
	}
	public String getStreet() {
		return Street;
	}
	public void setStreet(String street) {
		Street = street;
	}
	public String getAddrStr() {
		return AddrStr;
	}
	public void setAddrStr(String addrStr) {
		AddrStr = addrStr;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public String toString() {
		return "LocInfo{" +
				"loc=" + loc +
				", info='" + info + '\'' +
				", District='" + District + '\'' +
				", Street='" + Street + '\'' +
				", AddrStr='" + AddrStr + '\'' +
				", city='" + city + '\'' +
				'}';
	}
}
