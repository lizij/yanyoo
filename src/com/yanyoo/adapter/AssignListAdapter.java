package com.yanyoo.adapter;

import java.util.List;

import android.widget.ImageView;
import com.yanyoo.R;
import com.yanyoo.model.Assign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AssignListAdapter extends BaseAdapter{
	private List<Assign> assignlist = null;
	private LayoutInflater inflater = null;
	
	public AssignListAdapter(Context context, List<Assign> list){
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		assignlist = list;		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return assignlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return assignlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return assignlist.get(arg0).getId();
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = null;
		ViewHolder holder = null;
		if(convertView == null || convertView.getTag() == null){
			view = inflater.inflate(R.layout.activity_assignlist_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		}else{
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		
		Assign assign = (Assign) getItem(pos);
		holder.getTitle().setText(assign.getTitle());
		holder.getSponsor().setText(assign.getSponsor());
		holder.getTime().setText(assign.getTime().replaceAll("-", "."));
		holder.getType().setText(assign.getType());
		String typeTem = assign.getType();
		if(typeTem.equals("交友聚会"))
		{
			holder.getImage().setImageResource(R.drawable.jiaoyou);
			holder.getTextview().setBackgroundResource(R.drawable.textview_radius_style1);
		}
		else if(typeTem.equals("体育运动"))
		{
			holder.getImage().setImageResource(R.drawable.tiyu);
			holder.getTextview().setBackgroundResource(R.drawable.textview_radius_style2);
		}
		else if(typeTem.equals("户外旅行"))
		{
			holder.getImage().setImageResource(R.drawable.lvxing);
			holder.getTextview().setBackgroundResource(R.drawable.textview_radius_style3);
		}
		else if(typeTem.equals("讲座公益"))
		{
			holder.getImage().setImageResource(R.drawable.jiangzuo);
			holder.getTextview().setBackgroundResource(R.drawable.textview_radius_style4);
		}
		else if(typeTem.equals("音乐戏剧"))
		{
			holder.getImage().setImageResource(R.drawable.yinyue);
			holder.getTextview().setBackgroundResource(R.drawable.textview_radius_style5);
		}
		else
		{
			holder.getImage().setImageResource(R.drawable.activity);
			holder.getTextview().setBackgroundResource(R.drawable.textview_radius_style);
		}
		return view;
	}
	
	class ViewHolder{
		private ImageView image;
		private TextView textView;
		private TextView title;
		private TextView sponsor;
		private TextView time;
		private TextView type;

		public ImageView getImage() { return image; }
		public void setImage(ImageView image) { this.image = image; }

		public TextView getTextview() { return textView; }
		public void setTexitview(TextView textView) {
			this.textView = textView;
		}

		public TextView getType() {
			return type;
		}
		public void setType(TextView type) {
			this.type = type;
		}

		public TextView getTime() { return time; }
		public void setTime(TextView time) {
			this.time = time;
		}

		public TextView getTitle() {
			return title;
		}
		public void setTitle(TextView title) {
			this.title = title;
		}

		public TextView getSponsor() {
			return sponsor;
		}
		public void setSponsor(TextView sponsor) {
			this.sponsor = sponsor;
		}

		
		public ViewHolder(View view){
			this.textView = (TextView) view.findViewById(R.id.textView);
			this.image = (ImageView) view.findViewById(R.id.activity);
			this.title = (TextView) view.findViewById(R.id.assignlist_item_title);
			this.sponsor = (TextView) view.findViewById(R.id.assignlist_item_sponsor);
			this.time = (TextView) view.findViewById(R.id.assignlist_item_time);
			this.type = (TextView) view.findViewById(R.id.assignlist_item_type);
		}
	}

}
