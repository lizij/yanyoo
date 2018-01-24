package com.yanyoo.activities;

import com.yanyoo.ui.CircleButton;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.yanyoo.R;
import com.yanyoo.constant.Constants;
import com.yanyoo.model.Assign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak") public class AssignViewActivity extends Activity {
	private CircleButton assignview_join;
	private TextView assignview_title;
	private TextView assignview_time;
	private TextView assignview_type;
	private TextView assignview_state;
	private TextView assignview_maxmem;
	private TextView assignview_sponsor;
	private TextView assignview_member;
	private TextView assignview_content;
	private TextView assignview_type_1;
	private int assign_id;
	private Assign assign;
	private AssignViewHandler assignViewHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignview);
		
		assignview_join = (CircleButton) findViewById(R.id.assignview_join);
		assignview_title = (TextView) findViewById(R.id.assignview_title);
		assignview_time = (TextView) findViewById(R.id.assignview_time);
		assignview_type = (TextView) findViewById(R.id.assignview_type);
		assignview_state = (TextView) findViewById(R.id.assignview_state);
		assignview_type_1 = (TextView) findViewById(R.id.assignview_type_1);
		assignview_maxmem = (TextView) findViewById(R.id.assignview_maxmem);
		assignview_sponsor = (TextView) findViewById(R.id.assignview_sponsor);
		assignview_member = (TextView) findViewById(R.id.assignview_member);
		assignview_content = (TextView) findViewById(R.id.assignview_content);
		
		assign_id = getIntent().getIntExtra("id", -1);
		assignViewHandler = new AssignViewHandler();
		new AssignViewThread().start();
		
		assignview_join.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(assign != null && !assign.getMember().contains(Constants.USER)){
					new AssignJoinThread().start();
				}
				else{
					Toast.makeText(getApplicationContext(), "你已加入，请勿重复操作", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void setAssignTextView(Assign assign){
		assignview_title.setText(assign.getTitle());
		assignview_time.setText(assign.getTime());
		assignview_type.setText(assign.getType());
		assignview_type_1.setText(assign.getType());
		assignview_state.setText(assign.getState());
		assignview_maxmem.setText(assign.getMaxmem()+"");
		assignview_sponsor.setText(assign.getSponsor());
		String[] members = assign.getMember().split("@");
		assignview_member.setText("已有 "+assign.getMember().replace("@", ",").substring(1)+" 共"+(members.length-1)+"人参加");
		if(!assign.getMember().contains(Constants.USER) && members.length-1<assign.getMaxmem()){
			assignview_join.setVisibility(View.VISIBLE);
		}
		assignview_content.setText(assign.getContent());
		
	}
	
	class AssignViewHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String res = (String)msg.obj;
			if(res.equals("0")){
				setAssignTextView(assign);
			}
			else if(res.equals("1")){
				assign.setMember(assign.getMember()+"@"+Constants.USER);
				setAssignTextView(assign);
				Toast.makeText(getApplicationContext(), "加入成功", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	class AssignViewThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
        		HttpClient hc = new DefaultHttpClient();
        		String url = Constants.URL_SERVER_IP+"/servlet/assignview?id="+assign_id;
        		Log.v("url",url);
    			HttpGet hg = new HttpGet(url);
    			HttpResponse hr = hc.execute(hg);
    			Log.v("status", hr.getStatusLine().getStatusCode()+"");
    			if(hr.getStatusLine().getStatusCode() == 200){
    				String result = EntityUtils.toString(hr.getEntity(), HTTP.UTF_8);
    				Log.v("result", result);
    				assign = JSON.parseObject(result, Assign.class);
    				if(assign != null){
    					assignViewHandler.sendMessage(assignViewHandler.obtainMessage(100, "0"));
    				}
    				else{
    					assignViewHandler.sendMessage(assignViewHandler.obtainMessage(100, "-1"));
    				}
    			}
    			
    			if(hc != null){
    				hc.getConnectionManager().shutdown();
    			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				assignViewHandler.sendMessage(assignViewHandler.obtainMessage(100, "-1"));
				
			}
		}
	}
	
	class AssignJoinThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
        		HttpClient hc = new DefaultHttpClient();
        		String url = Constants.URL_SERVER_IP+"/servlet/assignjoin?id="+assign_id+"&member="+assign.getMember()+"@"+Constants.USER;
        		Log.v("url",url);
    			HttpGet hg = new HttpGet(url);
    			HttpResponse hr = hc.execute(hg);
    			Log.v("status", hr.getStatusLine().getStatusCode()+"");
    			if(hr.getStatusLine().getStatusCode() == 200){
    				String result = EntityUtils.toString(hr.getEntity(), HTTP.UTF_8);
    				Log.v("result", result);
    				assignViewHandler.sendMessage(assignViewHandler.obtainMessage(100, result));
    			}
    			else{
    				assignViewHandler.sendMessage(assignViewHandler.obtainMessage(100,"-2"));
    			}
    			
    			if(hc != null){
    				hc.getConnectionManager().shutdown();
    			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				assignViewHandler.sendMessage(assignViewHandler.obtainMessage(100, "-2"));
				
			}
		}
	}
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	Intent intent = new Intent(AssignViewActivity.this, AssignListActivity.class);
    	startActivity(intent);
    	finish();
    }
}
