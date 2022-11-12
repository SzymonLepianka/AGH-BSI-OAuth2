package Group28.OAuth.Controllers;

import Group28.OAuth.Model.Authorization;
import Group28.OAuth.Model.LogInUser;
import Group28.OAuth.Model.ValidateToken;
import Group28.OAuth.View.WebView;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@Controller
@RequestMapping(path = "/web", produces = MediaType.TEXT_HTML_VALUE)
public class WebController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static String getClientID(String accessToken) {

        String[] split_string = accessToken.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));

        String[] split = body.split(",");

        if (!split[0].startsWith("clientID", 2)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access Token should have 'clientID' instead of " + split[0].substring(2, 10));
        }
        return split[0].substring(12);
    }

    @GetMapping(value = "/login", params = "clientID")
    public String loginFormWithClientID(@RequestParam String clientID, HttpServletResponse httpServletResponse, Model model) {
        model.addAttribute("clientID", clientID);
        try {
            Authorization.Authorize(httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                return "loginForm";
            }
        }
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            var accessTokenCookie = WebUtils.getCookie(request, "AccessToken" + clientID);
            if (accessTokenCookie == null || !ValidateToken.validateToken(accessTokenCookie.getValue())) {
                var cookies = request.getCookies();
                for (var cookie : cookies) {
                    if (cookie.getName().startsWith("AccessToken") && ValidateToken.validateToken(cookie.getValue())) {
                        var modelResponse = LogInUser.handle(cookie.getValue(), clientID, passwordEncoder);
                        return WebView.LoginView(modelResponse, httpServletResponse);
                    }
                }
            } else {
                return "AlreadyLogged";
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/login")
    public String loginForm(HttpServletResponse httpServletResponse, Model model) {
        var clientID = "1";
        model.addAttribute("clientID", clientID);
        try {
            Authorization.Authorize(httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                return "loginForm";
            }
        }
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            var accessTokenCookie = WebUtils.getCookie(request, "AccessToken" + clientID);
            if (accessTokenCookie == null) {
                var cookies = request.getCookies();
                for (var cookie : cookies) {
                    if (cookie.getName().startsWith("AccessToken") && ValidateToken.validateToken(cookie.getValue())) {
                        var modelResponse = LogInUser.handle(cookie.getValue(), clientID, passwordEncoder);
                        System.out.println(modelResponse.content);
                        return WebView.LoginView(modelResponse, httpServletResponse);
                    }
                }
            } else {
                return "AlreadyLogged";
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/login", params ="clientID")
    public String handleLogin(@RequestParam String clientID, @RequestParam String username, @RequestParam String password, HttpServletResponse httpServletResponse) {
        try {
            var modelResponse = LogInUser.handle(username, password, clientID, passwordEncoder);
            return WebView.LoginView(modelResponse, httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpServletResponse httpServletResponse) {
        try {
            var modelResponse = LogInUser.handle(username, password, "1", passwordEncoder);
            return WebView.LoginView(modelResponse, httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
