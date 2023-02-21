package com.internship.usertracking.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.internship.usertracking.Entity.Activity;
import com.internship.usertracking.Entity.User;
import com.internship.usertracking.Jwt.JwtTokenUtil;
import com.internship.usertracking.Repository.UserRepository;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public String loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (user.getActivities() == null) {
            user.setActivities(new ArrayList<>());
        }

        User loginUser = new User(user.getUsername(), user.getPassword(), user.getActivities());
        Activity activity = new Activity(getLocationFromIP(), LocalDateTime.now(), getOSName(this.request),
                getBrowserName(this.request));
        loginUser.setActivity(activity);

        userRepository.save(loginUser);

        String token = jwtTokenUtil.generateToken(user);
        return token;
    }

    private String getLocationFromIP() {
        try {
            URL url = new URL("https://ipapi.co/json/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            String city = json.getString("city");
            String region = json.getString("region");
            String country = json.getString("country_name");

            return city + ", " + region + ", " + country;
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private String getOSName(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem os = userAgent.getOperatingSystem();

        return os.getName();
    }

    private String getBrowserName(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();

        return browser.getName();
    }

    @Override
    public List<Activity> getAllUserActivities() {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            username = jwtTokenUtil.extractUsername(jwtToken);
        }
        User user = userRepository.findByUsername(username);
        List<Activity> activities = user.getActivities();
        LocalDateTime loginTime = activities.get(activities.size()-1).getLoginTime();
        activities.get(activities.size()-1).setSesstionTime(Duration.between(loginTime, LocalDateTime.now()).toMinutes());
        user.setActivities(activities);
        userRepository.save(user);
        return activities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }
}
