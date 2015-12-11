package com.rilintech.model;

public class MedDescription {
	/**
	 * 药名
	 */
	private String name;
	/**
	 * 时间点
	 */
	private String time;
	/**
	 * 服用量
	 */
	private String mensure;
	/**
	 * 药品剂型
	 */
	private String usage;
	/**
	 * 铃声
	 */
	private String ringName;
	/**
	 * 重复（每天/一次）
	 */
	private String interval;
	/**
	 * 服务开启状态
	 */
	private int flag;
	/**
	 * 药的ID
	 */
	private int medd_id;
	/**
	 * 是否重复响铃（0/1）
	 */
	private int repeat;
	/**
	 * 请求码
	 */
	private String requestCode;
	/**
	 * 日期
	 */
	private String date;
	/**
	 * 图片路径
	 */
	private String imageUri;
	
	public String getImageUri() {
		return imageUri;
	}
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRequestCode() {
		return requestCode;
	}
	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}
	public int getRepeat() {
		return repeat;
	}
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	public int getMedd_id() {
		return medd_id;
	}
	public void setMedd_id(int medd_id) {
		this.medd_id = medd_id;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMensure() {
		return mensure;
	}
	public void setMensure(String mensure) {
		this.mensure = mensure;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getRingName() {
		return ringName;
	}
	public void setRingName(String ringName) {
		this.ringName = ringName;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	
	
	
	
	
	
}
