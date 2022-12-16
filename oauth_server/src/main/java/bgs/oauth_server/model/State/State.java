package bgs.oauth_server.model.State;

import java.util.*;

public interface State {
    Response handle(Map<String, String> params) throws Exception;

    String toString();
}

