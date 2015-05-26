package com.beepcall.repository;

import java.util.List;

import com.beepcall.dto.query.PokeCallQuery;
import com.beepcall.model.PokeCall;

public interface PokeCallRepositoryCustom {
	public List<PokeCall> search(PokeCallQuery query);
}
