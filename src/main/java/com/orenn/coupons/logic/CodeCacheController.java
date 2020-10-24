package com.orenn.coupons.logic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CodeCacheController {
	
	private Map<String, String> codeMap;
	
	public CodeCacheController() {
		this.codeMap = new HashMap<String, String>();
	}
	
	public void put(String key, String value) {
		this.codeMap.put(key, value);
	}
	
	public Object get(String key) {
		return this.codeMap.get(key);
	}
	
	public void remove(String key) {
		this.codeMap.remove(key);
	}
	
	public boolean isContainsValue(String value) {
		return this.codeMap.containsValue(value);
	}

}
