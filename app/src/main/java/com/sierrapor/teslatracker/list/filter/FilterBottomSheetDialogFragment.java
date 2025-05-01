package com.sierrapor.teslatracker.list.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sierrapor.teslatracker.R;
import com.sierrapor.teslatracker.data.Tesla;

import java.util.ArrayList;
import java.util.List;

public class FilterBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private FilterListener listener;

    public interface FilterListener {
        void onFiltersApplied(FilterOptions filters);
    }

    public void setFilterListener(FilterListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filters, container, false);

        EditText editColor = view.findViewById(R.id.edittext_color);
        EditText editCountry = view.findViewById(R.id.edittext_country);
        CheckBox checkForeign = view.findViewById(R.id.checkbox_foreign);
        SeekBar seekbarTimesSeen = view.findViewById(R.id.seekbar_times_seen);
        TextView textTimesSeen = view.findViewById(R.id.textview_times_seen);
        ChipGroup chipGroupPlayers = view.findViewById(R.id.chipgroup_players);
        Button applyButton = view.findViewById(R.id.button_apply);

        seekbarTimesSeen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textTimesSeen.setText(String.valueOf(progress));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        applyButton.setOnClickListener(v -> {
            FilterOptions filters = new FilterOptions();
            filters.setColor(editColor.getText().toString().trim());
            filters.setCountry(editCountry.getText().toString().trim());
            filters.setForeign(checkForeign.isChecked());
            filters.setMinTimesSeen(seekbarTimesSeen.getProgress());

            List<Tesla.players> selectedPlayers = new ArrayList<>();
            for (int i = 0; i < chipGroupPlayers.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupPlayers.getChildAt(i);
                if (chip.isChecked()) {
                    selectedPlayers.add(Tesla.players.valueOf(chip.getText().toString().toUpperCase()));
                }
            }
            filters.setSeenBy(selectedPlayers);

            if (listener != null) {
                listener.onFiltersApplied(filters);
            }

            dismiss();
        });

        return view;
    }
}


