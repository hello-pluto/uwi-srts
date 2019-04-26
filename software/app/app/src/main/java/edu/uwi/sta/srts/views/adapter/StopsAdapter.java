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
import edu.uwi.sta.srts.controllers.StopController;
import edu.uwi.sta.srts.controllers.StopsController;
import edu.uwi.sta.srts.utils.Model;
import edu.uwi.sta.srts.models.Stops;
import edu.uwi.sta.srts.utils.OnListFragmentInteractionListener;

public class StopsAdapter extends RecyclerView.Adapter<StopsAdapter.ViewHolder> implements edu.uwi.sta.srts.utils.View {

    private final StopsController stopsController;
    private final OnListFragmentInteractionListener mListener;
    private final View empty;

    public StopsAdapter(Stops stops, OnListFragmentInteractionListener listener, View empty) {
        this.stopsController = new StopsController(stops, this);
        this.mListener = listener;
        this.empty = empty;
        if(stops.getStops().size()!=0){
            empty.setVisibility(View.GONE);
        }
    }

    @Override @NonNull
    public StopsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_line_list_item, parent, false);
        return new StopsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StopsAdapter.ViewHolder holder, int position) {

        holder.stopController = stopsController.getStopController(position, null);

        holder.title.setText(holder.stopController.getStopName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.stopController.getModel());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stopsController.getStops().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public StopController stopController;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.stopController = null;
            this.title = (TextView) view.findViewById(R.id.title);
            view.findViewById(R.id.img).setVisibility(View.GONE);
        }

        @Override @NonNull
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }

    @Override
    public void update(Model model) {
        this.notifyDataSetChanged();

        if(((Stops)model).getStops().size() == 0){
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }
    }
}


