package com.ols.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ols.olsclass.CalculateAsyncTask;
import com.ols.olsclass.Data;
import com.ols.olsclass.DeviationAdapter;
import com.ols.view.DataView;
import com.ols.view.HorizontalListView.OnScrollListener;
import com.ols.view.ResultView;
import com.ols.view.HorizontalListView;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class OLSCActivity extends Activity implements OnScrollListener {

	Button btnAdd;
	EditText etNumx, etNumy, etNum;
	Button btnEquation, btnDeviation;
	Button inputMsg;
	DataView dataListView;
	HorizontalListView deviationListView;
	View mView;

	RelativeLayout inputLayout;
	TextView tvNodata;
	Boolean isInputShow = false;
	TranslateAnimation animationUp;
	TranslateAnimation animationDown;

	SimpleAdapter listAdapter;
	ArrayList<HashMap<String, Object>> list;

	int numPoint = 0, numMinus = 0, numZero = 0;
	/**
	 * addError=true:输入框数据输入不规范 addError=false:输入框数据输入规范
	 */
	boolean addError = false;
	/**
	 * AddOrAlter=true:添加数据状态 AddOrAlter=false:修改数据状态
	 */
	boolean AddOrEdit = true;
	private int location;
	private int formerlocation;
	Data data = new Data();
	double [][]randomData=new double[2][100];

	TextView dialogTV;
	Button btnEnsure;

	private long exitTime;

	int screenW,screenH;
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {

			} else if (msg.arg1 == 2) {

			} else if (msg.arg1 == 4) {
				if (msg.what == 1) {
					dataListView.getChildAt(msg.arg2).setBackgroundResource(
							R.drawable.itemback);
				}
			} else if (msg.arg1 == 5) {
				inputMsg.setVisibility(View.INVISIBLE);
			} else if (msg.arg1 == 6) {
				inputLayout.setEnabled(false);
				dataListView.bringToFront();
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		screenW  = getWindowManager().getDefaultDisplay().getWidth();       // 屏幕宽（像素，如：480px）  
		screenH = getWindowManager().getDefaultDisplay().getHeight();      // 屏幕高（像素，如：800p）
		
		new Thread()
		{
			@Override
			public void run() {
				Random ran=new Random();
				for(int i=0;i<100;i++)
				{
					randomData[0][i]=(ran.nextInt()-0.5)*5000;
					randomData[1][i]=(ran.nextInt()-0.5)*5000;
				}
				super.run();
			}
		}.start();
		btnAdd = (Button) findViewById(R.id.btn_adddata);
		etNumx = (EditText) findViewById(R.id.et_numx);
		etNumy = (EditText) findViewById(R.id.et_numy);
		etNum = etNumx;
		btnEquation = (Button) findViewById(R.id.btn_equation);
		btnDeviation = (Button) findViewById(R.id.btn_deviation);
		inputMsg = (Button) findViewById(R.id.btnmsg);
		inputLayout = (RelativeLayout) findViewById(R.id.inputlayout);
		tvNodata = (TextView) findViewById(R.id.tvnodata);
		initAnimation();
		btnEquation.setEnabled(false);
		btnDeviation.setEnabled(false);
		dataListView = (DataView) findViewById(R.id.lv_data);
		btnAdd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (etNumx.getText().toString().length() < 1
						|| etNumy.getText().toString().length() < 1) {

				} else {
					try {
						double x = 0, y = 0;
						x = Double.parseDouble(etNumx.getText().toString());
						y = Double.parseDouble(etNumy.getText().toString());
						if (AddOrEdit)
							location = data.getCount();
						addData(x, y, location);
						etNumx.setText("");
						etNumy.setText("");
					} catch (Exception e) {
						System.out.print(e.toString());
						Toast.makeText(getBaseContext(), "添加异常", 2000).show();
					}
				}
			}
		});
		btnAdd.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				Message message = new Message();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (AddOrEdit)
						btnAdd.setBackgroundResource(R.drawable.add_down);
					else
						btnAdd.setBackgroundResource(R.drawable.edit_down);
					break;
				case MotionEvent.ACTION_UP:
					if (AddOrEdit)
						btnAdd.setBackgroundResource(R.drawable.add);
					else
						btnAdd.setBackgroundResource(R.drawable.edit);
					break;
				}
				return false;
			}
		});
		btnAdd.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!addError) {
					if (hasFocus && AddOrEdit) {
						btnAdd.setBackgroundResource(R.drawable.add_focused);
					} else if (hasFocus && !AddOrEdit) {
						btnAdd.setBackgroundResource(R.drawable.edit_focused);
					} else if (!hasFocus && AddOrEdit) {
						btnAdd.setBackgroundResource(R.drawable.add);
					} else if (!hasFocus && !AddOrEdit) {
						btnAdd.setBackgroundResource(R.drawable.edit);
					}
				}
			}
		});
		etNumx.setKeyListener(new NumberKeyListener() {

			public int getInputType() {
				// 0无键盘 1英文键盘 2模拟键盘 3数字键盘
				return 3;
			}

			@Override
			protected char[] getAcceptedChars() {
				char[] c = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
						'-', '.' };
				return c;
			}
		});
		etNumy.setKeyListener(new NumberKeyListener() {

			public int getInputType() {
				// 0无键盘 1英文键盘 2模拟键盘 3数字键盘
				return 3;
			}

			@Override
			protected char[] getAcceptedChars() {
				char[] c = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
						'-', '.' };
				return c;
			}
		});
		etNumx.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				checkInput(s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				String numxStr = etNumx.getText().toString();
				if (etNumx.hasFocus()) {
					if (numxStr.length() < 1)
						etNumx.setBackgroundResource(R.drawable.datax_focused);
					else
						etNumx.setBackgroundResource(R.drawable.data_focused);
				} else {
					if (numxStr.length() < 1)
						etNumx.setBackgroundResource(R.drawable.datax);
					else
						etNumx.setBackgroundResource(R.drawable.data);
				}
			}
		});
		etNumy.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				checkInput(s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				String numyStr = etNumy.getText().toString();
				if (etNumy.hasFocus()) {
					if (numyStr.length() < 1)
						etNumy.setBackgroundResource(R.drawable.datay_focused);
					else
						etNumy.setBackgroundResource(R.drawable.data_focused);
				} else {
					if (numyStr.length() < 1)
						etNumy.setBackgroundResource(R.drawable.datay);
					else
						etNumy.setBackgroundResource(R.drawable.data);
				}
			}
		});
		etNumx.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isInputShow) {
					changeInputState(1);
				}
			}
		});
		etNumy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isInputShow) {
					changeInputState(1);
				}
			}
		});
		etNumx.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					Log.d("onFocusChange", "etNumx:true");
					etNum = etNumx;
					if (etNumx.getText().toString().length() < 1) {
						etNumx.setBackgroundResource(R.drawable.datax_focused);
					} else {
						etNumx.setBackgroundResource(R.drawable.data_focused);
					}
					if (!isInputShow) {
						changeInputState(1);
					}
				} else {
					Log.d("onFocusChange", "etNumx:false");
					if (etNumx.getText().toString().length() < 1) {
						etNumx.setBackgroundResource(R.drawable.datax);
					} else {
						etNumx.setBackgroundResource(R.drawable.data);
					}
					if (!etNumy.hasFocus()) {
						changeInputState(0);
					}
				}
			}
		});
		etNumy.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					Log.d("onFocusChange", "etNumy:true");
					etNum = etNumy;
					if (etNumy.getText().toString().length() < 1) {
						etNumy.setBackgroundResource(R.drawable.datay_focused);
					} else {
						etNumy.setBackgroundResource(R.drawable.data_focused);
					}
					if (!isInputShow) {
						changeInputState(1);
					}
				} else {
					Log.d("onFocusChange", "etNumy:false");
					if (etNumy.getText().toString().length() < 1) {
						etNumy.setBackgroundResource(R.drawable.datay);
					} else {
						etNumy.setBackgroundResource(R.drawable.data);
					}
					if (!etNumx.hasFocus()) {
						changeInputState(0);
					}
				}
			}
		});
		etNumx.setInputType(InputType.TYPE_NULL);
		etNumy.setInputType(InputType.TYPE_NULL);
		dataListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int position, long id) {
				location = position;
				final View nowView = (View) view.findViewById(R.id.ib_edit);
				Toast.makeText(getBaseContext(), "location=" + location, 2000)
						.show();
				OnClickListener listener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							OnItemEditClick(nowView);
							// editData(location);
						} else if (which == 1) {
							deleteData(location);
						}
					}
				};
				dataListView.getChildAt(location).setBackgroundResource(
						R.drawable.item_frame);
				Message message = new Message();
				message.arg1 = 4;
				message.arg2 = location;
				message.what = 1;
				mHandler.sendMessageDelayed(message, 500);
				String[] Menu = { "编辑", "删除" };
				if (!AddOrEdit && location == formerlocation)
					Menu[0] = "取消编辑";
				new AlertDialog.Builder(OLSCActivity.this).setItems(Menu,
						listener).show();
				return false;
			}
		});
		btnEquation.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				showResultDialog();
			}
		});
		btnDeviation.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDeviationDialog();
			}
		});
		btnEquation.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					btnEquation.setBackgroundResource(R.drawable.left_down);
					break;
				case MotionEvent.ACTION_UP:
					btnEquation.setBackgroundResource(R.drawable.left);
					break;
				}
				return false;
			}
		});
		btnDeviation.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					btnDeviation.setBackgroundResource(R.drawable.right_down);
					break;
				case MotionEvent.ACTION_UP:
					btnDeviation.setBackgroundResource(R.drawable.right);
					break;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
    	menu.add(0, 1, 0, "添加测试数据");
    	menu.add(0, 2, 0, "清空全部数据");
    	menu.add(0, 3, 0, "关于");
    	menu.add(0, 4, 0, "退出");
		return true;
	}
	
	//定制各菜单响应
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    		case 1: addTestData(); return true;
    		case 2: deleteAllData();return true;
    		case 3: AboutDialog();return true;
    		case 4: this.finish();
    		return true;
    		default:return false;
    	}
    }
    
    public void addTestData()
    {
    	Random ran=new Random();
		for(int i=0;i<100;i++)
		{
			data.addData(randomData[0][i], randomData[0][1]);
		}
		initdataListView();
		btnEquation.setEnabled(true);
		btnDeviation.setEnabled(true);
		btnEquation.setBackgroundResource(R.drawable.left);
		btnDeviation.setBackgroundResource(R.drawable.right);
    }
    
    public void deleteAllData()
    {
    	data.deleteAllData();
		initdataListView();
		btnEquation.setEnabled(false);
		btnDeviation.setEnabled(false);
		btnEquation.setBackgroundResource(R.drawable.left_no);
		btnDeviation.setBackgroundResource(R.drawable.right_no);
    }
    
    public void AboutDialog()
    {
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
    	builder.setIcon(R.drawable.icon)
  	   .setTitle("关于")
  	   .setMessage("最小二乘法计算器\nOLSCalculator\n\nwww.lxzh123.com\n© 2012 龙心之火\n\nAll rights reserved");
    	AlertDialog alert=builder.create();
    	alert.show();	
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isInputShow) {
				changeInputState(0);
			} else {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					this.finish();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("onTouchEvent", "touchY=" + event.getRawY());
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.d("onCreate", "14");
			int touchY = (int) event.getRawY();
			Log.d("onCreate", "15");
			if (isInputShow
					&& (touchY > inputLayout.getTop() + inputLayout.getHeight() || touchY < inputLayout
							.getTop())) {
				Log.d("onCreate", "16");
				changeInputState(0);
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	public void delInput(View v) {
		Log.d("delInput", "ID" + v.getId());
		int viewId = v.getId();
		String btnText = ((Button) findViewById(viewId)).getText().toString();
		if (viewId == R.id.btnclear) {
			Log.d("delInput", "1");
			etNum.setText("");
		} else if (viewId == R.id.btnfinish) {
			Log.d("delInput", "2");
			changeInputState(0);
		} else {
			if (isInputShow) {
				Log.d("delInput", "3");
				int cursorStart = etNum.getSelectionStart();
				int cursorEnd = etNum.getSelectionEnd();
				String text = etNum.getText().toString();
				if (viewId == R.id.btnback) {
					if (cursorStart != cursorEnd) {
						Log.d("delInput", "31");
						etNum.setText(text.substring(0, cursorStart)
								+ text.substring(cursorEnd));
						etNum.setSelection(cursorStart, cursorStart);
					} else {
						Log.d("delInput", "32");
						if (cursorStart != 0) {
							Log.d("delInput", "321");
							etNum.setText(text.subSequence(0, cursorStart - 1)
									+ text.substring(cursorStart, text.length()));
							etNum.setSelection(cursorStart - 1, cursorStart - 1);
						}
					}
				} else {
					inputMsg.setVisibility(View.VISIBLE);
					inputMsg.setText(((Button) findViewById(v.getId()))
							.getText());
					if (cursorStart != cursorEnd) {
						etNum.setText(text.subSequence(0, cursorStart)
								+ btnText
								+ text.subSequence(cursorEnd, text.length()));
					} else {
						StringBuffer content = new StringBuffer(text);
						content.insert(cursorStart, btnText);
						etNum.setText(content);
						etNum.setSelection(cursorStart + 1, cursorStart + 1);
					}
					Message message = new Message();
					message.arg1 = 5;
					message.what = v.getId();
					mHandler.sendMessageDelayed(message, 300);
				}
			}
		}
	}

	public void changeInputState(int state) {
		if (state == 1) {
			inputLayout.setEnabled(true);
			inputLayout.setVisibility(View.VISIBLE);
			inputMsg.bringToFront();
			inputLayout.bringToFront();
			inputLayout.startAnimation(animationUp);
			isInputShow = true;
		} else {
			inputLayout.startAnimation(animationDown);
			inputLayout.setVisibility(View.INVISIBLE);
			Message message = new Message();
			message.arg1 = 6;
			message.what = 2;
			mHandler.sendMessageDelayed(message, 600);
			isInputShow = false;
		}
	}

	public void OnItemEditClick(View v) {
		RelativeLayout layout = (RelativeLayout) v.getParent();
		if (AddOrEdit) {
			String number = ((TextView) layout.findViewById(R.id.tv_num))
					.getText().toString();
			location = Integer.parseInt(number.trim()) - 1;
			editData(location);
			Log.d("setBackgroundResource", "1");
			((ImageButton) layout.findViewById(R.id.ib_edit))
					.setBackgroundResource(R.drawable.edit_cancle);
		} else {
			String number = ((TextView) layout.findViewById(R.id.tv_num))
					.getText().toString();
			location = Integer.parseInt(number.trim()) - 1;
			if (formerlocation != location) {
				RelativeLayout mLayout = (RelativeLayout) mView;
				editData(location);
				Log.d("setBackgroundResource", "2");
				((ImageButton) mLayout.findViewById(R.id.ib_edit))
						.setBackgroundResource(R.drawable.edit);
				((ImageButton) layout.findViewById(R.id.ib_edit))
						.setBackgroundResource(R.drawable.edit_cancle);
			} else {
				AddOrEdit = true;
				etNumx.setText("");
				etNumy.setText("");
				btnAdd.setBackgroundResource(R.drawable.add);
				Log.d("setBackgroundResource", "3");
				((ImageButton) layout.findViewById(R.id.ib_edit))
						.setBackgroundResource(R.drawable.edit);
			}
		}
		formerlocation = location;
		mView = layout;
	}

	public void checkInput(String text) {
		checkInputText();
		if (addError) {
			if (AddOrEdit) {
				btnAdd.setEnabled(false);
				btnAdd.setBackgroundResource(R.drawable.add_error);
			} else {
				btnAdd.setEnabled(false);
				btnAdd.setBackgroundResource(R.drawable.add_error);
			}
		} else {
			if (AddOrEdit) {
				btnAdd.setEnabled(true);
				btnAdd.setBackgroundResource(R.drawable.add);
			} else {
				btnAdd.setEnabled(true);
				btnAdd.setBackgroundResource(R.drawable.edit);
			}
		}
	}

	public void checkInputText() {
		EditText[] etNumxy = { etNumx, etNumy };
		for (EditText editText : etNumxy) {
			numPoint = 0;
			numMinus = 0;
			String text = editText.getText().toString();
			int length = text.length();
			if (length < 1)
				continue;
			if (text.indexOf("-") > 0 && text.indexOf("-") < length - 1) {
				addError = true;
				return;
			}
			for (int i = 0; i < length; i++) {
				if (text.charAt(i) == '.') {
					numPoint++;
				} else if (text.charAt(i) == '-') {
					numMinus++;
				}
			}
			if (numPoint > 1 || numMinus > 1) {
				addError = true;
				return;
			} else if (numPoint == 0 && numMinus == 1 && text.indexOf("-") > 0) {
				addError = true;
				return;
			} else if (length >= 2 && text.indexOf("-") > text.indexOf(".")
					&& text.indexOf(".") >= 0) {
				addError = true;
				return;
			}
		}
		addError = false;
	}

	public void addData(double x, double y, int location) {
		Log.d("addData", "location=" + location);
		if (AddOrEdit) {
			data.addData(x, y);
			Toast.makeText(getBaseContext(), "添加成功", 2000).show();
			tvNodata.setVisibility(View.INVISIBLE);
			if (data.getCount() > 1) {
				btnEquation.setEnabled(true);
				btnEquation.setBackgroundResource(R.drawable.left);
			}
		} else {
			data.editData(x, y, location);
			Toast.makeText(getBaseContext(), "修改成功", 2000).show();
			btnAdd.setBackgroundResource(R.drawable.add);
			AddOrEdit = true;
		}
		initdataListView();
	}

	public void deleteData(int location) {
		Boolean delete = data.deleteData(location);
		if (delete) {
			Toast.makeText(getBaseContext(), "删除成功", 2000).show();
			if (data.getCount() < 2) {
				btnEquation.setEnabled(false);
				btnDeviation.setEnabled(false);
				btnEquation.setBackgroundResource(R.drawable.left_no);
				btnDeviation.setBackgroundResource(R.drawable.right_no);
			}
			if (data.getCount() < 1) {
				list.clear();
				listAdapter.notifyDataSetChanged();
				tvNodata.setVisibility(View.VISIBLE);
			} else {
				dataListView.setFocusable(true);
				initdataListView();
			}
		} else {
			Toast.makeText(getBaseContext(), "删除失败", 2000).show();
		}
		if (!AddOrEdit) {
			AddOrEdit = true;
			etNumx.setText("");
			etNumy.setText("");
			btnAdd.setBackgroundResource(R.drawable.add);
			location = -1;
		}
	}

	public void editData(int location) {
		Log.d("editData", "1");
		AddOrEdit = false;
		etNumx.setText(data.getNumx().get(location).toString());
		etNumy.setText(data.getNumy().get(location).toString());
		btnAdd.setBackgroundResource(R.drawable.edit);
		Log.d("editData", "2");
	}

	public void initdataListView() {
		list = new ArrayList<HashMap<String, Object>>();
		int count = data.getCount();
		HashMap<String, Object> map;
		for (int i = 0; i < count; i++) {
			map = new HashMap<String, Object>();
			map.put("num", (i + 1) + "");
			map.put("x", data.getNumx().get(i));
			map.put("y", data.getNumy().get(i));
			list.add(map);
		}
		if (count > 0) {
			listAdapter = new SimpleAdapter(this, list, R.layout.datalist,
					new String[] { "num", "x", "y" }, new int[] { R.id.tv_num,
							R.id.tv_numx, R.id.tv_numy });
			dataListView.setAdapter(listAdapter);
			dataListView.setVisibility(View.VISIBLE);
		}
		else
		{
			dataListView.setEmptyView(dataListView.getEmptyView());
			dataListView.setVisibility(View.INVISIBLE);
		}
	}

	public void showResultDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		final AlertDialog resultDialog = builder.create();
		resultDialog.show();

		Window window = resultDialog.getWindow();
		window.setGravity(Gravity.CENTER_VERTICAL);
		WindowManager.LayoutParams lp = window.getAttributes();
		// 设置透明度
		lp.alpha = 0.8f;
		window.setAttributes(lp);

		resultDialog.setContentView(R.layout.dialog);
		Log.d("showResultDialog", "1");
		ProgressBar progressView = (ProgressBar) resultDialog
				.findViewById(R.id.progressview);
		ResultView resultView = (ResultView) resultDialog
				.findViewById(R.id.resultview);
		Log.d("showResultDialog", "2");
		CalculateAsyncTask calculateAsyncTask = new CalculateAsyncTask(
				progressView, resultView, data);
		Log.d("showResultDialog", "3");
		calculateAsyncTask.execute(0);
		Log.d("showResultDialog", "4");
		btnEnsure = (Button) resultDialog.findViewById(R.id.ensure);
		btnEnsure.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				resultDialog.cancel();
			}
		});
		btnEnsure.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					btnEnsure.setBackgroundResource(R.drawable.sure_down);
					break;
				case MotionEvent.ACTION_UP:
					btnEnsure.setBackgroundResource(R.drawable.sure);
				}
				return false;
			}
		});
		btnDeviation.setEnabled(true);
		btnDeviation.setBackgroundResource(R.drawable.right);
	}

	public void showDeviationDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		Log.d("showDeviationDialog", "1");
		final AlertDialog deviationDialog = builder.create();
		deviationDialog.show();
		Log.d("showDeviationDialog", "2");
		Window window = deviationDialog.getWindow();
		window.setGravity(Gravity.CENTER_VERTICAL);
		WindowManager.LayoutParams lp = window.getAttributes();
		// 设置透明度
		lp.alpha = 0.8f;
		window.setAttributes(lp);
		Log.d("showDeviationDialog", "3");
		deviationDialog.setContentView(R.layout.deviationdialog);
		Log.d("showDeviationDialog", "4");
		deviationListView = (HorizontalListView) deviationDialog
				.findViewById(R.id.deviationlist);
		Log.d("showDeviationDialog", "5");
		if (!data.getIsCalculate())
			data.Calculate();
		DeviationAdapter deviationAdapter = new DeviationAdapter(data);
		try {
			Log.d("showDeviationDialog", "51");
			deviationListView.setAdapter(deviationAdapter);
			Log.d("showDeviationDialog", "52");
		} catch (Exception e) {
			Log.d("showDeviationDialog Exception:", e.toString());
		}
		Log.d("showDeviationDialog", "6");
	}

	public void initAnimation() {
		animationUp = new TranslateAnimation(0, 0, 210*screenW/320, 0);
		animationUp.setFillAfter(true);
		animationUp.setDuration(600);
		animationDown = new TranslateAnimation(0, 0, 0, 210*screenW/320);
		animationDown.setFillAfter(true);
		animationDown.setDuration(600);
	}

	@Override
	public void reachToResult(int direction) {
		// TODO Auto-generated method stub
	}
	
	private class DataAdapter extends SimpleAdapter{

		private Context context;
		private ArrayList<HashMap<String, Object>>list;
		private LayoutInflater inflater;  
		
		public DataAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.context = context;  
            inflater = LayoutInflater.from(context);  
            list = new ArrayList<HashMap<String, Object>>();  
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){  
				convertView = inflater.inflate(R.layout.datalist, null);  
            }  
			return super.getView(position, convertView, parent);
		}
	}
}
