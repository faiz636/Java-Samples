package com.rubbersoft.android.valveleakage.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rubbersoft.android.valveleakage.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Muhammad Muzammil on 27-Dec-15.
 */
public class ListAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    List<Data> data;
    Context context;
    public ListAdapter(Context context, List<Data> data) {
        this.data = data;
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return data.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return data.get(data.size()-position-1);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("FBLOG", "in getView");

        View v = convertView;

        if (v == null) {
            v = mInflater.inflate(R.layout.listview_singleitem, null,false);
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
        viewHolder.tv4.setText(String.valueOf(data.get(position).getLPGConcentration()));

        return v;

    }

    static class ViewHolder {
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tv4;
    }

}
