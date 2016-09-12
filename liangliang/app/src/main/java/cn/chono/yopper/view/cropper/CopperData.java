package cn.chono.yopper.view.cropper;

import java.io.Serializable;

import android.graphics.Bitmap;

public class CopperData implements Serializable {
	public  String croppedImage;
	public  int ActualCropX;
	public  int ActualCropY;
	public  int ActualCropWidth;
	public  int ActualCropHeight;
	public  boolean isCrop = false;
	public  int getActualCropX() {
		return ActualCropX;
	}
	public  void setActualCropX(int actualCropX) {
		ActualCropX = actualCropX;
	}
	public  int getActualCropY() {
		return ActualCropY;
	}
	public  void setActualCropY(int actualCropY) {
		ActualCropY = actualCropY;
	}
	public  int getActualCropWidth() {
		return ActualCropWidth;
	}
	public  void setActualCropWidth(int actualCropWidth) {
		ActualCropWidth = actualCropWidth;
	}
	public  int getActualCropHeight() {
		return ActualCropHeight;
	}
	public  void setActualCropHeight(int actualCropHeight) {
		ActualCropHeight = actualCropHeight;
	}
	public  boolean isCrop() {
		return isCrop;
	}
	
	public  String getCroppedImage() {
		return croppedImage;
	}
	public void setCroppedImage(String croppedImage) {
		this.croppedImage = croppedImage;
	}
	public void setCrop(boolean isCrop) {
		this.isCrop = isCrop;
	}
	

}
