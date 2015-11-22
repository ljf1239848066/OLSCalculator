package com.ols.olsclass;

import com.ols.calculator.R;
import com.ols.view.ResultView;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class CalculateAsyncTask extends AsyncTask<Integer, Integer, String> {

	private ProgressBar progressView;
	private ResultView resultView;
	private Data data;
	private int[] ImagesId;
	private int count = 0;

	public CalculateAsyncTask(ProgressBar pb, ResultView rV, Data data) {
		super();
		Log.d("CalculateAsyncTask", "1");
		this.progressView = pb;
		this.resultView = rV;
		this.data = data;
		ImagesId = new int[] { R.drawable.calculating_1,
				R.drawable.calculating_2, R.drawable.calculating_3,
				R.drawable.calculating_4, R.drawable.calculating_4,
				R.drawable.calculating_6, R.drawable.calculating_5,
				R.drawable.calculating_8 };
	}

	/**
	 * 这里的Integer参数对应AsyncTask中的第一个参数 这里的String返回值对应AsyncTask的第三个参数
	 * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
	 * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
	 */

	@Override
	protected String doInBackground(Integer... arg0) {
		Log.d("doInBackground", "1");
		data.Calculate();
		Log.d("doInBackground", "2");
		return null;
	}

	/**
	 * 该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
	 */
	@Override
	protected void onPreExecute() {
		Log.d("onPreExecute", "开始执行异步线程");
		progressView.setVisibility(View.VISIBLE);
		super.onPreExecute();
	}
	
	/**
	 * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
	 * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
	 */
	@Override
	protected void onPostExecute(String result) {
		Log.d("onPreExecute", "异步操作结束");
		progressView.setVisibility(View.INVISIBLE);
		resultView.setResult(data);
		super.onPostExecute(result);
	}

	/**
	 * 这里的Intege参数对应AsyncTask中的第二个参数
	 * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
	 * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
	 */

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
}
