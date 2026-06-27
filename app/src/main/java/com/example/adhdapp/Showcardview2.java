package com.example.adhdapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Showcardview2 extends Fragment  {

static interface Listener1{
    void itemclicked(int position);


}
Listener1 listener1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView rv= (RecyclerView) inflater.inflate(R.layout.fragment_showcardview2, container, false);

String [] name={"talkspace","betterhelp","samhsa","veryWellMind","psychCentral","helpguide"};

int [] id={R.drawable.talkspace,R.drawable.betterhelp,R.drawable.samhsa,R.drawable.verywellmind,R.drawable.centralhealth,R.drawable.helpguide};




CaptionedImageAdapter2 adapter= new CaptionedImageAdapter2(name,id);

 rv.setAdapter(adapter);
        GridLayoutManager glm= new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);

     rv.setLayoutManager(glm);

        adapter.setListener(new CaptionedImageAdapter2.Listener() {
            public void clicked(int position) {
                if(listener1!=null)
                {
                   listener1.itemclicked(position);

                }
            }
        });

    return rv;
    }
@Override
    public void onAttach(Context context)
{
    super.onAttach(context);
    this.listener1=(Listener1) context;


}


}