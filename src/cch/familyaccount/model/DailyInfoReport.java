package cch.familyaccount.model;

public class DailyInfoReport {
	private String itemName;
	private int count;
	private double avgFee;
	private double totalFee;
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getAvgFee() {
		return avgFee;
	}
	public void setAvgFee(double avgFee) {
		this.avgFee = avgFee;
	}
	public double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}
}
