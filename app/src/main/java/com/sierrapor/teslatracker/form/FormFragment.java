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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

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
        // Inflar el layout del diálogo
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_form, null);

        // Referenciar los campos de entrada
        List<Tesla.players> selectedPlayers = new ArrayList<>();
        editTextPlate = view.findViewById(R.id.edit_text_plate);
        editTextColor = view.findViewById(R.id.edit_text_color);
        editTextCountry = view.findViewById(R.id.edit_text_country);
        CheckBox checkBoxForeign = view.findViewById(R.id.checkbox_foreign);
        LinearLayout layoutCountry = view.findViewById(R.id.layout_country);
        Button buttonSelectPlayers = view.findViewById(R.id.button_select_players);

        // Manejar la visibilidad del campo país según el CheckBox
        checkBoxForeign.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layoutCountry.setVisibility(View.VISIBLE);
            } else {
                layoutCountry.setVisibility(View.GONE);
                editTextCountry.setText(""); // Limpiar el campo si se desmarca
            }
        });

        // Manejar la selección de jugadores
        buttonSelectPlayers.setOnClickListener(v -> {
            Tesla.players[] playersArray = Tesla.players.values();
            String[] playerNames = new String[playersArray.length];
            boolean[] checkedItems = new boolean[playersArray.length];

            // Llenar el array con los nombres de los jugadores
            for (int i = 0; i < playersArray.length; i++) {
                playerNames[i] = playersArray[i].name();
                if (playersArray[i] == Tesla.players.ERIKA) {
                    checkedItems[i] = true;  // ERIKA seleccionado por defecto
                    selectedPlayers.add(playersArray[i]);  // Agregar ERIKA a la lista de seleccionados
                }
            }

            AlertDialog.Builder playerDialog = new AlertDialog.Builder(requireContext());
            playerDialog.setTitle(R.string.player_hint);
            playerDialog.setMultiChoiceItems(playerNames, checkedItems, (dialog, which, isChecked) -> {
                if (isChecked) {
                    selectedPlayers.add(playersArray[which]);
                } else {
                    selectedPlayers.remove(playersArray[which]);
                }
            });
            playerDialog.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
            playerDialog.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
            playerDialog.show();
        });

        // Construir el diálogo con AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view)
                .setTitle(getString(R.string.add_button))
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.save), (dialog, which) -> {
                    // Recoger los datos introducidos
                    String plate = editTextPlate.getText().toString().trim();
                    String color = editTextColor.getText().toString().trim();
                    boolean isForeign = checkBoxForeign.isChecked();
                    String country = isForeign ? editTextCountry.getText().toString().trim() : null;

                    // Validación básica
                    if (plate.isEmpty()) {
                        Toast.makeText(getActivity(), getString(R.string.save_without_complete), Toast.LENGTH_SHORT).show();
                    } else {
                        // Crear un nuevo objeto Tesla y asignar los valores
                        Tesla newTesla = new Tesla();
                        newTesla.setPlate(plate);
                        newTesla.setColor(color);
                        newTesla.setForeign(isForeign);
                        newTesla.setCountry(isForeign ? country : null);
                        newTesla.setLastTimeSeen(LocalDate.now());
                        newTesla.setSeenBy(selectedPlayers); // Asignar los jugadores seleccionados

                        // Insertar en la base de datos a través del ViewModel
                        teslaViewModel.check(newTesla);

                        // Mensaje de confirmación al usuario
                        showConfirmationDialog();
                    }
                });

        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Obtener el TeslaViewModel del Activity que hospeda el diálogo.
        // Esto asegura que se comparta el mismo ViewModel con el resto de la aplicación (por ejemplo, para actualizar la lista).
        teslaViewModel = new ViewModelProvider(requireActivity()).get(TeslaViewModel.class);
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