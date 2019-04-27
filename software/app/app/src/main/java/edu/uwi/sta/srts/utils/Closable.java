package edu.uwi.sta.srts.utils;

import edu.uwi.sta.srts.models.User;

public interface Closable {
    void close(User user);
}