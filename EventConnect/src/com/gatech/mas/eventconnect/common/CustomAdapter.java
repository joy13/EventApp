package com.gatech.mas.eventconnect.common;

import java.util.ArrayList;

import com.gatech.mas.eventconnect.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Integer> {

    private Activity context;
    private ArrayList<Integer> objects;

    public CustomAdapter(Activity context, ArrayList<Integer> objects) {
        super(context, R.layout.grid_item_layout, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) i.inflate(R.layout.grid_item_layout, parent, false);
        }

        TextView t = (TextView) convertView.findViewById(R.id.label);
        ImageView i = (ImageView) convertView.findViewById(R.id.image);

        t.setText("label " + position);
        switch (position) {
        case 0:
        	t.setText("All events");
        	break;
        case 1:
        	t.setText("New events");
        	break;
        case 2:
        	t.setText("Recommended");
        	break;
        case 3:
        	t.setText("My events");
        	break;
        }
        i.setImageResource(objects.get(position));

        return convertView;
    }
}