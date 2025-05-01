package com.sierrapor.teslatracker.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.sierrapor.teslatracker.R;
import com.sierrapor.teslatracker.data.TeslaViewModel;
import com.sierrapor.teslatracker.list.filter.FilterBottomSheetDialogFragment;
import com.sierrapor.teslatracker.list.filter.FilterOptions;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListFragment extends Fragment {

    private TeslaViewModel viewModel;
    private TeslaAdapter adapter;

    private String currentSearchQuery = "";
    private FilterOptions currentFilters = new FilterOptions();

    public ListFragment() {
        super(R.layout.fragment_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar ViewModel y RecyclerView
        viewModel = new ViewModelProvider(requireActivity()).get(TeslaViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new TeslaAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getFilteredTeslas().observe(getViewLifecycleOwner(), teslas -> {
            adapter.submitList(teslas);
        });

        // Configurar menú (Search + Filter)
        setupMenu();

        // Aplicar filtros iniciales
        applyCombinedFilters();
    }

    private void setupMenu() {
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
                inflater.inflate(R.menu.menu_list, menu);

                // Configurar búsqueda
                MenuItem searchItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) searchItem.getActionView();
                //TODO: añadir a @string
                searchView.setQueryHint("Buscar por matrícula");

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        currentSearchQuery = query;
                        applyCombinedFilters();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        currentSearchQuery = newText;
                        applyCombinedFilters();
                        return true;
                    }
                });

                // Configurar botón de filtros
                MenuItem filterItem = menu.findItem(R.id.action_filter);
                filterItem.setOnMenuItemClickListener(item -> {
                    showFilterDialog();
                    return true;
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                return false; // No hay acciones por defecto
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void showFilterDialog() {
        FilterBottomSheetDialogFragment dialog = new FilterBottomSheetDialogFragment();
        dialog.setFilterListener(filters -> {
            currentFilters = filters;
            applyCombinedFilters();
        });
        dialog.show(getChildFragmentManager(), "filters");
    }

    private void applyCombinedFilters() {
        viewModel.applySearchAndFilters(currentSearchQuery, currentFilters);
    }
}
