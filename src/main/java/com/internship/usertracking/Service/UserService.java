package com.internship.usertracking.Service;

import java.util.List;

import com.internship.usertracking.Entity.User;
import com.internship.usertracking.Entity.Activity;

public interface UserService {

    void registerUser(User user);

    String loginUser(String username, String password);

    List<Activity> getAllUserActivities(String username);
}
