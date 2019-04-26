/*
 * Copyright (c) 2019. Razor Sharp Software Solutions
 *
 * Azel Daniel (816002285)
 * Michael Bristol (816003612)
 * Amanda Seenath (816002935)
 *
 * INFO 3604
 * Project
 *
 * UWI Shuttle Routing and Tracking System
 */

package edu.uwi.sta.srts.views.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.controllers.ShuttleController;
import edu.uwi.sta.srts.controllers.ShuttlesController;
import edu.uwi.sta.srts.utils.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.Shuttles;
import edu.uwi.sta.srts.utils.OnListFragmentInteractionListener;

public class ShuttlesAdapter extends RecyclerView.Adapter<ShuttlesAdapter.ViewHolder> implements edu.uwi.sta.srts.utils.View {

    private final ShuttlesController shuttlesController;
    private final OnListFragmentInteractionListener mListener;
    private final View empty;

    public ShuttlesAdapter(Shuttles shuttles, OnListFragmentInteractionListener listener, View empty) {
        this.shuttlesController = new ShuttlesController(shuttles, this);
        this.mListener = listener;
        this.empty = empty;
        if(shuttles.getShuttles().size()!=0){
            empty.setVisibility(View.GONE);
        }
    }

    @Override @NonNull
    public ShuttlesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.two_line_list_item, parent, false);
        return new ShuttlesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShuttlesAdapter.ViewHolder holder, int position) {

        holder.shuttleController = shuttlesController.getShuttleController(position, null);

        holder.title.setText(holder.shuttleController.getShuttleLicensePlateNo());

        holder.meta.setText("No driver");

        holder.subtitle.setText("No Route");

        new UserController(new User(holder.shuttleController.getShuttleDriverId()), new DriverView(holder.meta));

        new RouteController(new Route(holder.shuttleController.getShuttleRouteId()), new RouteView(holder.subtitle));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.shuttleController.getModel());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shuttlesController.getShuttles().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final TextView meta;
        public final TextView subtitle;
        public ShuttleController shuttleController;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.shuttleController = null;
            title = view.findViewById(R.id.title);
            subtitle = view.findViewById(R.id.subtitle);
            meta = view.findViewById(R.id.meta);
        }

        @Override @NonNull
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }

    @Override
    public void update(Model model) {
        this.notifyDataSetChanged();

        if(((Shuttles)model).getShuttles().size() == 0){
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }
    }

    public class DriverView implements edu.uwi.sta.srts.utils.View {

        private final TextView textView;

        private DriverView(TextView textView){
            this.textView = textView;
        }

        @Override
        public void update(Model model) {
            if(model instanceof User && textView != null){
                this.textView.setText(((User)model).getFullName());
            }
        }
    }

    public class RouteView implements edu.uwi.sta.srts.utils.View {

        private final TextView textView;

        private RouteView(TextView textView){
            this.textView = textView;
        }

        @Override
        public void update(Model model) {
            if(model instanceof Route && textView != null){
                this.textView.setText(((Route)model).getName());
            }
        }
    }
}


