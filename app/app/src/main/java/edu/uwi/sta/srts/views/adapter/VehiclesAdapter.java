package edu.uwi.sta.srts.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.controllers.VehicleController;
import edu.uwi.sta.srts.controllers.VehiclesController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Route;
import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.Vehicles;
import edu.uwi.sta.srts.views.OnListFragmentInteractionListener;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.ViewHolder> implements edu.uwi.sta.srts.views.View {

    private final VehiclesController vehiclesController;
    private final OnListFragmentInteractionListener mListener;
    private final View empty;

    public VehiclesAdapter(Vehicles vehicles, OnListFragmentInteractionListener listener, View empty) {
        this.vehiclesController = new VehiclesController(vehicles, this);
        this.mListener = listener;
        this.empty = empty;
        if(vehicles.getVehicles().size()!=0){
            empty.setVisibility(View.GONE);
        }
    }

    @Override
    public VehiclesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.two_line_list_item, parent, false);
        return new VehiclesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VehiclesAdapter.ViewHolder holder, int position) {

        holder.vehicleController = vehiclesController.getVehicleController(position, null);

        holder.title.setText(holder.vehicleController.getVehicleLicensePlateNo());

        holder.meta.setText("No driver");

        holder.subtitle.setText("No Route");

        new UserController(new User(holder.vehicleController.getVehicleDriverId()), new DriverView(holder.meta));

        new RouteController(new Route(holder.vehicleController.getVehicleRouteId()), new RouteView(holder.subtitle));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.vehicleController.getModel());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehiclesController.getVehicles().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final TextView meta;
        public final TextView subtitle;
        public VehicleController vehicleController;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.vehicleController = null;
            title = (TextView) view.findViewById(R.id.title);
            subtitle = (TextView) view.findViewById(R.id.subtitle);
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

        if(((Vehicles)model).getVehicles().size() == 0){
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }
    }

    public class DriverView implements edu.uwi.sta.srts.views.View {

        TextView textView;

        public DriverView(TextView textView){
            this.textView = textView;
        }

        @Override
        public void update(Model model) {
            if(model instanceof User && textView != null){
                this.textView.setText(((User)model).getFullName());
            }
        }
    }

    public class RouteView implements edu.uwi.sta.srts.views.View {

        TextView textView;

        public RouteView(TextView textView){
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


