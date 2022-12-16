package bgs.oauth_client_server.view;

import bgs.oauth_client_server.domain.*;
import org.json.*;

import java.text.*;

public class APIView {
    public String getUserData(User user) {
        var userJSON = new JSONObject();
        userJSON.put("user_id", user.getUserId());
        userJSON.put("email", user.getEmail());
        userJSON.put("first_name", user.getFirstName());
        userJSON.put("surname", user.getSurname());
        userJSON.put("username", user.getUsername());

        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
        userJSON.put("birth_date", ymdFormat.format(user.getBirthDate()));
        return userJSON.toString();
    }
}

