package edu.uwi.sta.srts.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.RouteController;
import edu.uwi.sta.srts.controllers.RoutesController;
import edu.uwi.sta.srts.controllers.StopsController;
import edu.uwi.sta.srts.controllers.VehiclesController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Routes;
import edu.uwi.sta.srts.models.Stop;
import edu.uwi.sta.srts.models.Stops;
import edu.uwi.sta.srts.models.Vehicles;
import edu.uwi.sta.srts.views.OnListFragmentInteractionListener;
import edu.uwi.sta.srts.views.fragments.RoutesFragment;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> implements edu.uwi.sta.srts.views.View {

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

    @Override
    public RoutesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.two_line_list_item, parent, false);
        return new RoutesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RoutesAdapter.ViewHolder holder, int position) {

        holder.routeController = routesController.getRouteController(position, null);

        holder.title.setText(holder.routeController.getRouteName());

        holder.meta.setText(String.valueOf(holder.routeController.getRouteFrequency()) + " mins");

        new VehiclesController(new Vehicles(holder.routeController.getRouteId()), new VehiclesView(holder.subtitle));

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

        if(((Routes)model).getRoutes().size() == 0){
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }
    }

    public class VehiclesView implements edu.uwi.sta.srts.views.View {

        TextView textView;

        public VehiclesView(TextView textView){
            this.textView = textView;
        }

        @Override
        public void update(Model model) {
            if(model instanceof Vehicles && textView != null){
                this.textView.setText(((Vehicles)model).getVehicles().size() + " shuttles");
            }
        }
    }
}
