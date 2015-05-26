package com.beepcall.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beepcall.dto.query.PokeCallQuery;
import com.beepcall.model.PokeCall;
import com.beepcall.repository.PokeCallRepository;

@Service
public class PokeCallServiceImpl implements PokeCallService {

	@Resource
	private PokeCallRepository pokeCallRepository;

	@Override
	public List<PokeCall> findAll() {

		return pokeCallRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PokeCall> search(Date beginTime, Date endTime) {
		PokeCallQuery query = new PokeCallQuery();
		query.setBeginRequestTime(beginTime);
		query.setEndRequestTime(endTime);
		List<PokeCall> list = pokeCallRepository.search(query);
		return list;
	}
}
