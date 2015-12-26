package com.rubbersoft.android.valveleakage.model;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rubbersoft.android.valveleakage.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Muhammad Muzammil on 27-Dec-15.
 */
public class ListAdapter extends ArrayAdapter<Data> {

    LayoutInflater mInflater;
    List<Data> data;
    public ListAdapter(Context context, int resource, List<Data> data) {
        super(context, resource, data);
        this.data = data;
        mInflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = mInflater.inflate(R.layout.listview_singleitem, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tv1 = (TextView) v.findViewById(R.id.textview_date);
            viewHolder.tv2 = (TextView) v.findViewById(R.id.textview_time);
            viewHolder.tv3 = (TextView) v.findViewById(R.id.textview_tempvalue);
            viewHolder.tv4 = (TextView) v.findViewById(R.id.textview_lpgvalue);
            v.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) v.getTag();
        viewHolder.tv1.setText(new SimpleDateFormat("dd-MM-yyy").format(new Date(data.get(position).getTimestamp())));
        viewHolder.tv2.setText(new SimpleDateFormat("hh:mm:ss a").format(new Date(data.get(position).getTimestamp())));
        viewHolder.tv3.setText(String.valueOf(data.get(position).getTemperature()));
        viewHolder.tv3.setText(String.valueOf(data.get(position).getLPGConcentration()));

        return v;

    }

    static class ViewHolder {
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tv4;
    }

}
