/*
 * Copyright (c) 2019. Razor Sharp Software Solutions
 *
 * Azel Daniel (816002285)
 * Amanda Seenath (816002935)
 * Michael Bristol (816003612)
 *
 * INFO 3604
 * Project
 *
 * UWI Shuttle Routing and Tracking System
 */

package edu.uwi.sta.srts.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Shuttles;
import edu.uwi.sta.srts.utils.OnListFragmentInteractionListener;
import edu.uwi.sta.srts.views.adapter.ShuttlesAdapter;
import edu.uwi.sta.srts.views.ViewShuttle;

public class ShuttlesFragment extends Fragment {

    private OnListFragmentInteractionListener listener;

    private boolean isAdmin;

    public ShuttlesFragment() {}

    public static ShuttlesFragment newInstance(boolean isAdmin) {
        ShuttlesFragment fragment =  new ShuttlesFragment();
        fragment.isAdmin = isAdmin;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_layout, container, false);

        View empty = view.findViewById(R.id.empty);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        this.listener = new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Model model) {
                Intent intent = new Intent(getContext(), ViewShuttle.class);
                intent.putExtra("shuttle", model);
                intent.putExtra("isAdmin", isAdmin);
                startActivity(intent);
            }
        };

        Shuttles shuttles = new Shuttles();
        if(!isAdmin){
            shuttles.filter(true);
        }
        ShuttlesAdapter adapter = new ShuttlesAdapter(shuttles, listener, empty);
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
