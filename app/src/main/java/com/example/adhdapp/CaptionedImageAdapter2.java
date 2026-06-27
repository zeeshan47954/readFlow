package com.example.adhdapp;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

class CaptionedImageAdapter2 extends RecyclerView.Adapter<CaptionedImageAdapter2.ViewHolder> {
static interface Listener{

 void clicked(int id);


}

Listener listener;

String[] captions;
int[]  imageids;

public CaptionedImageAdapter2(String[] captions, int[] imageids)
{
   this.captions=captions;
   this.imageids=imageids;

}

public static  class ViewHolder extends RecyclerView.ViewHolder {
   private CardView cardView;

   public ViewHolder(CardView v) {
       super(v);
       cardView = v;


   }
}
        @Override
        public int getItemCount()
        {
            return captions.length;


        }
    public void setListener(Listener listener)
    {
        this.listener = listener;
    }
@Override
public CaptionedImageAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent,int viewtype)
{
   CardView cv=(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.captionedimageadapter2,parent,false);
   return new ViewHolder(cv);



}
@Override
        public void onBindViewHolder(ViewHolder holder,final int position)
{
   CardView cardView=holder.cardView;
   ImageView iv=cardView.findViewById(R.id.imv);
   Drawable drawable= ContextCompat.getDrawable(cardView.getContext(),imageids[holder.getAdapterPosition()]);
   iv.setImageDrawable(drawable);
   TextView tv=cardView.findViewById(R.id.tv);

   tv.setText(captions[holder.getAdapterPosition()]);

   cardView.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
if(listener!=null)
{listener.clicked(holder.getAdapterPosition());}
       }
   });




}


    }









