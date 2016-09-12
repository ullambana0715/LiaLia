package cn.chono.yopper.view;

import java.util.ArrayList;
import java.util.List;

public class Wheel {
	/**
	 * 内容
	 */
	private List<String> texts=new ArrayList<String>();
	
	/**
	 * 焦点文字
	 */
	private String focusText;
	
	private String type;
	
	private int location;
	
	public Wheel(List<String> texts,String wheeltype,int scrollLocation){
		this.texts = texts;
		this.type=wheeltype;
		this.location=scrollLocation;
	}
	
	public List<String> getTexts() {
		return texts;
	}
	
	
	
			
	public String getType() {
		return type;
	}

	
	
	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public int getFocusTextPosition() {
		int position = 0;
		int count = texts.size();
		if(count > 0 ){
			for (int i = 0; i < texts.size(); i++) {
				if(texts.get(i).equals(focusText)) {
					position = i;
				}
			}
			if(position == 0) {
				position = -1;
			}
		}else{
			position = -1;
		}
		
		return position;
	}
}
