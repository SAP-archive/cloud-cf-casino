package com.sample.casino.model;

import java.text.DecimalFormat;

/**
 * Bean <code>QueryResult</code> is used to store queries from <code>slotmachines</code> table.
 */
public class QueryResult {

    private String id;
    private double sentiment;
    private String casinoid; 
    private double sum; 
    private int count; 
      
	public String getId() {
		return id;
	}

	public void setId(String slotMachineId) {
		this.id = slotMachineId;
	}

	public String getCasinoid() {
		return casinoid;
	}

	public void setCasinoid(String casinoid) {
		this.casinoid = casinoid;
	}

	public double getSentiment() {
		return sentiment;
	}
	
	public void setSentiment(double sentiment) {
		this.sentiment = sentiment;
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
