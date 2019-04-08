package edu.uwi.sta.srts.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.uwi.sta.srts.R;
import edu.uwi.sta.srts.controllers.UserController;
import edu.uwi.sta.srts.controllers.UsersController;
import edu.uwi.sta.srts.models.Model;
import edu.uwi.sta.srts.models.Users;
import edu.uwi.sta.srts.views.OnListFragmentInteractionListener;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> implements edu.uwi.sta.srts.views.View {

    private final UsersController usersController;
    private final OnListFragmentInteractionListener mListener;
    private final View empty;

    public UsersAdapter(Users users, OnListFragmentInteractionListener listener, View empty) {
        this.usersController = new UsersController(users, this);
        this.mListener = listener;
        this.empty = empty;
        if(users.getUsers().size()!=0){
            empty.setVisibility(View.GONE);
        }
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_line_list_item, parent, false);
        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UsersAdapter.ViewHolder holder, int position) {

        holder.userController = usersController.getUserController(position, null);

        holder.title.setText(holder.userController.getUserFullName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.userController.getModel());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersController.getUsers().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final TextView meta;
        public final ImageView avatar;
        public UserController userController;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.userController = null;
            title = (TextView) view.findViewById(R.id.title);
            avatar = (ImageView) view.findViewById(R.id.avatar);
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

        if(((Users)model).getUsers().size() == 0){
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }
    }
}

