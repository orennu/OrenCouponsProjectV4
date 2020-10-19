package com.orenn.coupons.logic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TokenCacheController {
	
	private Map<String, Object> tokenMap;
	
	public TokenCacheController() {
		this.tokenMap = new HashMap<String, Object>();
	}
	
	public void put(String key, Object value) {
		this.tokenMap.put(key, value);
	}
	
	public Object get(String key) {
		return this.tokenMap.get(key);
	}
	
	public void remove(String key) {
		this.tokenMap.remove(key);
	}
	
}
