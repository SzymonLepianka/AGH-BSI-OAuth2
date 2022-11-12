package bgs.oauth_server.view;

import bgs.oauth_server.domain.*;
import bgs.oauth_server.model.State.*;


import javax.servlet.http.*;

public class WebView {

    public static String LoginView(Response modelResponse, HttpServletResponse httpServletResponse) {
        var authCode = (AuthCode) modelResponse.content;
        var cookieAuthCode = new Cookie("AuthCode", authCode.getContent());
        cookieAuthCode.setPath("/");
        httpServletResponse.addCookie(cookieAuthCode);
        return "AlreadyLogged";
    }
}
