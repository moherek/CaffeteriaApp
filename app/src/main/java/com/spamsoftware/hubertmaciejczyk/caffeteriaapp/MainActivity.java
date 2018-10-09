package com.spamsoftware.hubertmaciejczyk.caffeteriaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public Button buttonQrCameraStart;
    public static final int REQUEST_CODE = 1;

    private static int numberOfStickers;
    private final int MAX_STICKERS = 6;
    private StickerList stickerList;
    private final String filenameInternal = "stickersFiles";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createButtons();
        numberOfStickers = loadStickersNumberFromMemory();
        createStickersImageViewList();
        stickersGraphicsUpdate(numberOfStickers);
    }

    @Override
    protected void onResume() {
        super.onResume();
        numberOfStickers = loadStickersNumberFromMemory();
        stickersGraphicsUpdate(numberOfStickers);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){

                if(resultCode == Activity.RESULT_OK && data.getStringExtra("command").equals("ADD")) {
                    numberOfStickers++;
                    stickersGraphicsUpdate(numberOfStickers);
                    addStickerToast(numberOfStickers);
                    saveStickersToFile(numberOfStickers);
                }

                if(resultCode == Activity.RESULT_OK && data.getStringExtra("command").equals("DELETE")){
                    if(numberOfStickers >= MAX_STICKERS){
                        numberOfStickers = 0;
                        stickersGraphicsUpdate(numberOfStickers);
                        saveStickersToFile(numberOfStickers);
                        collectStickersPassedToast();
                    }else collectStickersFailedToast(numberOfStickers);
                }
        }
    }

    private void createButtons(){
        buttonQrCameraStart = findViewById(R.id.buttonQrCameraStart);
        buttonQrCameraStart.setOnClickListener(this);
    }


    private void createStickersImageViewList(){
        stickerList = new StickerList();
        stickerList.addStickerToList(findViewById(R.id.sticker1));
        stickerList.addStickerToList(findViewById(R.id.sticker2));
        stickerList.addStickerToList(findViewById(R.id.sticker3));
        stickerList.addStickerToList(findViewById(R.id.sticker4));
        stickerList.addStickerToList(findViewById(R.id.sticker5));
        stickerList.addStickerToList(findViewById(R.id.sticker6));
    }

    public void goScannerQrActivity(Context mContext) {
        Intent scanner = new Intent(mContext, QrScannerActivity.class);
        startActivityForResult(scanner, REQUEST_CODE);
    }

    private void stickersGraphicsUpdate(int numberOfStickers){
        stickerList.setStickerImages(numberOfStickers);
        if(numberOfStickers >= MAX_STICKERS){
            buttonQrCameraStart.setText(R.string.button_collect);
        }
        else buttonQrCameraStart.setText(R.string.button_add);
    }

    private void addStickerToast(int numberOfStickers){
        if(numberOfStickers < MAX_STICKERS){
            Toast.makeText(this,"Zdobyłeś naklejkę :) Jeszcze " +
                            (MAX_STICKERS - MainActivity.numberOfStickers) +
                            " i darmowa kawa!",
                            Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, "Brawo! Zapraszamy na darmową kawę :)", Toast.LENGTH_LONG).show();
    }

    private void collectStickersPassedToast(){
            Toast.makeText(this,"Smacznego! Zapraszamy do dalszego zbierania.", Toast.LENGTH_LONG).show();
    }

    private void collectStickersFailedToast(int numberOfStickers){
        Toast.makeText(this, "Żeby odebrać darmową kawkę potrzebujesz " + MAX_STICKERS +
                " naklejek. Zbieraj dalej!", Toast.LENGTH_LONG).show();
    }

    private void saveStickersToFile(int numberOfStickers){
        String fileName = filenameInternal;
        String content = Integer.toString(numberOfStickers);
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int loadStickersNumberFromMemory(){
        String fileName = filenameInternal;
        String content;
        int numberOfStickers = 0;
        FileInputStream inputStream = null;
        try{
            inputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((content = bufferedReader.readLine()) != null){
                stringBuffer.append(content);
            }
            numberOfStickers = Integer.parseInt(stringBuffer.toString());
            return numberOfStickers;
        } catch (Exception e) {
            e.printStackTrace();
            return numberOfStickers;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonQrCameraStart: {
                goScannerQrActivity(this);
                break;
            }
        }
    }


}

