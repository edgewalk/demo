package com.example.demo.recharge;

import com.example.demo.SqlUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by: edgewalk
 * 2018-11-05 12:02
 */
public class RechargeMain {
	private static String  sqltemplate= "insert ignore into `t_pay` " +
			"(`billno`, `openid`, `itemid`, `region`, `uid`, `channel`, `device`, `money`, `orderid`, `level`, `viplevel`, `status`, `payway`, `devicecode`, `currency`, `rawmoney`) " +
			"values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s');";

	//存放价格表
	private static HashMap<String,GoodCash> goodCashMap = new HashMap<String,GoodCash>();

	private static List<String> existOrderIdList = new ArrayList<String>();

	//存在最后的筛选后的订单表
	private static ArrayList<RechargeBean> rechargeBeans = new ArrayList<>();

	public static void printSql(){
		for (RechargeBean rechargeBean : rechargeBeans) {
			String format = String.format(sqltemplate,
					rechargeBean.getAppOrderID(),
					rechargeBean.getExtendObj().getUid(),
					rechargeBean.getExtendObj().getItemid(),
					rechargeBean.getExtendObj().getRegion(),
					rechargeBean.getExtendObj().getUid(),
					rechargeBean.getChannel(),
					rechargeBean.getExtendObj().getDeviceType(),
					rechargeBean.getAmount(),
					rechargeBean.getOrderID(),
					rechargeBean.getExtendObj().getLevel(),
					rechargeBean.getExtendObj().getVip(),
					"0",
					rechargeBean.getExtendObj().getPayway(),
					rechargeBean.getExtendObj().getDeviceID(),
					"TWD",
					goodCashMap.get(rechargeBean.getExtendObj().getItemid()).getRawCash()
					);
			System.out.println(format);
		}
	}

	public static void printworldCommand(){
		for (RechargeBean rechargeBean : rechargeBeans) {
			String command = String.format("recharge,%s,%s,%s,%s,%s",
					rechargeBean.getExtendObj().getUid(),
					rechargeBean.getExtendObj().getItemid(),
					rechargeBean.getAppOrderID(),
					rechargeBean.getChannel(),
					rechargeBean.getOrderID());
			System.out.println(command);
		}
	}





	public static void main(String[] args) throws IOException {
		//加载价格表
		loadGoodsCash();
		//加载数据库已经存在的订单号
		loadexistOrder();
		InputStream resourceAsStream = RechargeMain.class.getClassLoader().getResourceAsStream("recharge.log");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
		String s;
		while ((s = bufferedReader.readLine()) != null) {
			String[] splits = s.trim().split(", ");
			RechargeBean rechargeBean = new RechargeBean(
					splits[0].trim().split("=")[1],
					splits[1].trim().split("=")[1],
					splits[2].trim().split("=")[1],
					splits[3].trim().split("=")[1].replace("'",""),
					splits[4].trim().split("=")[1],
					splits[5].trim().split("=")[1].replace("'",""),
					splits[6].trim().split("=")[1].replace("'",""),
					splits[7].trim().split("=")[1]
			);
			rechargeBean.setExtendObj(ExtendConverter.convert(rechargeBean.getExtend()));
			checkAmonut(rechargeBean);
			if (!existOrderIdList.contains(rechargeBean.getAppOrderID())){
				rechargeBeans.add(rechargeBean);
			}
		}
		//System.out.println(rechargeBeans.size());
		printSql();
		//printworldCommand();
	}



	public static void  checkAmonut(RechargeBean rechargeBean){
		GoodCash goodCash = goodCashMap.get(rechargeBean.getExtendObj().getItemid());
		//System.out.println(goodCash);
		if (!goodCash.getCash().equals(rechargeBean.getAmount())){
			System.out.println(rechargeBean);
		}
	}

	public static void loadGoodsCash() throws IOException {
		InputStream resourceAsStream = RechargeMain.class.getClassLoader().getResourceAsStream("goodsCash.dat");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
		ArrayList<RechargeBean> rechargeBeans = new ArrayList<>();
		String s;
		while ((s = bufferedReader.readLine()) != null) {
			String[] splits = s.trim().split(",");
			GoodCash goodCash = new GoodCash(splits[0].trim(), splits[1].trim(), splits[2].trim());
			goodCashMap.put(splits[0].trim(),goodCash);
		}
	}

	public static void loadexistOrder() throws IOException {
		InputStream resourceAsStream = RechargeMain.class.getClassLoader().getResourceAsStream("existOrder.dat");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
		ArrayList<RechargeBean> rechargeBeans = new ArrayList<>();
		String s;
		while ((s = bufferedReader.readLine()) != null) {
			existOrderIdList.add(s.trim());
		}
	}
}
