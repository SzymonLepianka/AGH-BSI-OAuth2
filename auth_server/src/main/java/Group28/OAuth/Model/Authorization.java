package Group28.OAuth.Model;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class Authorization {

    public static void Authorize(HttpServletResponse httpServletResponse) throws ResponseStatusException, SQLException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().startsWith("AccessToken")) {
                    if (ValidateToken.validateToken(cookie.getValue())) {
                        return;
                    }
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    public static void Authorize(HttpServletResponse httpServletResponse, String clientID) throws ResponseStatusException, SQLException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().equals("AccessToken" + clientID)) {
                    if (ValidateToken.validateToken(cookie.getValue())) {
                        return;
                    }
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
