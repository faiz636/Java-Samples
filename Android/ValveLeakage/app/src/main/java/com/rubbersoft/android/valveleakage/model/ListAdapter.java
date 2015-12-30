package com.rubbersoft.android.valveleakage.model;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
    boolean mVwasNull = false;
    private int mLastPosition;
    public int mFirstVisibleItem;
    ListView mlistView;

    public ListAdapter(Context context, List<Data> data, ListView listView) {
        this.data = data;
        this.context = context;
        this.mlistView = listView;
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
        return data.get(position);
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_singleitem, null,false);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.tv1 = (TextView) convertView.findViewById(R.id.textview_date);
            viewHolder.tv2 = (TextView) convertView.findViewById(R.id.textview_time);
            viewHolder.tv3 = (TextView) convertView.findViewById(R.id.textview_tempvalue);
            viewHolder.tv4 = (TextView) convertView.findViewById(R.id.textview_lpgvalue);
            convertView.setTag(viewHolder);
            Log.d("LA", "in mFirstTime == true");

            this.mVwasNull =true;
        }

        //custom animations for scrolling upward and downward
        implementCustomAnimations(parent, convertView);

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        settingTextsInTextViews(position, viewHolder);
        //checking and setting the desired colors for LPG
        checkIfErrorInLpg(position, viewHolder);
        //Different animations for scrollling upward and downward list..
//        implementingListViewAnimations(position, parent, convertView, mLastView);
        return convertView;
    }

    private void settingTextsInTextViews(int position, ViewHolder viewHolder) {
        viewHolder.tv1.setText(new SimpleDateFormat("dd-MM-yyy").format(new Date(data.get(position).getTimestamp())));
        viewHolder.tv2.setText(new SimpleDateFormat("hh:mm:ss a").format(new Date(data.get(position).getTimestamp())));
        viewHolder.tv3.setText(String.valueOf(data.get(position).getTemperature()));
        viewHolder.tv4.setText(String.valueOf(data.get(position).getLPGConcentration()));
    }

    private void checkIfErrorInLpg(int position, ViewHolder viewHolder) {
        if(Float.valueOf(data.get(position).getLPGConcentration()) > 200f ){
            viewHolder.tv4.setTextColor(Color.parseColor("black"));
            viewHolder.tv4.setBackgroundColor(Color.parseColor("#FF0000"));


        }
        else{
            viewHolder.tv4.setBackgroundColor(0);
            viewHolder.tv4.setTextColor(Color.parseColor("green"));
        }
    }

    private void implementCustomAnimations(ViewGroup parent, View v) {
        Log.d("LA", "mLastPosition < position");

        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
        fadeAnim.setInterpolator(new AccelerateInterpolator());
        fadeAnim.setDuration(200);

        ObjectAnimator xTransAnim = ObjectAnimator.ofFloat(v, "translationX", 0,parent.getWidth()/4);
        xTransAnim.setInterpolator(new CustomInerpolator());
        xTransAnim.setDuration(200);
        xTransAnim.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeAnim).with(xTransAnim);
        animatorSet.start();
    }

    static class ViewHolder {
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tv4;
    }



}
