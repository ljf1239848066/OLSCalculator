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
	 * �����Integer������ӦAsyncTask�еĵ�һ������ �����String����ֵ��ӦAsyncTask�ĵ���������
	 * �÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�
	 * ���ǿ��Ե���publishProgress��������onProgressUpdate��UI���в���
	 */

	@Override
	protected String doInBackground(Integer... arg0) {
		Log.d("doInBackground", "1");
		data.Calculate();
		Log.d("doInBackground", "2");
		return null;
	}

	/**
	 * �÷���������UI�̵߳���,����������UI�̵߳��� ���Զ�UI�ռ��������
	 */
	@Override
	protected void onPreExecute() {
		Log.d("onPreExecute", "��ʼִ���첽�߳�");
		progressView.setVisibility(View.VISIBLE);
		super.onPreExecute();
	}
	
	/**
	 * �����String������ӦAsyncTask�еĵ�����������Ҳ���ǽ���doInBackground�ķ���ֵ��
	 * ��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������
	 */
	@Override
	protected void onPostExecute(String result) {
		Log.d("onPreExecute", "�첽��������");
		progressView.setVisibility(View.INVISIBLE);
		resultView.setResult(data);
		super.onPostExecute(result);
	}

	/**
	 * �����Intege������ӦAsyncTask�еĵڶ�������
	 * ��doInBackground�������У���ÿ�ε���publishProgress�������ᴥ��onProgressUpdateִ��
	 * onProgressUpdate����UI�߳���ִ�У����п��Զ�UI�ռ���в���
	 */

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
}
