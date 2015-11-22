package com.ols.olsclass;

import com.ols.calculator.R;
import com.ols.calculator.R.id;
import com.ols.calculator.R.layout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviationAdapter extends BaseAdapter{

	private Data data;
	private DataStr dataStr;
	public DeviationAdapter(Data data) {
		super();
		Log.d("DeviationAdapter", "super");
		this.data=data;
		dataStr=new DataStr(data);
		Log.d("DeviationAdapter:count", ""+data.getCount());
	}

	@Override
	public int getCount() {
		return data.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		Log.d("DeviationAdapter", "getItem");
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		Log.d("DeviationAdapter", "getItemId");
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("deviationAdapter", "getView");
		Log.d("deviationAdapter", "position="+position);
		Log.d("getView:position="+position,"layout:deviationlist="+R.layout.deviationlist);
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deviationlist, null);
		TextView tv_num = (TextView) view.findViewById(R.id.de_tv_num);
		TextView tv_numx = (TextView) view.findViewById(R.id.de_tv_numx);
		TextView tv_numy = (TextView) view.findViewById(R.id.de_tv_numy);
		TextView tv_y = (TextView) view.findViewById(R.id.de_tv_y);
		TextView tv_delta_y = (TextView) view.findViewById(R.id.de_tv_deltay);
		Log.d("getView:position="+position,"1");
		tv_num.setText(position+"");
		tv_numx.setText(dataStr.getNumx().get(position));
		tv_numy.setText(dataStr.getNumy().get(position));
		tv_y.setText(dataStr.getFity().get(position));
		tv_delta_y.setText(dataStr.getDeltay().get(position));
		Log.d("getView:position="+position,"2");
		return view;
	}
}
