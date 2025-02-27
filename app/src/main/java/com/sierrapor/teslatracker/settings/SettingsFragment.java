package com.sierrapor.teslatracker.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.sierrapor.teslatracker.R;


public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        ListPreference languagePreference = findPreference("language");
        if (languagePreference != null) {
            languagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                LocaleHelper.setLocale(requireContext(), newValue.toString());
/*              TODO: Decidir que hacer con esta puta mierda
                Estas lineas comentadas solucionan el bug de no actualizar la configuración
º               al cambiar el idioma, pero por otra parte reinician la actividad al completo y
                te llevan al fragmento de inicio, que de momento es logros pero que será form

                Intent intent = requireActivity().getIntent();
                 requireActivity().finish();
                requireActivity().startActivity(intent);*/

                requireActivity().recreate();

                return true;
            });
        }
    }

}