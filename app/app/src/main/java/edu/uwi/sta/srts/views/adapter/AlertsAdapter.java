package edu.uwi.sta.srts.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.AlertController;
import edu.uwi.sta.srts.controllers.AlertsController;
import edu.uwi.sta.srts.models.Alert;
import edu.uwi.sta.srts.models.Alerts;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Users;
import edu.uwi.sta.srts.views.OnListFragmentInteractionListener;

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

    @Override
    public AlertsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_line_list_item, parent, false);
        return new AlertsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlertsAdapter.ViewHolder holder, int position) {

        holder.alertController = alertsController.getAlertController(position, null);

        holder.title.setText(holder.alertController.getAlertTitle());

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
        public final TextView meta;
        public AlertController alertController;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.alertController = null;
            title = (TextView) view.findViewById(R.id.title);
            meta = (TextView) view.findViewById(R.id.meta);
        }

        @Override
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
