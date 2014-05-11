package cch.familyaccount.model;

import java.util.Date;

public class DailyInfo {
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public Date getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}
	public Date getFillDate() {
		return fillDate;
	}
	public void setFillDate(Date fillDate) {
		this.fillDate = fillDate;
	}
	public String getPCInfo() {
		return PCInfo;
	}
	public void setPCInfo(String pCInfo) {
		PCInfo = pCInfo;
	}
	public String getIsPaid() {
		return isPaid;
	}
	public void setIsPaid(String isPaid) {
		this.isPaid = isPaid;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	private String personName;
	private String itemName;
	private double fee;
	private Date feeDate;
	private Date fillDate;
	private String PCInfo;
	private String isPaid;
	private Date payDate;
	private String itemType;
	private String comment;
	
}
