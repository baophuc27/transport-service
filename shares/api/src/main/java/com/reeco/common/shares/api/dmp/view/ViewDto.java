package com.reeco.common.shares.api.dmp.view;


public class ViewDto {

	private int viewId;

	private String name;

	private String userId;

	private String serviceAddress;

	public ViewDto(){}

	public ViewDto(int viewId, String name, String userId, String serviceAddress) {
		this.viewId = viewId;
		this.name = name;
		this.userId = userId;
		this.serviceAddress = serviceAddress;
	}

	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
