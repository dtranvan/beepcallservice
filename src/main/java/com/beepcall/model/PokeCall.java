package com.beepcall.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import com.beepcall.utils.Constants;

@Entity
@Table(name = "poke_call")
public class PokeCall {
	@Id
	@Column(name = "TRANS_ID")
	private String transId;

	@Column(name = "CALLER")
	private String caller;

	@Column(name = "MSISDN")
	private String msisdn;

	@Column(name = "CALLED")
	private String called;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DIAL_TIME")
	private Date dialTime;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = Constants.DATETIME_PATTERN)
	@Column(name = "REQUEST_TIME")
	private Date requestTime;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = Constants.DATETIME_PATTERN)
	@Column(name = "MAKE_CALL_TIME")
	private Date makeCallTime;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = Constants.DATETIME_PATTERN)
	@Column(name = "LAST_START_WORK")
	private Date lastStartWork;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = Constants.DATETIME_PATTERN)
	@Column(name = "LAST_FINISH_WORK")
	private Date lastFinishWork;

	@Column(name = "RETRY_TOTAL")
	private Long retryTotal;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = Constants.DATETIME_PATTERN)
	@Column(name = "RING_TIME")
	private Date ringTime;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = Constants.DATETIME_PATTERN)
	@Column(name = "END_TIME")
	private Date endTime;

	@Column(name = "SYSTEM_STATUS")
	private String status;

	@Column(name = "RES")
	private String response;

	@Column(name = "CALL_ID")
	private String callId;

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getCalled() {
		return called;
	}

	public void setCalled(String called) {
		this.called = called;
	}

	public Date getDialTime() {
		return dialTime;
	}

	public void setDialTime(Date dialTime) {
		this.dialTime = dialTime;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getMakeCallTime() {
		return makeCallTime;
	}

	public void setMakeCallTime(Date makeCallTime) {
		this.makeCallTime = makeCallTime;
	}

	public Date getLastStartWork() {
		return lastStartWork;
	}

	public void setLastStartWork(Date lastStartWork) {
		this.lastStartWork = lastStartWork;
	}

	public Date getLastFinishWork() {
		return lastFinishWork;
	}

	public void setLastFinishWork(Date lastFinishWork) {
		this.lastFinishWork = lastFinishWork;
	}

	public Long getRetryTotal() {
		return retryTotal;
	}

	public void setRetryTotal(Long retryTotal) {
		this.retryTotal = retryTotal;
	}

	public Date getRingTime() {
		return ringTime;
	}

	public void setRingTime(Date ringTime) {
		this.ringTime = ringTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
