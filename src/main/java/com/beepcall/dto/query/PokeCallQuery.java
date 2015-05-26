package com.beepcall.dto.query;

import java.util.Date;

public class PokeCallQuery {

	private Date beginRequestTime;
	private Date endRequestTime;
	private String response;

	public Date getBeginRequestTime() {
		return beginRequestTime;
	}

	public void setBeginRequestTime(Date beginRequestTime) {
		this.beginRequestTime = beginRequestTime;
	}

	public Date getEndRequestTime() {
		return endRequestTime;
	}

	public void setEndRequestTime(Date endRequestTime) {
		this.endRequestTime = endRequestTime;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
