package com.ols.view;

import com.ols.olsclass.Data;
import com.ols.olsclass.DataStr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ResultView  extends View{
	
	private Paint namePaint,resultPaint,linePaint,squarePaint,equationPaint;
	private Data data;
	private DataStr dataStr;
	public ResultView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		namePaint=new Paint();
		namePaint.setColor(Color.WHITE);
		namePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		namePaint.setTextSize(20);
		
		resultPaint=new Paint();
		resultPaint.setColor(Color.WHITE);
		resultPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		resultPaint.setTextSize(20);	
		
		linePaint=new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		linePaint.setStrokeWidth(1);//设置线条粗细
		
		squarePaint=new Paint();
		squarePaint.setColor(Color.WHITE);
		squarePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		squarePaint.setTextSize(12);
		
		equationPaint=new Paint();
		equationPaint.setColor(Color.WHITE);
		equationPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		equationPaint.setTextSize(18);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(data==null)
		{
			return;
		}
		//xArerage
		canvas.drawText("x", 12, 18, namePaint);
		canvas.drawLine(11, 2, 25, 2, linePaint);
		canvas.drawText("=", 35, 20, namePaint);
		canvas.drawText(dataStr.xAverage, 55, 20, resultPaint);
		//yArerage
		canvas.drawText("y", 12, 43, namePaint);
		canvas.drawLine(11, 27, 25, 27, linePaint);
		canvas.drawText("=", 35, 45, namePaint);
		canvas.drawText(dataStr.yAverage, 55, 45, resultPaint);
		//xArerage2
		canvas.drawText("x", 10, 67, namePaint);
		canvas.drawLine(10, 52, 24, 52, linePaint);
		canvas.drawText("=", 35, 70, namePaint);
		canvas.drawText("2", 25, 58, squarePaint);
		canvas.drawText(dataStr.xAverage2, 55, 70, resultPaint);
		//yArerage2
		canvas.drawText("y", 10, 92, namePaint);
		canvas.drawLine(10, 77, 24, 77, linePaint);
		canvas.drawText("=", 35, 95, namePaint);
		canvas.drawText("2", 25, 83, squarePaint);
		canvas.drawText(dataStr.yAverage2, 55, 95, resultPaint);
		//x2Arerage
		canvas.drawText("x", 10, 120, namePaint);
		canvas.drawLine(10, 104, 30, 104, linePaint);
		canvas.drawText("=", 35, 120, namePaint);
		canvas.drawText("2", 23, 118, squarePaint);
		canvas.drawText(dataStr.x2Average, 55, 120, resultPaint);
		//y2Arerage
		canvas.drawText("y", 10, 145, namePaint);
		canvas.drawLine(10, 129, 30, 129, linePaint);
		canvas.drawText("=", 35, 145, namePaint);
		canvas.drawText("2", 23, 143, squarePaint);
		canvas.drawText(dataStr.y2Average, 55, 145, resultPaint);
		//xyArerage
		canvas.drawText("xy", 9, 170, namePaint);
		canvas.drawLine(8, 155, 34, 155, linePaint);
		canvas.drawText("=", 35, 170, namePaint);
		canvas.drawText(dataStr.xyAverage, 55, 170, resultPaint);
		//r
		canvas.drawText("r", 14, 195, namePaint);
		canvas.drawText("=", 35, 195, namePaint);
		canvas.drawText(dataStr.r, 55, 195, resultPaint);
		//a
		canvas.drawText("a", 14, 220, namePaint);
		canvas.drawText("=", 35, 220, namePaint);
		canvas.drawText(dataStr.a, 55, 220, resultPaint);
		//b
		canvas.drawText("b", 14, 245, namePaint);
		canvas.drawText("=", 35, 245, namePaint);
		canvas.drawText(dataStr.b, 55, 245, resultPaint);
		//线性方程
		canvas.drawText("线性方程:", 9, 265, equationPaint);
		//方程
		String lineStr="y="+dataStr.a+"*x";
		double datab=data.b;
		if(datab<0)lineStr+=""+dataStr.b;
		else lineStr+="+"+dataStr.b;
		if(lineStr.length()>30){
			canvas.drawText((String) lineStr.subSequence(0, lineStr.indexOf("x")+1), 9, 290, resultPaint);
			canvas.drawText((String) lineStr.subSequence(lineStr.indexOf("x")+1, lineStr.length()), 70, 315, resultPaint);
		}
		else canvas.drawText(lineStr, 9, 290, resultPaint);
		super.onDraw(canvas);
	}

	public void setResult(Data data)
	{
		this.data=data;
		dataStr=new DataStr(data);
		this.invalidate();
	}
}
