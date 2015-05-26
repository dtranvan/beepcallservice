package com.beepcall.service;

import java.util.Date;
import java.util.List;

import com.beepcall.model.PokeCall;

public interface PokeCallService {
	public List<PokeCall> findAll();

	public List<PokeCall> search(Date beginTime, Date endTime);
}
