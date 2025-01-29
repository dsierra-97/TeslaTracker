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
                // Reiniciar la actividad correctamente
                Intent intent = requireActivity().getIntent();
                requireActivity().finish();
                requireActivity().startActivity(intent);
                return true;
            });
        }
    }

}