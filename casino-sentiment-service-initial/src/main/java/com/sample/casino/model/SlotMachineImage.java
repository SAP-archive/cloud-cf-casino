package com.sample.casino.model;

public class SlotMachineImage {

	String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	} 	
	
	public String toJson() {
		return "{ \"image_url\" : \"" + imageUrl + "\" }";
	}
}
