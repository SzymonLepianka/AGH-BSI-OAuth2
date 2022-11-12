package Group28.OAuth.Model.State;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.Map;

public class Failure extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("Failure");

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Other failure (Failure)");
    }

    @Override
    public String toString() {
        return "Failure";
    }

}
