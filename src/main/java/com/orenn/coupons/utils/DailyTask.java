package com.orenn.coupons.utils;

import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.CouponsController;

public class DailyTask extends TimerTask {
	
	@Autowired
	private CouponsController couponsController;
	
	@Override
	public void run() {
		try {
			this.couponsController.deleteExpiredCoupons();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

}
