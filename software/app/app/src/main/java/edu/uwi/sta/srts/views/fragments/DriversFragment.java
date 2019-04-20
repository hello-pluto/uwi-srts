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
import edu.uwi.sta.srts.models.Users;
import edu.uwi.sta.srts.utils.OnListFragmentInteractionListener;
import edu.uwi.sta.srts.utils.UserType;
import edu.uwi.sta.srts.views.ViewUser;
import edu.uwi.sta.srts.views.adapter.UsersAdapter;

public class DriversFragment extends Fragment {

    private OnListFragmentInteractionListener listener;

    public DriversFragment() {}

    public static DriversFragment newInstance() {
        return new DriversFragment();
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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        Users drivers = new Users();
        drivers.filter(UserType.DRIVER);

        this.listener = new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Model model) {
                Intent intent = new Intent(getContext(), ViewUser.class);
                intent.putExtra("user", model);
                intent.putExtra("isAdmin", true);
                startActivity(intent);
            }
        };

        UsersAdapter adapter = new UsersAdapter(drivers, listener, empty);
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
