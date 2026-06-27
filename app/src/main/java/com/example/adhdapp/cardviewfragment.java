package com.example.adhdapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;


public class cardviewfragment extends Fragment {

static interface Listener
{

    void itemclicked(int id);

}
Listener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        RecyclerView rv=(RecyclerView) inflater.inflate(R.layout.fragment_cardviewfragment,container,false);
int [] videods= new int[VideosAndStrings.vas.length];

String [] captions=new String[VideosAndStrings.vas.length];
for(int i=0;i< captions.length;i++) {

videods[i]=VideosAndStrings.vas[i].getVideourl();
captions[i]=VideosAndStrings.vas[i].getCaption();

}
        Uri[] videoUris = new Uri[videods.length];
        for (int i = 0; i < videods.length; i++) {
            videoUris[i] = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + videods[i]);

        }

CaptionedImageAdapter adapter=new CaptionedImageAdapter(videoUris,captions);
rv.setAdapter(adapter);

        GridLayoutManager glm= new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);

rv.setLayoutManager(glm);

adapter.setListener(new CaptionedImageAdapter.Listener() {
    @Override
    public void cardclickedincaptionedImageAdapter(int id) {

        if(listener!=null)
        {
            listener.itemclicked(id);

        }
    }
});



        return rv;
    }

public void onAttach(Context context)
{
    super.onAttach(context);

    this.listener=(Listener)context;


}



}