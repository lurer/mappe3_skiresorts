package com.example.s198599.s198599_mappe3.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import com.example.s198599.s198599_mappe3.R;

/**
 * Created by espen on 11/23/15.
 */
public class ResortAdapter extends ArrayAdapter<Resort> {

    private Context mContext;
    private int mResource;
    private int mTextViewResourceId;
    private List<Resort> mList;

    public ResortAdapter(Context context, int resource, List<Resort> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mList = objects;
    }

    public ResortAdapter(Context context, int resource, int textViewResourceId, List<Resort> objects) {
        super(context, resource, textViewResourceId, objects);
        mContext = context;
        mResource = resource;
        mList = objects;
        mTextViewResourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.resort_adapter_item, null);

        }else{
            view = convertView;
        }

        ItemElements element = new ItemElements();
        element.name = (TextView)view.findViewById(R.id.rName);

        element.distance = (TextView)view.findViewById(R.id.rDistance);
        element.duration = (TextView)view.findViewById(R.id.rDuration);



        Resort resort = mList.get(position);

        element.name.setText(resort.getName());


        element.distance.setText(resort.getDistance().getDistanceKmString());
        element.duration.setText(resort.getDistance().getDurationString());


        return view;
    }


    private class ItemElements{
        TextView name;
        TextView duration;
        TextView distance;

    }
}
