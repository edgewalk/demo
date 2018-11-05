package com.example.demo.recharge;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 扩展参数
 * Created by: edgewalk
 * 2018-08-06 18:26
 */
@Data
@AllArgsConstructor
public class Extend {
	/*
	1 	uid:用户uid
	2 	region:区,不分区默认传递1
	3 	itemid:商品id
	4 	gameKey:针对游戏分国家而言,此处不分国家,可以传递国家代码
	5	deviceID:设备号
	6	deviceType:设备类型
	7	level:等级
	8	vip:vip等级
	9	type:1现网,2测试,3审核
	10	bundle:取自g_version,type和bundle的值都为1时,连接测试库
	11	currency:币种
	12	payway:支付方式,googlepay,iospay
	(type ,bundle 取自g_version表默认1,1 )
	*/
	//558551906910330,1,gold_8,01,4ba4d58a2c2ea2072bbc8b76aa7f360c,ios,1,0,3,1,HK,iospay
	private Long uid;
	private int region;
	private String itemid;
	private String gameKey;
	private String deviceID;
	private String deviceType;
	private int level;
	private int vip;
	private int type;
	private String bundle;
	private String currency;
	private String payway;
}
