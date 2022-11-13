package bgs.oauth_server.model.State;

import java.sql.*;
import java.util.*;

public interface State {
    Response handle(Context context, Map<String, String> params) throws SQLException;

    String toString();
}

