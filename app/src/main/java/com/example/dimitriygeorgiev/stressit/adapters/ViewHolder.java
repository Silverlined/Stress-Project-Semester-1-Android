package com.example.dimitriygeorgiev.stressit.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dimitriygeorgiev.stressit.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView img_vector;
    private TextView txt_main;
    private TextView txt_extra;


    ViewHolder(View itemView) {
        super(itemView);
        img_vector = itemView.findViewById(R.id.img_vector);
        txt_main = itemView.findViewById(R.id.txt_sensorValue);
        txt_extra = itemView.findViewById(R.id.txt_extra);
    }

    public void setImg_vector(int vector) {
        this.img_vector.setImageResource(vector);
    }

    public void setTxt_sensorValue(String[] values, int position) {
        if (position == 0) {
            txt_main.setText(String.valueOf(Math.round(Double.parseDouble(values[0]))) + " bpm");
            txt_extra.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            txt_extra.setText(String.valueOf(Math.round(Double.parseDouble(values[1]))) + " ms");
        } else {
            if (Integer.valueOf(values[1].substring(0, values[1].indexOf("."))) != 0) {
                txt_main.setText("Stress Level " + values[1]);
            } else {
                txt_main.setText("Normal");
            }
            txt_extra.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            txt_extra.setText(String.valueOf(values[0]) + " µS");
        }
    }
}