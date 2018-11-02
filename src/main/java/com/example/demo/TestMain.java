package com.example.demo;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by: edgewalk
 * 2018-09-21 15:52
 */
@Slf4j
public class TestMain {
	//@Before(KPISearchConditionInterceptor.class)
	@SuppressWarnings("all")
	public void searchKPI() {
		final String lineCode = getPara("lineCode");
		final String lineName = getPara("lineName");
		String orgId = getPara("orgId");
		String orgName = getPara("orgName");
		String startDate = getPara("startDate");
		String endDate = getPara("endDate");

		final ConcurrentHashMap<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("linecd", lineCode);

		final StringBuffer trStr = new StringBuffer();
		final String[] orgIdArr = orgId.split(",");
		final String[] orgNameArr = orgName.split(",");
		int totalThread = orgIdArr.length;
		if (totalThread < 1)
			return;

		ExecutorService threadPool = Executors.newFixedThreadPool(totalThread);
		final CountDownLatch downLatch = new CountDownLatch(totalThread);
		try {
			for (int i = 0; i < totalThread; i++) {    // 遍历机构
				final int j = i;
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						paramMap.put("orgId", orgIdArr[j]);
						// 查询 人数
						String empSql = SqlUtil.getSql("kpi-sql.getEmp", paramMap);
						List<Record> emp = Db.find(empSql);
						int startEmp = emp.get(0).getBigDecimal("startemp").intValue();
						int endEmp = emp.get(0).getBigDecimal("endemp").intValue();
						int avgEmp = (startEmp + endEmp) / 2;
						// 获取总学时
						double onlineHours = 0;
						double faceHours = 0;
						double moveHours = 0;
						String totalHoursSql = SqlUtil.getSql("kpi-sql.getTotalHoursByType", paramMap);
						List<Record> totalHours = Db.find(totalHoursSql);
						for (Record record : totalHours) {
							switch (record.getStr("type")) {
								case "网络":
								case "6":
								case "9":
									onlineHours += record.getBigDecimal("hours").doubleValue();
									break;
								case "面授":
								case "1":
								case "8":
								case "5":
									faceHours += record.getBigDecimal("hours").doubleValue();
									break;
								case "移动":
								case "znexam":
								case "znlive":
									moveHours += record.getBigDecimal("hours").doubleValue();
									break;
							}
						}
						// 平均学时
						double allavgHours = avgEmp == 0 ? 0 : (double) Math.round(((onlineHours + faceHours + moveHours) / avgEmp) * 100) / 100;
						double faceavgHours = avgEmp == 0 ? 0 : (double) Math.round((faceHours / avgEmp) * 100) / 100;
						double onlineavgHours = avgEmp == 0 ? 0 : (double) Math.round((onlineHours / avgEmp) * 100) / 100;
						double moveavgHours = avgEmp == 0 ? 0 : (double) Math.round((moveHours / avgEmp) * 100) / 100;
						synchronized (this) {
							trStr.append("<tr>");
							trStr.append("<td>" + orgNameArr[j] + "</td>");
							if (StringUtil.isNotEmptyOrNull(lineName) && StringUtil.isNotEmptyOrNull(lineCode)) {
								trStr.append("<td>" + lineName + "</td>");
							}
							trStr.append("<td>" + avgEmp + "</td>");
							trStr.append("<td>" + startEmp + "</td>");
							trStr.append("<td>" + endEmp + "</td>");
							trStr.append("<td>" + allavgHours + "</td>");
							trStr.append("<td>" + faceavgHours + "</td>");
							trStr.append("<td>" + onlineavgHours + "</td>");
							trStr.append("<td>" + moveavgHours + "</td>");
							trStr.append("<td>" + faceHours + "</td>");
							trStr.append("<td>" + onlineHours + "</td>");
							trStr.append("<td>" + moveHours + "</td>");
							trStr.append("</tr>");
						}
						downLatch.countDown();
					}
				});
			}
		} catch (Exception e) {
			log.error("KPI调用多线程查询出问题了.......");
			e.printStackTrace();
		} finally {
			threadPool.shutdown();
		}
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.error("KPI调用多线程查询出问题了.......");
			e.printStackTrace();
		}
		setAttr("trStr", trStr);
		setAttrAndRender(lineCode, lineName, orgId, orgName, startDate, endDate);
	}


	private void setAttrAndRender(String lineCode, String lineName, String orgId, String orgName, String startDate, String endDate) {
	}

	private void setAttr(String trStr, StringBuffer trStr1) {
	}


	private String getPara(String lineCode) {
		return null;
	}
}
