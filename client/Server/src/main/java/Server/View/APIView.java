package Server.View;

import Server.Domain.User;
import org.json.JSONObject;

public class APIView {
    public String getUserData(User user) {
        var userJSON = new JSONObject();
        userJSON.put("user_id", user.getId());
        userJSON.put("email", user.getEmail());
        userJSON.put("first_name", user.getFirstName());
        userJSON.put("surname", user.getSurname());
        userJSON.put("username", user.getUsername());
        return userJSON.toString();
    }
}

