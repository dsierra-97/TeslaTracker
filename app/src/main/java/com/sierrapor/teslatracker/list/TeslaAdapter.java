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

public class TeslaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Tesla> teslaList = new ArrayList<>();

    public void setTeslaList(List<Tesla> teslaList) {
        this.teslaList = teslaList;
        notifyDataSetChanged();
    }

    // El total de items es la cantidad de Teslas + 1 (el header)
    @Override
    public int getItemCount() {
        return teslaList.size() + 1;
    }

    // Se define el tipo de vista según la posición
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tesla, parent, false);
            return new TeslaViewHolder(view);
        }
    }

    // Al enlazar la vista, se distingue si es header o un item normal
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            // Configuras el cabecero (puedes asignar un título o imagen, etc.)
            //((HeaderViewHolder) holder).headerTitle.setText("Cabecero de la lista");
        } else if (holder instanceof TeslaViewHolder) {
            // Como el primer elemento es el header, los datos empiezan desde la posición 1
            Tesla tesla = teslaList.get(position - 1);
            TeslaViewHolder itemHolder = (TeslaViewHolder) holder;
            itemHolder.plateTextView.setText(tesla.getPlate());
            itemHolder.colorTextView.setText(tesla.getColor());
            itemHolder.countTextView.setText(String.valueOf(tesla.getNumberTimesSeen()));
        }
    }

    // ViewHolder para el item de la lista
    static class TeslaViewHolder extends RecyclerView.ViewHolder {
        TextView plateTextView, colorTextView, countTextView;

        TeslaViewHolder(View itemView) {
            super(itemView);
            plateTextView = itemView.findViewById(R.id.text_plate);
            colorTextView = itemView.findViewById(R.id.text_color);
            countTextView = itemView.findViewById(R.id.text_count);
        }
    }

    // ViewHolder para el cabecero
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.text_header_plate);
        }
    }
}

