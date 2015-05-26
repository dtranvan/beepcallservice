package com.beepcall.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.beepcall.dto.query.PokeCallQuery;
import com.beepcall.model.PokeCall;

public class PokeCallRepositoryImpl extends BaseDaoImpl implements PokeCallRepositoryCustom {
	
	private static final String RES_SUCCESS = "180";
	private static final long RETRY_TOTAL = 11;

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<PokeCall> search(PokeCallQuery pokeCallQuery) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append(" SELECT distinct p FROM PokeCall p WHERE 1=1 ");
		
		if (pokeCallQuery.getBeginRequestTime() != null) {
			selectQuery.append(" AND p.requestTime >= :begin");
			params.put("begin", pokeCallQuery.getBeginRequestTime());
		}
		if (pokeCallQuery.getEndRequestTime() != null) {
			selectQuery.append(" AND p.requestTime <= :end");
			params.put("end", pokeCallQuery.getEndRequestTime());
		}	
		selectQuery.append(" AND (p.response = :response OR p.retryTotal >= :retryTotal)");
		params.put("response", RES_SUCCESS);
		params.put("retryTotal", RETRY_TOTAL);

		Query query = this.entityManager.createQuery(selectQuery.toString());
		
		setParams(query, params);
		
		List<PokeCall> result = query.getResultList();
		
		return result;
	}

}
