package com.sample.casino.model;

import java.text.DecimalFormat;

public class AggregationResult {

	private String casinoid;
	
	private String id; 
	
	private int count;
	
	private double sum;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getCasinoid() {
		return casinoid;
	}

	public void setCasinoid(String casinoid) {
		this.casinoid = casinoid;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sentimentSum) {
		this.sum = sentimentSum;
	} 
	
	public double getAverageSentiment() {
		DecimalFormat df = new DecimalFormat("#.#");      
		double value = (this.sum / this.count); 
		value = Double.valueOf(df.format(value));
		return value;
	}
}
