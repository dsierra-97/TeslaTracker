package com.sierrapor.teslatracker.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sierrapor.teslatracker.R;
import com.sierrapor.teslatracker.data.Tesla;

public class TeslaAdapter extends ListAdapter<Tesla, RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public TeslaAdapter() {
        super(DIFF_CALLBACK);
    }

    // --------- VIEW TYPES ---------

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1; // +1 por el header
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    // --------- VIEW HOLDERS ---------

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_tesla, parent, false);
            return new TeslaViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            // Puedes configurar el texto del header si quieres
            // ((HeaderViewHolder) holder).headerTitle.setText("Cabecera");
        } else {
            Tesla tesla = getItem(position - 1); // Compensa el header
            TeslaViewHolder itemHolder = (TeslaViewHolder) holder;
            itemHolder.plateTextView.setText(tesla.getPlate());
            itemHolder.colorTextView.setText(tesla.getColor());
            itemHolder.countTextView.setText(String.valueOf(tesla.getNumberTimesSeen()));

            itemHolder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("tesla", tesla);
                Navigation.findNavController(v).navigate(R.id.action_listFragment_to_detailFragment, bundle);
            });
        }
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

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.text_header_plate);
        }
    }

    // --------- DIFF CALLBACK ---------

    private static final DiffUtil.ItemCallback<Tesla> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tesla>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tesla oldItem, @NonNull Tesla newItem) {
            return oldItem.getPlate().equals(newItem.getPlate()); // Asumimos que la matrícula es única
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tesla oldItem, @NonNull Tesla newItem) {
            return oldItem.equals(newItem);
        }
    };
}
