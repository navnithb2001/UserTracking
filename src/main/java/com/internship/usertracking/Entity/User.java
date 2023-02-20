package com.internship.usertracking.Entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Document(collection = "user")
public class User {

    @Id
    private String username;

    private String password;

    private List<Activity> activities;



    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.activities = new ArrayList<Activity>();
    }

    public User(String username, String password, List<Activity> activities) {
        this.username = username;
        this.password = password;
        this.activities = activities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActivity(Activity activity) {
        this.activities.add(activity);
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}

