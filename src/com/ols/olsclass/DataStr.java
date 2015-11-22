package com.ols.olsclass;

import java.util.ArrayList;
import java.util.List;

public class DataStr {
	public List<String> numx = new ArrayList<String>();
	public List<String> numy = new ArrayList<String>();
	public List<String> fity = new ArrayList<String>();
	public List<String> deltay = new ArrayList<String>();

	public String xAverage = "", yAverage = "", xAverage2 = "", yAverage2 = "",
			x2Average = "", y2Average = "", xyAverage = "";
	public String a = "", b = "", r = "";
	public DoubleData doubleData;

	public DataStr(Data data)
	{
		System.out.println("DataStr(Data data)");
		setData(data);
	}
	
	public List<String> getNumx() {
		return numx;
	}

	public List<String> getNumy() {
		return numy;
	}

	public List<String> getFity() {
		return fity;
	}

	public List<String> getDeltay() {
		return deltay;
	}

	public void setData(Data data) {
//		System.out.println("setData(Data data)");
		int count = data.getCount();
//		System.out.println("setData(Data data):count="+count);
		for (int i = 0; i < count; i++) {
//			System.out.println("setData(Data data):i="+i);
			doubleData = new DoubleData(data.getNumx().get(i) + "");
			numx.add(doubleData.DataFormat());
			doubleData = new DoubleData(data.getNumy().get(i) + "");
			numy.add(doubleData.DataFormat());
			doubleData = new DoubleData(data.getFity().get(i) + "");
			fity.add(doubleData.DataFormat());
			doubleData = new DoubleData(data.getDeltay().get(i) + "");
			deltay.add(doubleData.DataFormat());
		}
//		System.out.println("setData(Data data):for=finished");
		doubleData = new DoubleData(data.xAverage + "");
		xAverage = doubleData.DataFormat();
//		System.out.println("setData(Data data):xAverage="+xAverage);
		doubleData = new DoubleData(data.yAverage + "");
		yAverage = doubleData.DataFormat();
//		System.out.println("setData(Data data):yAverage="+yAverage);
		doubleData = new DoubleData(data.xAverage2 + "");
		xAverage2 = doubleData.DataFormat();
//		System.out.println("setData(Data data):xAverage2="+xAverage2);
		doubleData = new DoubleData(data.yAverage2 + "");
		yAverage2 = doubleData.DataFormat();
//		System.out.println("setData(Data data):yAverage2="+yAverage2);
		doubleData = new DoubleData(data.x2Average + "");
		x2Average = doubleData.DataFormat();
//		System.out.println("setData(Data data):x2Average="+x2Average);
		doubleData = new DoubleData(data.y2Average + "");
		y2Average = doubleData.DataFormat();
//		System.out.println("setData(Data data):y2Average="+y2Average);
		doubleData = new DoubleData(data.xyAverage + "");
		xyAverage = doubleData.DataFormat();
//		System.out.println("setData(Data data):xyAverage="+xyAverage);
		doubleData = new DoubleData(data.a + "");
		a = doubleData.DataFormat();
//		System.out.println("setData(Data data):a="+a);
		doubleData = new DoubleData(data.b + "");
		b = doubleData.DataFormat();
//		System.out.println("setData(Data data):b="+b);
		doubleData = new DoubleData(data.r + "");
		r = doubleData.DataFormat();
//		System.out.println("setData(Data data):r="+r);
	}
}
