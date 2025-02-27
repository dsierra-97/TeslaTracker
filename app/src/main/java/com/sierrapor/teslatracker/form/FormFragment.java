package com.sierrapor.teslatracker.form;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.sierrapor.teslatracker.R;
import com.sierrapor.teslatracker.data.Tesla;
import com.sierrapor.teslatracker.data.TeslaViewModel;

public class FormFragment extends DialogFragment {

    private EditText editTextPlate;
    private EditText editTextColor;
    private TeslaViewModel teslaViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflar el layout del diálogo
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_form, null);

        // Referenciar los campos de entrada
        editTextPlate = view.findViewById(R.id.edit_text_plate);
        editTextColor = view.findViewById(R.id.edit_text_color);

        // Construir el diálogo con AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view)
                .setTitle(getString(R.string.add_button)) // Aquí se usa getString()
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                    // Simplemente se cierra el diálogo
                    dialog.cancel();
                })
                .setPositiveButton(getString(R.string.save), (dialog, which) -> {
                    // Recoger los datos introducidos
                    String plate = editTextPlate.getText().toString().trim();
                    String color = editTextColor.getText().toString().trim();
                    // Validación básica: asegurarse de que los campos no estén vacíos
                    if (plate.isEmpty() || color.isEmpty()) {
                        Toast.makeText(getActivity(), getString(R.string.save_without_complete), Toast.LENGTH_SHORT).show();
                    } else {
                        // Crear un nuevo objeto Tesla y asignar los valores
                        Tesla newTesla = new Tesla();
                        newTesla.setPlate(plate);
                        newTesla.setColor(color);

                        // Insertar en la base de datos a través del ViewModel
                        teslaViewModel.insert(newTesla);
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
}