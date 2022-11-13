package Group28.OAuth.Model.State;

import java.sql.SQLException;
import java.util.Map;

public abstract class State {

    public abstract Response handle(Context context, Map<String, String> params) throws SQLException;

    public abstract String toString();
}

