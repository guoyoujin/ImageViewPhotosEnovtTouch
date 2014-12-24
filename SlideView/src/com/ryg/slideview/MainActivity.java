package com.ryg.slideview;

import java.util.ArrayList;
import java.util.List;

import com.ryg.slideview.SlideView.OnSlideListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener,
		OnClickListener, OnSlideListener {

	private static final String TAG = "MainActivity";

	private ListViewCompat mListView;

	SlideAdapter slideAdapter;
	
	int position;

	boolean isEdit = false;
	private List<MessageItem> mMessageItems = new ArrayList<MainActivity.MessageItem>();

	private SlideView mLastSlideViewWithStatusOn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mListView = (ListViewCompat) findViewById(R.id.list);

		for (int i = 0; i < 20; i++) {
			MessageItem item = new MessageItem();
			if (i % 3 == 0) {
				item.iconRes = R.drawable.default_qq_avatar;
				item.title = "腾讯新闻";
				item.msg = "青岛爆炸满月：大量鱼虾死亡";
				item.time = "晚上18:18";
			} else {
				item.iconRes = R.drawable.wechat_icon;
				item.title = "微信团队";
				item.msg = "欢迎你使用微信";
				item.time = "12月18日";
			}
			mMessageItems.add(item);
		}
		slideAdapter = new SlideAdapter();
		mListView.setAdapter(slideAdapter);
		mListView.setOnItemClickListener(this);
	}

	private class SlideAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		SlideAdapter() {
			super();
			mInflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			return mMessageItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mMessageItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			SlideView slideView = (SlideView) convertView;
			if (slideView == null) {
				View itemView = mInflater.inflate(R.layout.list_item, null);

				slideView = new SlideView(MainActivity.this);
				slideView.setContentView(itemView);
				holder = new ViewHolder(slideView);
				slideView.setOnSlideListener(MainActivity.this);
				slideView.setTag(holder);
			} else {
				holder = (ViewHolder) slideView.getTag();
			}
			MessageItem item = mMessageItems.get(position);
			item.slideView = slideView;
			item.slideView.reset();
			// holder.icon.setImageResource(item.iconRes);
			if (isEdit) {
				holder.title.setVisibility(View.VISIBLE);
				holder.arrowRight.setVisibility(View.GONE);
//				holder.title.setOnClickListener(MainActivity.this);
			}
			// holder.title.setText(item.title);
			// holder.msg.setText(item.msg);
			// holder.time.setText(item.time);
			holder.deleteHolder.setOnClickListener(MainActivity.this);

			return slideView;
		}

	}

	public class MessageItem {
		public int iconRes;
		public String title;
		public String msg;
		public String time;
		public SlideView slideView;
	}

	private static class ViewHolder {
		public TextView close;
		public ImageView icon;
		public TextView title;
		public TextView msg;
		public TextView arrowRight;
		public ViewGroup deleteHolder;

		ViewHolder(View view) {
			// close = (TextView) view.findViewById(R.id.list_left_close);
			// icon = (ImageView) view.findViewById(R.id.icon);
			title = (TextView) view.findViewById(R.id.list_left_close);
			// msg = (TextView) view.findViewById(R.id.msg);
			arrowRight = (TextView) view.findViewById(R.id.list_right);
			deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.e(TAG, "onItemClick position=" + position);
		if(isEdit){
			mMessageItems.get(position).slideView.open();
		}else{
			if(mMessageItems.get(position).slideView.close() && mMessageItems.get(position).slideView.getScrollX() == 0){
				if(mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn.getScrollX() == 0){
					Toast.makeText(MainActivity.this, "onItemClick position=" + position, 0).show();
				}
			}
		}
		
		
	}

	@Override
	public void onSlide(View view, int status) {
		if (mLastSlideViewWithStatusOn != null
				&& mLastSlideViewWithStatusOn != view) {
			mLastSlideViewWithStatusOn.shrink();
		}
		
		if (status == SLIDE_STATUS_ON) {
			mLastSlideViewWithStatusOn = (SlideView) view;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.holder) {
			Log.e(TAG, "onClick v=" + v);
			System.out.println("reove*****"+position);
			mMessageItems.remove(mListView.getPosition());
			slideAdapter.notifyDataSetChanged();
		} else if (v.getId() == R.id.list_left_close) {
			mMessageItems.get(mListView.getPosition()).slideView.open();
//			System.out.println("open --------"+position);
//			mLastSlideViewWithStatusOn.open();
		}

	}

	public void click(View v) {
		isEdit = true;
		slideAdapter.notifyDataSetChanged();
	}
}
