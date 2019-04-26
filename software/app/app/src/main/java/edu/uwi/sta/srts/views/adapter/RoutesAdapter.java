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
import edu.uwi.sta.srts.controllers.RoutesController;
import edu.uwi.sta.srts.controllers.ShuttlesController;
import edu.uwi.sta.srts.utils.Model;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.models.Shuttle;
import edu.uwi.sta.srts.models.Shuttles;
import edu.uwi.sta.srts.utils.OnListFragmentInteractionListener;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> implements edu.uwi.sta.srts.utils.View {

    private final RoutesController routesController;
    private final OnListFragmentInteractionListener mListener;
    private final View empty;

    public RoutesAdapter(Routes routes, OnListFragmentInteractionListener listener, View empty) {
        this.routesController = new RoutesController(routes, this);
        this.mListener = listener;
        this.empty = empty;
        if(routes.getRoutes().size()!=0){
            empty.setVisibility(View.GONE);
        }
    }

    @Override @NonNull
    public RoutesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.two_line_list_item, parent, false);
        return new RoutesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RoutesAdapter.ViewHolder holder, int position) {

        holder.routeController = routesController.getRouteController(position, null);

        holder.title.setText(holder.routeController.getRouteName());

        holder.meta.setText(String.valueOf(String.valueOf(holder.routeController.getRouteFrequency()) + " mins"));

        new ShuttlesController(new Shuttles(holder.routeController.getRouteId()), new ShuttlesView(holder.subtitle));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.routeController.getModel());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return routesController.getRoutes().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final TextView subtitle;
        public final TextView meta;
        public RouteController routeController;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.routeController = null;
            this.title = view.findViewById(R.id.title);
            this.subtitle = view.findViewById(R.id.subtitle);
            this.meta = view.findViewById(R.id.meta);
        }

        @Override @NonNull
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }

    @Override
    public void update(Model model) {
        this.notifyDataSetChanged();

        if(((Routes)model).getRoutes().size() == 0){
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }
    }

    public class ShuttlesView implements edu.uwi.sta.srts.utils.View {

        TextView textView;

        private ShuttlesView(TextView textView){
            this.textView = textView;
        }

        @Override
        public void update(Model model) {
            if(model instanceof Shuttles && textView != null){
                int numOnDutyShuttles = 0;
                for(Shuttle shuttle: ((Shuttles)model).getShuttles() ){
                    if(shuttle.isOnDuty()){
                        numOnDutyShuttles++;
                    }
                }
                if(numOnDutyShuttles == 0){
                    this.textView.setText("No on duty shuttles");
                }else if(numOnDutyShuttles == 1){
                    this.textView.setText("1 on duty shuttle");
                }else{
                    this.textView.setText(numOnDutyShuttles + " on duty shuttles");
                }
            }
        }
    }
}
