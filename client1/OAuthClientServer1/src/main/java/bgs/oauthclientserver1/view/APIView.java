package bgs.oauthclientserver1.view;

import bgs.oauthclientserver1.domain.*;
import org.json.*;

public class APIView {
    public String getUserData(User user) {
        var userJSON = new JSONObject();
        userJSON.put("user_id", user.getUserId());
        userJSON.put("email", user.getEmail());
        userJSON.put("first_name", user.getFirstName());
        userJSON.put("surname", user.getSurname());
        userJSON.put("username", user.getUsername());
        userJSON.put("birth_date", user.getBirthDate());
        return userJSON.toString();
    }
}

