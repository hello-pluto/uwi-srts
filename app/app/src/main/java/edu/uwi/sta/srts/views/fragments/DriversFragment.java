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
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.Users;
import edu.uwi.sta.srts.models.utils.UserType;
import edu.uwi.sta.srts.views.OnListFragmentInteractionListener;
import edu.uwi.sta.srts.views.adapter.UsersAdapter;
import edu.uwi.sta.srts.views.EditUser;

public class DriversFragment extends Fragment {

    private OnListFragmentInteractionListener listener;

    public DriversFragment() {}

    public static DriversFragment newInstance() {
        DriversFragment fragment = new DriversFragment();
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

        Users drivers = new Users();
        drivers.filterSelf(UserType.DRIVER);

        listener = new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Model model) {
                Intent intent = new Intent(getContext(), EditUser.class);
                intent.putExtra("user", (User)model);
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
