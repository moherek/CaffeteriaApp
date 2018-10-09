package com.spamsoftware.hubertmaciejczyk.caffeteriaapp;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class StickerList {
    private List<ImageView> stickerList = new ArrayList<>();

    StickerList(){
    }

    public void addStickerToList(View imageView){
        stickerList.add((ImageView) imageView);
    }

    public void setStickerImages(int numberOfStickers){

        for(int i = 0; i < stickerList.size(); i++)

            if(numberOfStickers > i){
                stickerList.get(i).setImageResource(R.drawable.sticker);
            }
            else
                stickerList.get(i).setImageResource(R.drawable.emptysticker);

    }
}
