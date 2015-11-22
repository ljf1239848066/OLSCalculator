package com.ols.olsclass;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Data {
	private List<Double> numx = new ArrayList<Double>();
	private List<Double> numy = new ArrayList<Double>();
	private List<Double> fity = new ArrayList<Double>();
	private List<Double> deltay = new ArrayList<Double>();

	public double xAverage = 0, yAverage = 0, xAverage2 = 0, yAverage2 = 0,
			x2Average = 0, y2Average = 0, xyAverage = 0;
	public double a = 0, b = 0, r = 0;

	/**
	 * 计算时可能出现r为非数字的情况 isFalse=true:正常 isFalse=false:异常
	 */
	boolean isFalse = true;
	boolean isCalculate=false;
	private int count;

	public Data() {
		super();
	}

	public int getCount() {
		count = numx.size();
		return count;
	}

	public boolean getFalse() {
		return isFalse;
	}
	
	public boolean getIsCalculate() {
		return isCalculate;
	}

	public List<Double> getNumx() {
		return numx;
	}

	public List<Double> getNumy() {
		return numy;
	}

	public List<Double> getFity() {
		return fity;
	}

	public List<Double> getDeltay() {
		return deltay;
	}

	public void addData(double x, double y) {
//		Log.d("addData", "1");
		numx.add(x);
		numy.add(y);
		count++;
//		Log.d("addData", "2");
		isCalculate=false;
	}
	public void editData(double x,double y,int location)
	{
		Log.d("editData", "1");
		numx.set(location, x);
		numy.set(location, y);
		Log.d("editData", "2");
		isCalculate=false;
	}
	public boolean deleteData(int location) {
		if (location > getCount() || location < 0) {
			return false;
		} else {
			numx.remove(location);
			numy.remove(location);
			fity.clear();
			deltay.clear();
			count--;
			isCalculate=false;
			return true;
		}
	}
	
	public boolean deleteAllData()
	{
		try
		{
			numx.clear();
			numy.clear();
			fity.clear();
			deltay.clear();
			isCalculate=false;
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	public void Calculate() {
		Log.d("Calculate", "1");
		new CalculateThread().start();
	}
	public class CalculateThread extends Thread
	{
		public CalculateThread()
		{
		}

		@Override
		public void run() {
			double sumx = 0, sumy = 0, sumxy = 0, sumx2 = 0, sumy2 = 0, sum2x = 0, sum2y = 0;
			sumx = 0;
			sumy = 0;
			sumxy = 0;
			sumx2 = 0;
			sumy2 = 0;
			sum2x = 0;
			sum2y = 0;
			a = 0;
			b = 0;
			r = 0;
			Log.d("Calculate", "1");
			double x = 0, y = 0;
			for (int i = 0; i < count; i++) {
				x = numx.get(i);
				y = numy.get(i);
				sumx += x;
				sumy += y;
				sumxy += x * y;
				sumx2 += x * x;
				sumy2 += y * y;
			}
			Log.d("Calculate", "2");
			sum2x = sumx * sumx;
			sum2y = sumy * sumy;
			a = (count * sumxy - sumx * sumy) / (count * sumx2 - sum2x);
			b = (sumy - a * sumx) / count;
			r = (count * sumxy - sumx * sumy)
					/ Math.sqrt((count * sumx2 - sum2x) * (count * sumy2 - sum2y));
			Log.d("Calculate", "3");
			if (!Double.isNaN(r)) {
				Log.d("Calculate", "4");
				isFalse = true;
				xAverage = sumx / count;
				yAverage = sumy / count;
				xAverage2 = sum2x / (count * count);
				yAverage2 = sum2y / (count * count);
				x2Average = sumx2 / count;
				y2Average = sumy2 / count;
				xyAverage = sumxy / count;
				Log.d("Calculate", "5");
				fity.clear();
				deltay.clear();
				double fy = 0, dy = 0;
				Log.d("Calculate", "6");
				Log.d("Calculate", "count="+count);
				for (int i = 0; i < count; i++) {
					x = numx.get(i);
					y = numy.get(i);
					fy = a * x + b;
					dy = Math.abs(fy - y);
					fity.add(fy);
					deltay.add(dy);
				}
				Log.d("Calculate", "7");
				isCalculate=true;
			} else
				isFalse = false;
			super.run();
		}
	}
}
