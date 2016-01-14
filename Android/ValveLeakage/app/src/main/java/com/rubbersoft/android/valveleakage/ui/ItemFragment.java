package com.rubbersoft.android.valveleakage.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.rubbersoft.android.valveleakage.model.ListAdapter;
import com.rubbersoft.android.valveleakage.utils.Callback;
import com.rubbersoft.android.valveleakage.utils.ConfigConstants;
import com.rubbersoft.android.valveleakage.utils.DataBaseSource;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends ListFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mNodeName;

    private Context context;
    private ListAdapter listAdapter;
    private Callback callback;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Rename and change types of parameters
    public static ItemFragment newInstance(String param1) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mNodeName = getArguments().getString(ARG_PARAM1);
        }
        List list = null;

        callback = new Callback() {
            @Override
            public void callback() {
                notifyDataChanged();
            }
        };

        switch (mNodeName) {
            case ConfigConstants.TABLE_NODE1:
                list = DataBaseSource.getInstance().dataNode1;
                break;
            case ConfigConstants.TABLE_NODE2:
                list = DataBaseSource.getInstance().dataNode2;
                break;
            case ConfigConstants.TABLE_NODE3:
                list = DataBaseSource.getInstance().dataNode3;
                break;
            case ConfigConstants.TABLE_NODE4:
                list = DataBaseSource.getInstance().dataNode4;
                break;
        }

        listAdapter = new ListAdapter(getActivity(), list);

        // TODO: Change Adapter to display your content
        setListAdapter(listAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (mNodeName) {
            case ConfigConstants.TABLE_NODE1:
                DataBaseSource.getInstance().dataChangeCallback1 = callback;
                return;
            case ConfigConstants.TABLE_NODE2:
                DataBaseSource.getInstance().dataChangeCallback2 = callback;
                return;
            case ConfigConstants.TABLE_NODE3:
                DataBaseSource.getInstance().dataChangeCallback3 = callback;
                return;
            case ConfigConstants.TABLE_NODE4:
                DataBaseSource.getInstance().dataChangeCallback4 = callback;
                return;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        switch (mNodeName) {
            case ConfigConstants.TABLE_NODE1:
                DataBaseSource.getInstance().dataChangeCallback1 = null;
                return;
            case ConfigConstants.TABLE_NODE2:
                DataBaseSource.getInstance().dataChangeCallback2 = null;
                return;
            case ConfigConstants.TABLE_NODE3:
                DataBaseSource.getInstance().dataChangeCallback3 = null;
                return;
            case ConfigConstants.TABLE_NODE4:
                DataBaseSource.getInstance().dataChangeCallback4 = null;
                return;
        }


    }

    public void notifyDataChanged() {
        listAdapter.notifyDataSetChanged();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
/*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
*/

}
