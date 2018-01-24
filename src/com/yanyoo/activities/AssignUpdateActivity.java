package com.yanyoo.activities;

import com.yanyoo.ui.CircleButton;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.yanyoo.R;
import com.yanyoo.R.id;
import com.yanyoo.R.layout;
import com.yanyoo.constant.Constants;
import com.yanyoo.model.Assign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak") public class AssignUpdateActivity extends Activity {
	private EditText assignupdate_title;
	private EditText assignupdate_time;
	private EditText assignupdate_type;
	private EditText assignupdate_state;
	private EditText assignupdate_maxmem;
	private EditText assignupdate_member;
	private EditText assignupdate_content;
	private CircleButton assignupdate_submit;
	
	private Assign assign;
	private int assign_id;
	private AssignUpdateHandler assignUpdateHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignupdate);
		
		assignupdate_title = (EditText) findViewById(R.id.assignupdate_title);
		assignupdate_time = (EditText) findViewById(R.id.assignupdate_time);
		assignupdate_type = (EditText) findViewById(R.id.assignupdate_type);
		assignupdate_state = (EditText) findViewById(R.id.assignupdate_state);
		assignupdate_maxmem = (EditText) findViewById(R.id.assignupdate_maxmem);
		assignupdate_member = (EditText) findViewById(R.id.assignupdate_member);
		assignupdate_content = (EditText) findViewById(R.id.assignupdate_content);
		assignupdate_submit = (CircleButton) findViewById(R.id.assignupdate_submit);
		
		assign_id = getIntent().getIntExtra("id", -1);
		assignUpdateHandler = new AssignUpdateHandler();
		new AssignViewThread().start();
		assignupdate_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AssignUpdateThread().start();
			}
		});
	}
	
	class AssignUpdateThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				HttpClient hc = new DefaultHttpClient();
	    		String url = Constants.URL_SERVER_IP+"/servlet/assignupdate";
	    		Log.v("url",url);
				HttpPost hp = new HttpPost(url);
				getAssignEditView(assign);
//				Log.v("assign",assign.toString());
				String jo = JSON.toJSONString(assign);
//				Log.v("para",jo);
				hp.setEntity(new StringEntity(jo, HTTP.UTF_8));
				HttpResponse hr = hc.execute(hp);
				Log.v("status", hr.getStatusLine().getStatusCode()+"");
				if(hr.getStatusLine().getStatusCode() == 200){
					String result = EntityUtils.toString(hr.getEntity(), HTTP.UTF_8);
					assignUpdateHandler.sendMessage(assignUpdateHandler.obtainMessage(100, result));
//					Log.v("result", result);
				}
				else{
					assignUpdateHandler.sendMessage(assignUpdateHandler.obtainMessage(100, "-1"));
				}
				
				if(hc != null){
					hc.getConnectionManager().shutdown();
				}
			}catch(Exception e){
				e.printStackTrace();
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
    					assignUpdateHandler.sendMessage(assignUpdateHandler.obtainMessage(100, "1"));
    				}
    				else{
    					assignUpdateHandler.sendMessage(assignUpdateHandler.obtainMessage(100, "-1"));
    				}
    			}
    			
    			if(hc != null){
    				hc.getConnectionManager().shutdown();
    			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				assignUpdateHandler.sendMessage(assignUpdateHandler.obtainMessage(100, "-1"));
				
			}
		}
	}
	
	private void setAssignEditView(Assign assign){
		assignupdate_title.setText(assign.getTitle());
		assignupdate_time.setText(assign.getTime());
		assignupdate_type.setText(assign.getType());
		assignupdate_state.setText(assign.getState());
		assignupdate_maxmem.setText(assign.getMaxmem()+"");
		assignupdate_member.setText(assign.getMember().replace("@", ",").substring(1));
		assignupdate_content.setText(assign.getContent());
	}
	private void getAssignEditView(Assign assign){
		assign.setTitle(assignupdate_title.getText().toString());
		assign.setTime(assignupdate_time.getText().toString());
		assign.setType(assignupdate_type.getText().toString());
		assign.setState(assignupdate_state.getText().toString());
		assign.setMaxmem(Integer.parseInt(assignupdate_maxmem.getText().toString()));
		String[] members = assignupdate_member.getText().toString().split(",");
		StringBuilder member = new StringBuilder();
		for(String m:members)
			member.append("@"+m);
		assign.setMember(member.toString());
		assign.setContent(assignupdate_content.getText().toString());
	}
	
	class AssignUpdateHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String res = (String)msg.obj;
			if(res.equals("0")){
				Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(AssignUpdateActivity.this, AssignListActivity.class);
				startActivity(intent);
				finish();
			}else if(res.equals("1")){
				setAssignEditView(assign);
			}
			else{
				Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
			}
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(AssignUpdateActivity.this, AssignListActivity.class);
		startActivity(intent);
		finish();
	}
}
