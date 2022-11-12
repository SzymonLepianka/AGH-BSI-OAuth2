package bgs.oauth_server.model.State;

public class Response {

    public Object content;
    public String redirect;

    public Response(String redirect, Object content) {
        this.content = content;
        this.redirect = redirect;
    }
}