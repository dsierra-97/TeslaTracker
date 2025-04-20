package com.sierrapor.teslatracker.detail;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

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

    private EditText editPlate, editColor, editCountry, editNumber, editFirst, editLast;
    private Button buttonEdit, buttonSave;
    private LinearLayout layoutCountry;
    private boolean isEditMode = false;
    private TeslaViewModel viewModel;
    private Tesla tesla;
    private CheckBox checkboxForeign;
    private Spinner spinnerPlayers;
    private ChipGroup selectedPlayersContainer;
    private final List<Tesla.players> selectedPlayers = new ArrayList<>();
    private LinearLayout playerSelectionLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        viewModel = new ViewModelProvider(this).get(TeslaViewModel.class);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_delete) {
                    showDeleteDialog();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        // Obtención de referencias del layout.
        editPlate = view.findViewById(R.id.edit_plate);
        editColor = view.findViewById(R.id.edit_color);
        layoutCountry = view.findViewById(R.id.layout_country);
        editCountry = view.findViewById(R.id.edit_country);
        editNumber = view.findViewById(R.id.edit_number);
        editFirst = view.findViewById(R.id.edit_first);
        editLast = view.findViewById(R.id.edit_last);
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
            if (editPlate.getText().toString().isEmpty() && editColor.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.save_without_complete_plate_and_color), Toast.LENGTH_SHORT).show();
            } else if (editPlate.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.save_without_complete_plate), Toast.LENGTH_SHORT).show();
            }else if (editColor.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.save_without_complete_color), Toast.LENGTH_SHORT).show();
            } else if (editPlate.getText().toString().length() > 10) {
                Toast.makeText(getActivity(), getString(R.string.plate_too_long), Toast.LENGTH_SHORT).show();
            } else if (editNumber.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.save_without_complete_number), Toast.LENGTH_SHORT).show();
            } else {
                saveChanges();
                lockFields();
                isEditMode = false;
                playerSelectionLayout.setVisibility(View.GONE);
                updateChips();
                buttonEdit.setVisibility(View.VISIBLE);
                buttonSave.setVisibility(View.GONE);
            }
        });

        setupPlayerSpinner();

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
                requireContext(), R.layout.item_spinner_player, spinnerItems
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
        adapter.setDropDownViewResource(R.layout.item_spinner_player);
        return adapter;
    }

    private void showData() {
        editPlate.setText(tesla.getPlate());
        editColor.setText(tesla.getColor());
        editCountry.setText(tesla.getCountry());
        editNumber.setText(String.valueOf(tesla.getNumberTimesSeen()));
        editFirst.setText(String.valueOf(tesla.getFirstTimeSeen()));
        editLast.setText(String.valueOf(tesla.getLastTimeSeen()));
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
        editFirst.setEnabled(false);
        editLast.setEnabled(false);
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
        viewModel.edit(tesla);
    }

    private void updateChips() {
        selectedPlayersContainer.removeAllViews();

        for (Tesla.players player : selectedPlayers) {
            Chip chip = new Chip(requireContext());
            chip.setText(player.name());
            chip.setChipBackgroundColor(ContextCompat.getColorStateList(requireContext(), R.color.colorBackground));
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnBackground));
            chip.setCloseIconVisible(true);
            chip.setClickable(isEditMode);
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

    private void showDeleteDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_confirmation_title)
                .setMessage(R.string.delete_confirmation_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteTesla())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void deleteTesla() {
        viewModel.delete(tesla);
        //Borra la pila (para que Atras no funcione)
        new NavOptions.Builder().setPopUpTo(R.id.listFragment, true) // limpia hasta listFragment, inclusive
                .build();

        //Navega a listFragment para no mostrar un tesla ya eliminado
        NavHostFragment.findNavController(this).navigate(
                R.id.listFragment,
                null,
                new NavOptions.Builder()
                        .setPopUpTo(R.id.detailFragment, true) // o listFragment, según lo que quieras limpiar
                        .build()
        );
        //Muestra a polar :)
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_delete_confirmation);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(true);

        ImageView imageView = dialog.findViewById(R.id.image_delete_confirmation);
        imageView.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}

