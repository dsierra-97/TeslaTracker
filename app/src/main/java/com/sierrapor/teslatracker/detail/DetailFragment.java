package com.sierrapor.teslatracker.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sierrapor.teslatracker.R;
import com.sierrapor.teslatracker.data.Tesla;
import com.sierrapor.teslatracker.data.TeslaViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailFragment extends Fragment {

    private EditText editPlate, editColor, editCountry, editNumber;
    private Button buttonEdit, buttonSave;
    private LinearLayout layoutCountry;
    private boolean isEditMode = false;
    private TeslaViewModel viewModel;
    private Tesla tesla;

    private CheckBox checkboxForeign;
    private Spinner spinnerPlayers;
    private ChipGroup selectedPlayersContainer;
    private List<Tesla.players> selectedPlayers = new ArrayList<>();
    private LinearLayout playerSelectionLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Obtención de referencias del layout.
        editPlate = view.findViewById(R.id.edit_plate);
        editColor = view.findViewById(R.id.edit_color);
        layoutCountry = view.findViewById(R.id.layout_country);
        editCountry = view.findViewById(R.id.edit_country);
        editNumber = view.findViewById(R.id.edit_number);
        buttonEdit = view.findViewById(R.id.button_edit);
        buttonSave = view.findViewById(R.id.button_save);
        checkboxForeign = view.findViewById(R.id.checkbox_foreign);
        spinnerPlayers = view.findViewById(R.id.playerSpinner);
        selectedPlayersContainer = view.findViewById(R.id.playerChips);
        playerSelectionLayout = view.findViewById(R.id.playerSelectionLayout);

        checkboxForeign.setOnCheckedChangeListener((buttonView, isChecked) ->
                layoutCountry.setVisibility(isChecked ? View.VISIBLE : View.GONE));
        layoutCountry.setVisibility(checkboxForeign.isChecked() ? View.VISIBLE : View.GONE);

        if (getArguments() != null) {
            tesla = (Tesla) getArguments().getSerializable("tesla");
            showData();
            lockFields();
        }

        buttonEdit.setOnClickListener(v -> {
            isEditMode = true;
            unlockFields();
            playerSelectionLayout.setVisibility(View.VISIBLE);
            updateChips();
            buttonEdit.setVisibility(View.GONE);
            buttonSave.setVisibility(View.VISIBLE);
        });

        buttonSave.setOnClickListener(v -> {
            saveChanges();
            lockFields();
            isEditMode = false;
            playerSelectionLayout.setVisibility(View.GONE);
            updateChips();
            buttonEdit.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.GONE);
        });

        setupPlayerSpinner(); // ← Aquí se aplica la nueva lógica encapsulada

        return view;
    }

    private void setupPlayerSpinner() {
        List<Object> spinnerItems = new ArrayList<>();
        spinnerItems.add(getString(R.string.select_a_player));
        spinnerItems.addAll(List.of(Tesla.players.values()));
        ArrayAdapter<Object> adapter = getObjectArrayAdapter(spinnerItems);
        spinnerPlayers.setAdapter(adapter);

        spinnerPlayers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View viewSpinner, int position, long id) {
                if (position == 0) return;
                Tesla.players selected = (Tesla.players) parent.getItemAtPosition(position);
                if (!selectedPlayers.contains(selected)) {
                    selectedPlayers.add(selected);
                    updateChips();
                }
                spinnerPlayers.setSelection(0); // Reset al placeholder
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @NonNull
    private ArrayAdapter<Object> getObjectArrayAdapter(List<Object> spinnerItems) {
        ArrayAdapter<Object> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, spinnerItems
        ) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view).setText(position == 0
                        ? getString(R.string.select_a_player)
                        : ((Tesla.players) Objects.requireNonNull(getItem(position))).name());
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                return getView(position, convertView, parent);
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void showData() {
        editPlate.setText(tesla.getPlate());
        editColor.setText(tesla.getColor());
        editCountry.setText(tesla.getCountry());
        // Muestra el número de veces visto
        editNumber.setText(String.valueOf(tesla.getNumberTimesSeen()));
        checkboxForeign.setChecked(tesla.isForeign());

        if (tesla.getSeenBy() != null) {
            selectedPlayers.clear();
            selectedPlayers.addAll(tesla.getSeenBy());
            updateChips();
        }
    }

    private void lockFields() {
        editPlate.setEnabled(false);
        editColor.setEnabled(false);
        editCountry.setEnabled(false);
        editNumber.setEnabled(false);
        checkboxForeign.setEnabled(false);
        // No es necesario bloquear manualmente el ChipGroup;
        // La eliminación se controla en updateChips según isEditMode.
        spinnerPlayers.setEnabled(false);
    }

    private void unlockFields() {
        editPlate.setEnabled(true);
        editColor.setEnabled(true);
        editCountry.setEnabled(true);
        editNumber.setEnabled(true);
        checkboxForeign.setEnabled(true);
        spinnerPlayers.setEnabled(true);
    }

    private void saveChanges() {
        tesla.setPlate(editPlate.getText().toString());
        tesla.setColor(editColor.getText().toString());
        tesla.setCountry(editCountry.getText().toString());
        try {
            tesla.setNumberTimesSeen(Integer.parseInt(editNumber.getText().toString()));
        } catch (NumberFormatException e) {
            tesla.setNumberTimesSeen(tesla.getNumberTimesSeen());
        }
        tesla.setForeign(checkboxForeign.isChecked());
        tesla.setSeenBy(new ArrayList<>(selectedPlayers));
        //TODO: Implementar el método edit en el viewModel
        // viewModel.edit(tesla);
    }

    private void updateChips() {
        // Elimina todos los chips
        selectedPlayersContainer.removeAllViews();

        for (Tesla.players player : selectedPlayers) {
            Chip chip = new Chip(requireContext());
            chip.setText(player.name() + " ✖");

            // Personaliza el Chip: fondo, márgenes, etc.
            chip.setChipBackgroundColorResource(android.R.color.holo_blue_light);
            chip.setTextColor(getResources().getColor(android.R.color.white));
            chip.setClickable(isEditMode); // Solo clickeable en modo edición
            chip.setCheckable(false);

            // Solo permitir eliminar si estamos en modo edición.
            if (isEditMode) {
                chip.setOnClickListener(v -> {
                    selectedPlayers.remove(player);
                    updateChips();
                });
            }
            selectedPlayersContainer.addView(chip);
        }
    }
}

