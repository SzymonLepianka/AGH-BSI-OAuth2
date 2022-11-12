package bgs.oauth_server.model.State;

import java.sql.*;
import java.util.*;

public abstract class State {

    public abstract Response handle(Context context, Map<String, String> params) throws SQLException;

    public abstract String toString();
}

