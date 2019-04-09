package edu.uwi.sta.srts.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.Vehicle;
import edu.uwi.sta.srts.models.Vehicles;
import edu.uwi.sta.srts.views.OnListFragmentInteractionListener;
import edu.uwi.sta.srts.views.adapter.VehiclesAdapter;
import edu.uwi.sta.srts.views.admin.EditRoute;
import edu.uwi.sta.srts.views.admin.EditVehicle;

public class VehiclesFragment extends Fragment {

    private OnListFragmentInteractionListener listener;

    public VehiclesFragment() {}

    public static VehiclesFragment newInstance() {
        VehiclesFragment fragment = new VehiclesFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_layout, container, false);

        View empty = view.findViewById(R.id.empty);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        listener = new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Model model) {
                Intent intent = new Intent(getContext(), EditVehicle.class);
                intent.putExtra("vehicle", (Vehicle)model);
                startActivity(intent);
            }
        };

        VehiclesAdapter adapter = new VehiclesAdapter(new Vehicles(), listener, empty);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}

