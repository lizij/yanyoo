package com.yanyoo.activities;

import android.app.ActionBar;
import com.yanyoo.ui.CircleButton;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


@SuppressLint("HandlerLeak") public class AssignNewActivity extends Activity {
	private CircleButton assignnew_submit;
	private EditText assignnew_title;
	private EditText assignnew_time;
	private EditText assignnew_type;
	private EditText assignnew_state;
	private EditText assignnew_maxmem;
	private EditText assignnew_content;
	private Assign assign;
	private AssignNewHandler assignNewHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignnew);
		ActionBar actionbar = getActionBar();
		actionbar.hide();

        assignnew_submit = (CircleButton) findViewById(R.id.assignnew_submit);
        assignnew_title = (EditText) findViewById(R.id.assignnew_title);
        assignnew_time = (EditText) findViewById(R.id.assignnew_time);
        assignnew_type = (EditText) findViewById(R.id.assignnew_type);
        assignnew_state = (EditText) findViewById(R.id.assignnew_state);
        assignnew_maxmem = (EditText) findViewById(R.id.assignnew_maxmem);
        assignnew_content = (EditText) findViewById(R.id.assignnew_content);
        
        assignNewHandler = new AssignNewHandler();
		
        assignnew_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AssignNewThread().start();
			}
		});
    }

    private void getInput(){
    	assign = new Assign();
		assign.setTitle(assignnew_title.getText().toString());
		assign.setContent(assignnew_content.getText().toString());
		assign.setMember("@"+Constants.USER);
		assign.setState(assignnew_state.getText().toString());
		assign.setSponsor(Constants.USER);
		assign.setTime(assignnew_time.getText().toString());
		assign.setType(assignnew_type.getText().toString());
		assign.setMaxmem(Integer.parseInt(assignnew_maxmem.getText().toString()));
    }
    class AssignNewThread extends Thread{
    	@Override
    	public void run() {
    		try {
        		HttpClient hc = new DefaultHttpClient();
        		String url = Constants.URL_SERVER_IP+"/servlet/assignnew";
        		Log.v("url",url);
    			HttpPost hp = new HttpPost(url);
    			
    			getInput();
    			String jo = JSON.toJSONString(assign);
    			Log.v("para",jo);
    			hp.setEntity(new StringEntity(jo, HTTP.UTF_8));
    			HttpResponse hr = hc.execute(hp);
    			Log.v("status", hr.getStatusLine().getStatusCode()+"");
    			if(hr.getStatusLine().getStatusCode() == 200){
    				String result = EntityUtils.toString(hr.getEntity(), HTTP.UTF_8);
    				assignNewHandler.sendMessage(assignNewHandler.obtainMessage(100, result));
    				Log.v("result", result);
    			}
    			else{
    				assignNewHandler.sendMessage(assignNewHandler.obtainMessage(100, "-1"));
    			}
    			
    			if(hc != null){
    				hc.getConnectionManager().shutdown();
    			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				assignNewHandler.sendMessage(assignNewHandler.obtainMessage(100, "-1"));
				
			}
    		// TODO Auto-generated method stub
    	}
    }
    class AssignNewHandler extends Handler{
    	@Override
    	public void handleMessage(Message msg) {
    		// TODO Auto-generated method stub
    		if(((String)(msg.obj)).equals("0")){
				Toast.makeText(getApplicationContext(), "活动创建成功", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(AssignNewActivity.this, AssignListActivity.class);
				startActivity(intent);
				finish();
			}
			else{
				Toast.makeText(getApplicationContext(), "活动创建失败", Toast.LENGTH_LONG).show();
			}
    	}
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	Intent intent = new Intent(AssignNewActivity.this, AssignListActivity.class);
    	startActivity(intent);
    	finish();
    }
}