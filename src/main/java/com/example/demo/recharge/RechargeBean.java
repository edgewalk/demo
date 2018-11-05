package com.example.demo.recharge;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by: edgewalk
 * 2018-11-05 12:52
 */
@Data
public class RechargeBean {
	private String gameID;
	private String channel;
	private String uid;
	private String appOrderID;
	private String amount;
	private String extend;
	private String orderID;
	private String sandbox;

	private Extend extendObj;

	public RechargeBean(String gameID, String channel, String uid, String appOrderID, String amount, String extend, String orderID, String sandbox) {
		this.gameID = gameID;
		this.channel = channel;
		this.uid = uid;
		this.appOrderID = appOrderID;
		this.amount = amount;
		this.extend = extend;
		this.orderID = orderID;
		this.sandbox = sandbox;
	}
}
