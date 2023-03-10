package com.internship.usertracking.Entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Activity {

    private String geolocation;

    private LocalDateTime loginTime;

    private String osName;

    private String browserName;

    private Long sesstionTime;

    public Activity() {
    }

    public Activity(String geolocation, LocalDateTime loginTime, String osName, String browserName) {
        this.geolocation = geolocation;
        this.loginTime = loginTime;
        this.osName = osName;
        this.browserName = browserName;
        this.sesstionTime = 0l;
    }

    public Activity(String geolocation, LocalDateTime loginTime, String osName, String browserName, Long sesstionTime) {
        this.geolocation = geolocation;
        this.loginTime = loginTime;
        this.osName = osName;
        this.browserName = browserName;
        this.sesstionTime = sesstionTime;
    }
}
