package com.beepcall.utils;

import java.util.List;
/**
 * @author DungTV
 * http://www.quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/crontrigger
 */
public class Utils {

	public static String createExp(String intervalTime) {	
		return "0 0/" + intervalTime + " * * * ?";
	}
	
	
	public static String createExp() {		
		return "0 0 12 ? * SAT";
	}
	

	public static <E> boolean isEmpty(List<E> list) {
		return (list == null || list.size() == 0);
	}
}
