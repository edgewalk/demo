package com.example.demo.recharge;

import lombok.Data;

/**
 * Created by: edgewalk
 * 2018-11-05 12:44
 */
@Data
public class tPayBean {

	private String billno;
	private String openid;
	private String itemid;
	private int region;
	private int uid;
	private int channel;
	private String device;
	private int money;
	private String orderid;
	private int level;
	private int viplevel;
	private int status;
	private String payway;
	private String devicecode;
	private String currency;
	private int rawmoney;
	private int amountgift;
}
