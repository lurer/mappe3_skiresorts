package com.example.s198599.s198599_mappe3.models;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.s198599.s198599_mappe3.R;


import java.util.List;


/**
 * Created by espen on 11/23/15.
 */
public class ResortListFragment extends ListFragment{
    private ResortAdapter adapter;
    private ListView lv;
    private ResortRepository repository;
    private List<Resort> list;

    private ResortAdapterCallback callback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = ResortRepository.getInstance().getResorts();

        adapter = new ResortAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1, list);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.resort_list_view, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (ResortAdapterCallback)activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        lv = (ListView)view.findViewById(R.id.resort_list_view);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                callback.onItemClicked(adapter.getItem(position));
            }
        });
    }


    public void sortAlphabeticallyAsc(){

    }

    public void sortAlphabeticallyDesc(){

    }

    public void sortDistanceAsc(){

    }

    public void sortDistanceDesc(){

    }
}

