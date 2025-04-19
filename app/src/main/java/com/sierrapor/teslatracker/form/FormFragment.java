package com.sierrapor.teslatracker.form;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sierrapor.teslatracker.R;
import com.sierrapor.teslatracker.data.Tesla;
import com.sierrapor.teslatracker.data.TeslaViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FormFragment extends DialogFragment {

    private EditText editTextPlate;
    private EditText editTextColor;
    private EditText editTextCountry;
    private TeslaViewModel teslaViewModel;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_form, null);

        List<Tesla.players> selectedPlayers = new ArrayList<>();
        editTextPlate = view.findViewById(R.id.edit_text_plate);
        editTextColor = view.findViewById(R.id.edit_text_color);
        editTextCountry = view.findViewById(R.id.edit_text_country);
        CheckBox checkBoxForeign = view.findViewById(R.id.checkbox_foreign);
        LinearLayout layoutCountry = view.findViewById(R.id.layout_country);
        Button buttonSelectPlayers = view.findViewById(R.id.button_select_players);
        ChipGroup chipGroupPlayers = view.findViewById(R.id.chip_group_players);

        checkBoxForeign.setOnCheckedChangeListener((buttonView, isChecked) -> {
            layoutCountry.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (!isChecked) editTextCountry.setText("");
        });

        buttonSelectPlayers.setOnClickListener(v -> {
            Tesla.players[] playersArray = Tesla.players.values();
            String[] playerNames = new String[playersArray.length];
            boolean[] checkedItems = new boolean[playersArray.length];

            for (int i = 0; i < playersArray.length; i++) {
                playerNames[i] = playersArray[i].name();
                checkedItems[i] = selectedPlayers.contains(playersArray[i]);
            }

            AlertDialog.Builder playerDialog = new AlertDialog.Builder(requireContext());
            playerDialog.setTitle(R.string.player_hint);
            playerDialog.setMultiChoiceItems(playerNames, checkedItems, (dialog, which, isChecked) -> {
                if (isChecked) {
                    if (!selectedPlayers.contains(playersArray[which])) {
                        selectedPlayers.add(playersArray[which]);
                    }
                } else {
                    selectedPlayers.remove(playersArray[which]);
                }
            });
            playerDialog.setPositiveButton(R.string.ok,(dialog, which) -> {
                // Actualizar chips al confirmar selección
                chipGroupPlayers.removeAllViews(); // Limpiar anteriores
                for (Tesla.players player : selectedPlayers) {
                    Chip chip = new Chip(requireContext());
                    chip.setText(player.name());
                    chip.setChipBackgroundColor(ContextCompat.getColorStateList(requireContext(), R.color.colorBackground));;
                    chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnBackground));
                    chip.setCloseIconVisible(true); // Hacemos visible el ícono de cierre
                    chip.setOnCloseIconClickListener(view1 -> {
                        selectedPlayers.remove(player);
                        chipGroupPlayers.removeView(chip);
                    });
                    chipGroupPlayers.addView(chip);
                }
            });
            playerDialog.setNegativeButton(R.string.cancel, null);
            playerDialog.show();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view)
                .setTitle(getString(R.string.add_button))
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.save), null); // <--- define aquí primero

        AlertDialog dialog = builder.create(); // <--- luego creas

        dialog.setOnShowListener(d -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String plate = editTextPlate.getText().toString().toUpperCase().trim();
                String color = editTextColor.getText().toString().trim();
                boolean isForeign = checkBoxForeign.isChecked();
                String country = isForeign ? editTextCountry.getText().toString().trim() : null;

                if (plate.isEmpty() && color.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.save_without_complete_plate_and_color), Toast.LENGTH_SHORT).show();
                } else if (plate.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.save_without_complete_plate), Toast.LENGTH_SHORT).show();
                }else if (color.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.save_without_complete_color), Toast.LENGTH_SHORT).show();
                } else if (plate.length() > 10) {
                    Toast.makeText(getActivity(), getString(R.string.plate_too_long), Toast.LENGTH_SHORT).show();
                } else {
                    saveTesla(plate, color, isForeign, country, selectedPlayers);
                    dialog.dismiss();
                    showConfirmationDialog();
                }
            });
        });

        dialog.show();
        return dialog;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Obtener el TeslaViewModel del Activity que hospeda el diálogo.
        // Esto asegura que se comparta el mismo ViewModel con el resto de la aplicación (por ejemplo, para actualizar la lista).
        teslaViewModel = new ViewModelProvider(requireActivity()).get(TeslaViewModel.class);
    }

    private void saveTesla(String plate, String color, boolean isForeign, String country, List<Tesla.players> selectedPlayers) {
        // Crea un nuevo objeto Tesla, asigna los valores e inserta en la base de datos a través del ViewModel
        Tesla newTesla = new Tesla();
        newTesla.setPlate(plate);
        newTesla.setColor(color);
        newTesla.setForeign(isForeign);
        newTesla.setCountry(isForeign ? country : null);
        newTesla.setLastTimeSeen(LocalDate.now());
        newTesla.setSeenBy(selectedPlayers);

        teslaViewModel.check(newTesla);
    }

    private void showConfirmationDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_confirmation);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(true);

        ImageView imageView = dialog.findViewById(R.id.image_confirmation);
        imageView.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}