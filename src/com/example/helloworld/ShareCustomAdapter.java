package com.example.helloworld;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShareCustomAdapter extends BaseAdapter  {
	 LayoutInflater inflater;
	Context _context;
	List<AppInfo> _list;
	public ShareCustomAdapter(Context mContext, List<AppInfo> shareAppInfos) {
		// TODO Auto-generated constructor stub
		 _context = mContext;
		 _list= shareAppInfos;
		 inflater = LayoutInflater.from( mContext );
	}
	
	 public int getCount() {
	        return _list.size();
	      }
	  
	    public Object getItem(int arg0) {
	        return _list.get(arg0);
	   }
	
	  public View getView ( int position, View convertView, ViewGroup parent ) {
		 AppInfo app=null;
		  if (convertView == null) {
		      /* create a new view of my layout and inflate it in the row */
			   convertView = ( LinearLayout ) inflater.inflate(  R.layout.popup_share_item, null );
			
			   app = (AppInfo) getItem( position );
	          /* Take the TextView from layout and set the city's name */
	          TextView txtName = (TextView) convertView.findViewById(R.id.share_item_name);
	          txtName.setText(app.getAppName());
	        
	          ImageView icon = (ImageView) convertView.findViewById(R.id.share_item_icon);
	          icon.setImageDrawable(  app.getAppIcon());
	          convertView.setTag(app);
		  }else{
			  convertView.getTag();
		  }
    
          return convertView;
    }

	@Override
	public int getItemViewType(int arg0) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
		// TODO Auto-generated method stub
	
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		 
		return true;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
