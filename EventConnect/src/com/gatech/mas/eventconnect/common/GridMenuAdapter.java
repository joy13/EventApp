package com.gatech.mas.eventconnect.common;

import com.gatech.mas.eventconnect.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridMenuAdapter extends BaseAdapter {

	private Context mContext;
	public GridMenuAdapter() {
		// TODO Auto-generated constructor stub
	}

	public GridMenuAdapter(Context c) {
		// TODO Auto-generated constructor stub
		mContext = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mThumbIds[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	convertView = (View) inflater.inflate(R.layout.grid_item_layout, parent, false);
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(120, 120));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.allevents, R.drawable.newevents,
            R.drawable.recommended, R.drawable.myevents,
    };

}
