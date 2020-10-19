package com.orenn.coupons.logic;

import java.util.HashMap;
import java.util.Map;

public class CodeCahceController {
	
	private Map<String, Object> codeMap;
	
	public CodeCahceController() {
		this.codeMap = new HashMap<String, Object>();
	}
	
	public void put(String key, Object value) {
		this.codeMap.put(key, value);
	}
	
	public Object get(String key) {
		return this.codeMap.get(key);
	}
	
	public void remove(String key) {
		this.codeMap.remove(key);
	}
	
	public boolean isContainsValue(Object value) {
		return this.codeMap.containsValue(value);
	}

}
