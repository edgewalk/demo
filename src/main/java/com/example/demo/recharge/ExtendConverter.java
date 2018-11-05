package com.example.demo.recharge;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by: edgewalk
 * 2018-08-06 18:29
 */
@Slf4j
public class ExtendConverter {

	public static Extend convert(String extStr) {
		if (extStr == null || extStr.isEmpty() || extStr.length() < 12) {
			log.error("【extend参数不正确】扩展参数extend不正确,extend = {}", extStr);
		}
		Extend extend=null;
		try {
			String[] exts = extStr.split(",");
			extend = new Extend(
					Long.parseLong(exts[0].trim()),
					Integer.parseInt(exts[1].trim()),
					exts[2],
					exts[3],
					exts[4],
					exts[5],
					Integer.parseInt(exts[6].trim()),
					Integer.parseInt(exts[7].trim()),
					Integer.parseInt(exts[8].trim()),
					exts[9],
					exts[10],
					exts[11]
			);

		} catch (Exception e) {
			log.error("【对象转换错误】，extStr = {},e={}", extStr, e);
		}
		return extend;
	}
}
