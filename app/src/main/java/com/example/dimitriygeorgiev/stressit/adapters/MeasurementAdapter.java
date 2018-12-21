package com.example.dimitriygeorgiev.stressit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dimitriygeorgiev.stressit.R;

import java.util.List;

public class MeasurementAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List sensorValues;
    private Context mContext;
    private static final int COUNT_ITEMS = 2;

    public MeasurementAdapter(Context context, List sensorValues) {
        this.sensorValues = sensorValues;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_measurement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.setImg_vector(R.drawable.ic_favorite_black_24dp);

            String[] values = {String.valueOf(sensorValues.get(position)), String.valueOf(sensorValues.get(position + 1))};
            holder.setTxt_sensorValue(values, position);
        } else if (position == 1) {
            holder.setImg_vector(R.drawable.ic_accessibility_black_24dp);

            String[] values = {String.valueOf(sensorValues.get(position + 1)), String.valueOf(sensorValues.get(position + 2))};
            holder.setTxt_sensorValue(values, position);
        }
    }

    @Override
    public int getItemCount() {
        return COUNT_ITEMS;
    }
}
