package com.sierrapor.teslatracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sierrapor.teslatracker.data.TeslaViewModel;
import com.sierrapor.teslatracker.form.FormFragment;

import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        TeslaViewModel teslaViewModel = new ViewModelProvider(this).get(TeslaViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_view);

        //Se obtiene el NavController manualmente ya que daba error al usar R.id.nav_host_fragment no se encontraba el navHostFragment
        NavController navController = NavHostFragment.findNavController((NavHostFragment) (getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)));
        NavigationUI.setupWithNavController(bottomNavView, navController);

        // Escuchar los cambios de destino y actualizar el título
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Aquí puedes actualizar el título según el fragmento actual
            String fragmentTitle = destination.getLabel().toString();
            getSupportActionBar().setTitle(fragmentTitle);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Encuentra el FloatingActionButton
        FloatingActionButton fabAddTesla = findViewById(R.id.fab_add_tesla);

        // Configura el listener para el botón flotante
        fabAddTesla.setOnClickListener(view -> {
            // Crea una instancia del DialogFragment
            FormFragment dialogFragment = new FormFragment();

            // Muestra el DialogFragment usando el FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();
            dialogFragment.show(fragmentManager, "AddTeslaDialog");
        });

        // Leer preferencia de modo oscuro
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(newBase);
        String lang = prefs.getString("language", "es");
        Context context = updateLocale(newBase, lang);
        super.attachBaseContext(context);
    }

    private Context updateLocale(Context context, String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }
}