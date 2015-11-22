package com.ols.view;

import com.ols.olsclass.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class DeviationDialog extends AlertDialog {
	private Data data;
	protected DeviationDialog(Context context, Data data) {
		super(context);
		this.data = data;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public void setView(View view) {
		// TODO Auto-generated method stub
		super.setView(view);
	}
	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		super.setContentView(view);
	}
	public void setProperties(int layoutid)
	{
		Window window = getWindow();
		window.setGravity(Gravity.CENTER_VERTICAL);
		WindowManager.LayoutParams lp = window.getAttributes();
		// …Ë÷√Õ∏√˜∂»
		lp.alpha = 0.8f;
		window.setAttributes(lp);
		
		setContentView(layoutid);
	}
}
