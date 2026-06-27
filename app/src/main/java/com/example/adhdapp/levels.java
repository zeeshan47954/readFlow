package com.example.adhdapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class levels extends ListFragment {
    static interface Listener {
        void itemClicked(long id);

    }

    Listener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String[] name = {"level1", "level2", "level3", "level4", "level5"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, name);

        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;

    }

    @Override
    public void onListItemClick(ListView lv, View view, int position, long id) {
        if (listener != null) {
            listener.itemClicked(id);

        }
    }
}