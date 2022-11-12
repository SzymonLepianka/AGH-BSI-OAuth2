package Server.Model;

import Server.Domain.User;
import Server.Domain.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddUser {
    public static boolean addUser(String username, String password, String email, String firstName, String surname, String birthDate, UsersRepository usersRepository) {
        User n = new User();
        n.setUsername(username);
        n.setPassword(hashPassword(password));
        n.setEmail(email);
        n.setFirstName(firstName);
        n.setSurname(surname);
        try {
            n.setBirthDate(new SimpleDateFormat("dd.MM.yyyy").parse(birthDate));
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date parse error");
        }
        usersRepository.save(n);
        return true;
    }

    private static String hashPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Password hashing problem");
        }
    }

}
