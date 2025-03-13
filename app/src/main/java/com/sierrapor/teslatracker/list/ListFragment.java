package com.sierrapor.teslatracker.list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sierrapor.teslatracker.R;
import com.sierrapor.teslatracker.data.TeslaViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListFragment extends Fragment {
    private TeslaViewModel teslaViewModel;
    private TeslaAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TeslaAdapter();
        recyclerView.setAdapter(adapter);

        teslaViewModel = new ViewModelProvider(this).get(TeslaViewModel.class);
        teslaViewModel.getAllTeslas().observe(getViewLifecycleOwner(), teslas -> {
            adapter.setTeslaList(teslas);
        });

        return view;
    }
}