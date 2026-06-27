package com.example.adhdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class speedDialog  {

    private Context context;
    private Dialog dialog;
    private SeekBar slider;
    private Button btnOk;
    private TextView dialogTitle;

    public speedDialog(Context context) {
        this.context = context;
    }

    public void showDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_speed_dialog);

        dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        slider = (SeekBar) dialog.findViewById(R.id.slider);
        btnOk = (Button) dialog.findViewById(R.id.btn_ok);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = slider.getProgress() + 1; // +1 because min is 1
                // Do something with the chosen value

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}