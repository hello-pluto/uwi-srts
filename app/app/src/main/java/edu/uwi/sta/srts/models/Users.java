package edu.uwi.sta.srts.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.utils.DatabaseHelper;
import edu.uwi.sta.srts.models.utils.UserType;

public class Users extends Model {

    private ArrayList<User> users =  new ArrayList<>();
    private UserType filter = null;

    /**
     * Default constructor that fetches all users from the database
     */
    public Users() {
        super();
        DatabaseHelper.getInstance().getDatabaseReference("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        users.clear();
                        for (DataSnapshot user: dataSnapshot.getChildren()) {
                            User u = user.getValue(User.class);
                            if(filter != null && u.getUserType() == filter){
                                users.add(u);
                            }
                        }

                        notifyObservers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Constructor that accepts a local collection of users
     * @param users The collection of users
     */
    public Users(ArrayList<User> users){
        super();
        this.users.addAll(users);
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public void addUser(User user){
        this.users.add(user);
    }

    public void removeUser(int index){
        this.users.remove(index);
    }

    public ArrayList<User> filterSelf(UserType userType){

        filter = userType;

        ArrayList<User> users = new ArrayList<>();
        for(User user: getUsers()){
            if(user.getUserType() == userType){
                users.add(user);
            }
        }

        this.users.clear();
        this.users.addAll(users);

        return users;
    }

    @Override
    public void save() {
        for(User user: this.getUsers()){
            user.save();
        }
    }
}
