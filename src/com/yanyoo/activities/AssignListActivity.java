package com.yanyoo.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.yanyoo.R;
import com.yanyoo.adapter.AssignListAdapter;
import com.yanyoo.constant.Constants;
import com.yanyoo.model.Assign;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class AssignListActivity extends Activity {
	private Button assignlist_search;
	private ListView assignlist_listview;
	private Spinner assignlist_searchmode;
	private EditText assignlist_searchcont;
	private AssignListHandler assignListHandler;
	private AssignListAdapter assignListAdapter;
	private List<Assign> assignlistall;
	private List<Assign> assignlist;
	private Assign assign;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignlist);

		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar));
		actionBar.show();
		//设置返回按钮
		actionBar.setDisplayHomeAsUpEnabled(true);
		//隐藏logo
		actionBar.setDisplayShowHomeEnabled(false);

		assignlist_search = (Button) findViewById(R.id.assignlist_search);
		assignlist_listview = (ListView) findViewById(R.id.assignlist_listview);
		assignlist_searchmode = (Spinner) findViewById(R.id.assignlist_searchmode);
		assignlist_searchcont = (EditText) findViewById(R.id.assignlist_searchcont);
		
		assignlist_searchmode.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"标题","时间 ","发起","参与"}));
		
		assignListHandler = new AssignListHandler();
		new AssignListThread().start();
		
		assignlist_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int pos = (int) assignlist_searchmode.getSelectedItemId();
				String cont = assignlist_searchcont.getText().toString();
				assignlist.clear();
//				Toast.makeText(getApplicationContext(), pos+"", Toast.LENGTH_SHORT).show();
				for (int i = 0; i < assignlistall.size(); i++) {
					Assign a = assignlistall.get(i);
					switch (pos) {
						case 0:
							if (a.getTitle().contains(cont))
								assignlist.add(a);
							break;
						case 1:
							if (a.getTime().contains(cont))
								assignlist.add(a);
							break;
						case 2:
							if (a.getSponsor().contains(cont))
								assignlist.add(a);
							break;
						case 3:
							if (a.getMember().contains(cont))
								assignlist.add(a);
							break;
						default:
							break;
					}
				}
				assignListAdapter.notifyDataSetChanged();
			}
		});
		assignlist_searchcont.addTextChangedListener(new AssignListTextWatcher());
		/*
		assignlist_assponsor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				assignlist_searchmode.setSelection(2);
				assignlist_searchcont.setText(Constants.USER);
				assignlist_search.performClick();
			}
		});
		assignlist_asmember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				assignlist_searchmode.setSelection(3);
				assignlist_searchcont.setText(Constants.USER);
				assignlist_search.performClick();
			}
		});
		*/
		assignlist_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent;
				if(assignlist.get(pos).getSponsor().equals(Constants.USER))
					intent = new Intent(AssignListActivity.this, AssignUpdateActivity.class);
				else
					intent = new Intent(AssignListActivity.this, AssignViewActivity.class);
				intent.putExtra("id", assignlist.get(pos).getId());
				startActivity(intent);
				finish();
			}
		});
		
		assignlist_listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				assign = assignlist.get(pos);
				if(assign.getSponsor().equals(Constants.USER)){
					Dialog dialog = new AlertDialog.Builder(AssignListActivity.this)
							.setTitle(assign.getTitle())
							.setMessage("确认取消该活动？")
							.setPositiveButton("确认", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									new AssignDel().start();
								}
							}).create();
					dialog.show();
					return true;
				}
				return false;
						
				
			}
		});
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
				return true;
			case R.id.assignnew_bt:
				Intent intent = new Intent(AssignListActivity.this, AssignNewActivity.class);
				startActivity(intent);
				finish();
				return true;
			case R.id.assignlist_refresh:
				new AssignListThread().start();
				return true;
			case R.id.assignlist_asmember:
				assignlist_searchmode.setSelection(3);
				assignlist_searchcont.setText(Constants.USER);
				assignlist_search.performClick();
				return true;
			case R.id.assignlist_assponsor:
				assignlist_searchmode.setSelection(2);
				assignlist_searchcont.setText(Constants.USER);
				assignlist_search.performClick();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}



	class AssignListThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				HttpClient hc = new DefaultHttpClient();
        		String url = Constants.URL_SERVER_IP+"/servlet/assignlist";
        		Log.v("url",url);
    			HttpPost hp = new HttpPost(url);
    			HttpResponse hr = hc.execute(hp);
    			Log.v("status", hr.getStatusLine().getStatusCode()+"");
    			if(hr.getStatusLine().getStatusCode() == 200){
    				String result = EntityUtils.toString(hr.getEntity(), HTTP.UTF_8);
//    				Log.v("result", result);
    				assignlistall = JSON.parseArray(result, Assign.class);
    				if(assignlistall != null && assignlistall.get(0).getTitle() != null){
    					assignListHandler.sendMessage(assignListHandler.obtainMessage(100, "0"));
    				}
    				else if(assignlistall.get(0).getTitle() == null){
    					assignListHandler.sendMessage(assignListHandler.obtainMessage(100, "1"));
    				}
    				else{
    					assignListHandler.sendMessage(assignListHandler.obtainMessage(100, "-1"));
    				}
    			}
    			else{
    				assignListHandler.sendMessage(assignListHandler.obtainMessage(100, "-1"));
    			}
    			
    			if(hc != null){
    				hc.getConnectionManager().shutdown();
    			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				assignListHandler.sendMessage(assignListHandler.obtainMessage(100, "-1"));
			}
		}
	}
	
	class AssignDel extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				HttpClient hc = new DefaultHttpClient();
        		String url = Constants.URL_SERVER_IP+"/servlet/assigndel?id="+assign.getId();
        		Log.v("url",url);
    			HttpGet hg = new HttpGet(url);
    			HttpResponse hr = hc.execute(hg);
    			Log.v("status", hr.getStatusLine().getStatusCode()+"");
    			if(hr.getStatusLine().getStatusCode() == 200){
    				String result = EntityUtils.toString(hr.getEntity(), HTTP.UTF_8);
    				Log.v("result", result);
    				if(result.equals("0")){
    					assignListHandler.sendMessage(assignListHandler.obtainMessage(101, "2"));
    				}
    				else{
    					assignListHandler.sendMessage(assignListHandler.obtainMessage(101, "-1"));
    				}
    			}
    			else{
    				assignListHandler.sendMessage(assignListHandler.obtainMessage(101, "-1"));
    			}
    			
    			if(hc != null){
    				hc.getConnectionManager().shutdown();
    			}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				assignListHandler.sendMessage(assignListHandler.obtainMessage(101, "-1"));
			}
			
		}
		
	}
	class AssignListHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String res = (String)msg.obj;
			if(res.equals("0") && assignlistall != null){
				if(assignListAdapter == null){
					assignlist = new ArrayList<Assign>(assignlistall);
					assignListAdapter = new AssignListAdapter(AssignListActivity.this, assignlist);
					assignlist_listview.setAdapter(assignListAdapter);
				}else{
//					Log.v("refresh", assignlist.toString());
					assignlist.clear();
					assignlist.addAll(assignlistall);
//					Log.v("refresh", assignlist.toString());
					assignListAdapter.notifyDataSetChanged();
					assignlist_search.performClick();
				}
			}
			else if(res.equals("1")){
				Toast.makeText(getApplicationContext(), "目前没有活动", Toast.LENGTH_LONG).show();
			}
			else if(res.equals("2")){
				assignlist.remove(assign);
				assignlistall.remove(assign);
				assignListAdapter.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(), "已取消该活动", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
			}
		}
	}
	class AssignListTextWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			assignlist_search.performClick();
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
