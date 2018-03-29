package com.example.ravindra.onlineexamination;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Ravindra on 05-Mar-18.
 */

public class MyAdapter2 extends BaseAdapter {
    LayoutInflater layoutInflater;

    Context context;

    public MyAdapter2(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.listclr, null);
        Button v = convertView.findViewById(R.id.btnColorId);
        TextView tv = convertView.findViewById(R.id.tvcolorId);
        Resources res = context.getResources();
        if (position == 0){
            v.setBackgroundColor(res.getColor(R.color.answered));
            tv.setText("Answered");
        }
        if (position == 1){
            v.setBackgroundColor(res.getColor(R.color.notAnswered));
            tv.setText("Not Answered");
        }
        if (position == 2){
            v.setBackgroundColor(res.getColor(R.color.notVisited));
            tv.setText("Not Visited");
        }

        return convertView;
    }
}
