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
 * adapter used to show data in list
 */
public class ListAdapter extends BaseAdapter {

    LayoutInflater mInflater;//used to inflate layouts
    List<Data> data;//data to be shown
    Context context;
    boolean mVwasNull = false;
    private int mLastPosition;
    public int mFirstVisibleItem;

    public ListAdapter(Context context, List<Data> data) {
        this.data = data;
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//getting inflator setvice
    }

    /**
     * Get size of list
     *
     * @return The size of data list
     */
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

    /**
     * called every time when a view is to created and data to be refreshed.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {//checking view is already created

            //create view
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
        //set background color to red if value is leakage value
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

        //animation for transprancy
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
        fadeAnim.setInterpolator(new AccelerateInterpolator());
        fadeAnim.setDuration(200);

        //animation for
        ObjectAnimator xTransAnim = ObjectAnimator.ofFloat(v, "translationX", 0,parent.getWidth()/4);
        xTransAnim.setInterpolator(new CustomInerpolator());
        xTransAnim.setDuration(200);

        //create animator set to run animation
        AnimatorSet animatorSet = new AnimatorSet();

        //add animations to set
        animatorSet.play(fadeAnim).with(xTransAnim);

        //run animations
        animatorSet.start();
    }

    /*
    * Class to hold reference of already  created views
    * to avoid finding it every time needed
    */
    static class ViewHolder {
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tv4;
    }



}
