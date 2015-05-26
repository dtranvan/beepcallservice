package com.beepcall.repository;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

/**
 * Created by: Admin
 */
public abstract class BaseDaoImpl {
	public final String FROM = "FROM";

	@PersistenceContext
	private EntityManager em;

	public Integer getCount(String query, Map<String, Object> params) {
		String[] result = StringUtils.splitByWholeSeparatorPreserveAllTokens(
				query, FROM);
		if (result.length > 2) {
			throw new RuntimeException("Invalid HQL.");
		}
		String newquery = "SELECT count(*) " + FROM + " " + result[1];
		Query hquery = this.em.createQuery(newquery);
		
		setParams(hquery, params);
		
		Number number = (Number) hquery.getSingleResult();
		return number.intValue();
	}
	
	public void setParams(Query query,  Map<String, Object> params) {
		if (!params.isEmpty()) {
			Set<Entry<String, Object>> set = params.entrySet();
			Iterator<Entry<String, Object>> iterator = set.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> obj = iterator.next();
				query.setParameter(obj.getKey(), obj.getValue());
			}
		}
	}

}
