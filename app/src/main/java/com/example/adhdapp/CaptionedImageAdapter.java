package com.example.adhdapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.zip.Inflater;

public class CaptionedImageAdapter  extends RecyclerView.Adapter<CaptionedImageAdapter.ViewHolder> {
VideoView vwoutside;
    public Uri[] videouris;
    public String[] captions;

    static interface  Listener{

        void cardclickedincaptionedImageAdapter(int id);

    }

    Listener listener;
  public CaptionedImageAdapter(Uri [] videouris,String []captions)
  {
      this.captions=captions;
      this.videouris=videouris;

  }

  public static class ViewHolder extends RecyclerView.ViewHolder
  {private CardView cardView;
     public ViewHolder(CardView cv)
      {

          super(cv);
          cardView=cv;


      }



  }
    @Override
    public int getItemCount()
    {
        return captions.length;

    }

   public void setListener(Listener listener)
   {

       this.listener=listener;

   }

@Override
    public CaptionedImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int a)
    {
CardView cv=(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.captionedimageadapter,parent,false);

return new ViewHolder(cv);



    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position)
    {
        CardView cv=holder.cardView;
        VideoView vw=cv.findViewById(R.id.vwcardview);
        Uri videoUri = videouris[holder.getAdapterPosition()];
        vw.setVideoURI(videoUri);
        vwoutside=vw;
        vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                vw.start(); // restart the video when it finishes playing
            }
        });

        vw.start();
        TextView tv=cv.findViewById(R.id.tvcardview);
tv.setText(captions[holder.getAdapterPosition()]);

cv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

      if(listener!=null)
      {
      listener.cardclickedincaptionedImageAdapter(holder.getAdapterPosition());


      }
    }
});





    }


}
