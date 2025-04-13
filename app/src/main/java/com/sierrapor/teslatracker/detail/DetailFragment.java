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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sierrapor.teslatracker.R;
import com.sierrapor.teslatracker.data.Tesla;
import com.sierrapor.teslatracker.data.TeslaViewModel;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Obtención de referencias del layout. Asegúrate de que los IDs coincidan con tu XML.
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

        // Configuramos el listener para el CheckBox
        checkboxForeign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Si no está marcado, ocultamos el campo de país; si está marcado, lo mostramos.
                layoutCountry.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        // Establece el estado inicial del campo "Country" en función del estado del checkbox.
        layoutCountry.setVisibility(checkboxForeign.isChecked() ? View.VISIBLE : View.GONE);

        // En modo vista, ocultamos el Spinner.
        spinnerPlayers.setVisibility(View.GONE);

        // Cargar datos si vienen argumentos
        if (getArguments() != null) {
            tesla = (Tesla) getArguments().getSerializable("tesla");
            showData();
            lockFields();
        }

        buttonEdit.setOnClickListener(v -> {
            isEditMode = true;
            unlockFields();
            // Al entrar en modo edición, mostramos el spinner
            spinnerPlayers.setVisibility(View.VISIBLE);
            updateChips(); // Para activar/eliminar la funcionalidad de los chips en modo edición
            buttonEdit.setVisibility(View.GONE);
            buttonSave.setVisibility(View.VISIBLE);
        });

        buttonSave.setOnClickListener(v -> {
            saveChanges();
            lockFields();
            isEditMode = false;
            // Ocultamos el spinner en modo vista
            spinnerPlayers.setVisibility(View.GONE);
            updateChips();
            buttonEdit.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.GONE);
        });

        // Configuramos el Spinner
        ArrayAdapter<Tesla.players> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, Tesla.players.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayers.setAdapter(adapter);

        spinnerPlayers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View viewSpinner, int position, long id) {
                Tesla.players player = Tesla.players.values()[position];
                if (!selectedPlayers.contains(player)) {
                    selectedPlayers.add(player);
                    updateChips();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        return view;
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

