package com.sierrapor.teslatracker.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sierrapor.teslatracker.R;
import com.sierrapor.teslatracker.data.Tesla;

import java.util.ArrayList;
import java.util.List;

public class TeslaAdapter extends RecyclerView.Adapter<TeslaAdapter.TeslaViewHolder> {
    private List<Tesla> teslaList = new ArrayList<>();

    public void setTeslaList(List<Tesla> teslaList) {
        this.teslaList = teslaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeslaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tesla, parent, false);
        return new TeslaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeslaViewHolder holder, int position) {
        Tesla tesla = teslaList.get(position);
        holder.plateTextView.setText(tesla.getPlate());
        holder.colorTextView.setText(tesla.getColor());
        holder.countTextView.setText(String.valueOf(tesla.getNumberTimesSeen()));
    }

    @Override
    public int getItemCount() {
        return teslaList.size();
    }

    static class TeslaViewHolder extends RecyclerView.ViewHolder {
        TextView plateTextView, colorTextView, countTextView;

        TeslaViewHolder(View itemView) {
            super(itemView);
            plateTextView = itemView.findViewById(R.id.text_plate);
            colorTextView = itemView.findViewById(R.id.text_color);
            countTextView = itemView.findViewById(R.id.text_count);
        }
    }
}
