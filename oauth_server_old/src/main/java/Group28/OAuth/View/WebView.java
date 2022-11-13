package Group28.OAuth.View;

import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Model.State.Response;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class WebView {

    public static String LoginView(Response modelResponse, HttpServletResponse httpServletResponse) {
        var authCode = (AuthCode) modelResponse.content;
        var cookieAuthCode = new Cookie("AuthCode", authCode.getContent());
        cookieAuthCode.setPath("/");
        httpServletResponse.addCookie(cookieAuthCode);
        return "AlreadyLogged";
    }
}
