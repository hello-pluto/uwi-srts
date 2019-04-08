package edu.uwi.sta.srts.controllers;

import java.util.ArrayList;

import edu.uwi.sta.srts.models.User;
import edu.uwi.sta.srts.models.Users;
import edu.uwi.sta.srts.views.View;

public class UsersController extends Controller {

    /**
     * Constructor that requires the users model and its corresponding view
     * @param model The list of user models
     * @param view The corresponding view
     */
    public UsersController(Users model, View view) {
        super(model, view);
    }

    public ArrayList<User> getUsers() {
        return ((Users)this.model).getUsers();
    }

    public void addUser(User user){
        ((Users)this.model).addUser(user);
    }

    public void removeUser(int index){
        ((Users)this.model).removeUser(index);
    }

    public User filter(String userId){
        for(User user: getUsers()){
            if(user.getId().equals(userId)){
                return user;
            }
        }
        return null;
    }

    public UserController getUserController(int index, View view){
        return new UserController(getUsers().get(index), view);
    }
}
