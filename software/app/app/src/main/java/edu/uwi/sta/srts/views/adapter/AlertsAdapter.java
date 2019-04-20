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

package edu.uwi.sta.srts.views.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.AlertController;
import edu.uwi.sta.srts.controllers.AlertsController;
import edu.uwi.sta.srts.models.Alerts;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.utils.Utils;
import edu.uwi.sta.srts.utils.OnListFragmentInteractionListener;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.ViewHolder> implements edu.uwi.sta.srts.views.View {

    private final AlertsController alertsController;
    private final OnListFragmentInteractionListener mListener;
    private final View empty;

    public AlertsAdapter(Alerts alerts, OnListFragmentInteractionListener listener, View empty) {
        this.alertsController = new AlertsController(alerts, this);
        this.mListener = listener;
        this.empty = empty;
        if(alerts.getAlerts().size()!=0){
            empty.setVisibility(View.GONE);
        }
    }

    @Override @NonNull
    public AlertsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_line_list_item, parent, false);
        return new AlertsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlertsAdapter.ViewHolder holder, int position) {

        holder.alertController = alertsController.getAlertController(position, null);

        holder.title.setText(holder.alertController.getAlertTitle());

        holder.img.setBorderColor(Utils.getUrgencyColor(holder.alertController.getAlertUrgency()));
        holder.img.setCircleBackgroundColor(Utils.getUrgencyColor(holder.alertController.getAlertUrgency()));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.alertController.getModel());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alertsController.getAlerts().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final CircleImageView img;
        public AlertController alertController;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.alertController = null;
            this.img = view.findViewById(R.id.img);
            this.title = view.findViewById(R.id.title);
        }

        @Override @NonNull
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }

    @Override
    public void update(Model model) {
        this.notifyDataSetChanged();

        if(((Alerts)model).getAlerts().size() == 0){
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }
    }
}
