package com.beepcall.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beepcall.model.PokeCall;

public interface PokeCallRepository extends JpaRepository<PokeCall, String>, PokeCallRepositoryCustom {
	public List<PokeCall> findByRequestTimeBetween(Date beginTime, Date endTime);
}
