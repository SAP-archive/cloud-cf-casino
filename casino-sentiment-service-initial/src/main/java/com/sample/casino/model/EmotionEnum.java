package com.sample.casino.model;

public enum EmotionEnum {
	
	disgust(0.0), 
	anger(12.5), 
	fear(25.0),
	sadness(37.5),
	neutral(62.5),
	surprise(75.0), 
	contempt(87.5), 
	happiness(100.0); 
	
	double value;
	
	private EmotionEnum(double value) {
		this.value = value; 
	}

	public double getValue() {
		return this.value;
	}
	
	public static EmotionEnum get(Emotion emotion) {
		return EmotionEnum.valueOf(emotion.getEmotion());
	}
}
