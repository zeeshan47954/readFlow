package com.example.adhdapp;

public class VideosAndStrings {
    int  videourl;
    String  caption;

    public VideosAndStrings(int videourl,String caption)
    {
        this.caption=caption;
        this.videourl=videourl;

    }

 public static final VideosAndStrings [] vas={
      new VideosAndStrings(R.raw.readingfinal,"read_article"),new VideosAndStrings(R.raw.brain,"challenges"),new VideosAndStrings(R.raw.audio,"increase focus"),new VideosAndStrings(R.raw.help,"talk to expert")


 };
    public int getVideourl()
    {

      return  this.videourl;

    }
    public String getCaption()
    {

       return this.caption;

    }

    public String toString()
    {

        return this.caption;

    }

}
