package bgs.oauth_server.controllers;

import bgs.oauth_server.domain.*;
import bgs.oauth_server.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.server.*;
import org.springframework.web.util.*;

import javax.servlet.http.*;
import java.util.*;

@Controller
@RequestMapping(path = "/web", produces = MediaType.TEXT_HTML_VALUE)
public class WebController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogInUser logInUser;
    @Autowired
    private ValidateToken validateToken;
    @Autowired
    private Authorization authorization;

    @GetMapping(value = "/login", params = "clientID")
    @ResponseBody
    public ResponseEntity<String> handleLoginWithClientID(@RequestParam String clientID, HttpServletResponse httpServletResponse, Model model) {
        model.addAttribute("clientID", clientID);
        System.out.println("kontroler: loginFormWithClientID (clientID=" + clientID + ")");

        // autoryzacja
        if (!authorization.authorizeOnCookie()) {
            //TODO change status code
            return new ResponseEntity<>("loginForm", HttpStatus.OK);
        }

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            var accessTokenCookie = WebUtils.getCookie(request, "AccessToken" + clientID);
            if (accessTokenCookie == null || !validateToken.validateToken(accessTokenCookie.getValue())) {
                var cookies = request.getCookies();
                for (var cookie : cookies) {
                    if (cookie.getName().startsWith("AccessToken") && validateToken.validateToken(cookie.getValue())) {
                        var modelResponse = logInUser.handleLoginWithClientID(cookie.getValue(), clientID);
                        System.out.println("Tworzenie nowego AuthCode");

                        // dodaje cookie do response
                        var authCode = (AuthCode) modelResponse.content;
//                        var cookieAuthCode = new Cookie("AuthCode", authCode.getContent());
//                        cookieAuthCode.setPath("/");
//                        httpServletResponse.addCookie(cookieAuthCode);

                        // zamiast strony "alreadyLogged" zwraca AuthCode
                        return new ResponseEntity<>(authCode.getContent(), HttpStatus.OK);
//                        return WebView.LoginView(modelResponse, httpServletResponse);
                    }
                }
            } else {
                System.out.println("Token się zgadza, powrót bez tworzenia nowego");
                return new ResponseEntity<>("AlreadyLogged", HttpStatus.OK);
            }
            return new ResponseEntity<>("Brak autoryzacji (loginFormWithClientID)", HttpStatus.UNAUTHORIZED);
        } catch (ResponseStatusException rse) {
            rse.printStackTrace();
            return new ResponseEntity<>(rse.getReason(), rse.getStatus());
        }
    }

    @PostMapping(value = "/loginForAuthCode")
    @ResponseBody
    public ResponseEntity<String> handleLoginForAuthCode(@RequestBody Map<String, String> json, HttpServletResponse httpServletResponse) {
        System.out.println("kontroler: handleLogin (POST)");

        String username = json.get("username");
        String password = json.get("password");
        String clientID = json.get("clientID");

        try {
            var modelResponse = logInUser.handleLoginForAuthCode(username, password, clientID, passwordEncoder);

            // dodaje cookie do response
            var authCode = (AuthCode) modelResponse.content;
            var cookieAuthCode = new Cookie("AuthCode", authCode.getContent());
            cookieAuthCode.setPath("/");
            httpServletResponse.addCookie(cookieAuthCode);

            // zamiast strony "alreadyLogged" zwraca AuthCode
            return new ResponseEntity<>(authCode.getContent(), HttpStatus.OK);
//            return WebView.LoginView(modelResponse, httpServletResponse);

        } catch (ResponseStatusException rse) {
            return new ResponseEntity<>(rse.getReason(), rse.getStatus());
        }
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<String> handleLogin(@RequestBody Map<String, String> json) {
        System.out.println("kontroler: handleLogin (POST)");

        String username = json.get("username");
        String password = json.get("password");
        String clientID = json.get("clientID");

        try {
            if (logInUser.handleLogin(username, password, clientID, passwordEncoder)) {
                return new ResponseEntity<>("true", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("false", HttpStatus.UNAUTHORIZED);
            }
        } catch (ResponseStatusException rse) {
            rse.printStackTrace();
            return new ResponseEntity<>(rse.getReason(), rse.getStatus());
        }
    }
}
