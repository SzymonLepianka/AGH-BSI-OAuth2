package bgs.oauth_server.dao;

import bgs.oauth_server.domain.*;

import java.sql.*;
import java.util.*;

public class UsersAccessObject implements IDataBaseAccessObject<User> {
    private Connection connection;

    UsersAccessObject(Connection connection) {
        this.connection = connection;
    }

    private User createUserFromResult(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setBirthDate(rs.getDate("birth_date"));
        user.setFirstName(rs.getString("first_name"));
        user.setDeveloper(rs.getBoolean("is_developer"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setSurname(rs.getString("surname"));
        user.setUsername(rs.getString("username"));
        return user;
    }

    @Override
    public List<User> readAll() throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery("select * from users");
        List<User> ResultList = new LinkedList<>();
        while(result.next()) {
            ResultList.add(createUserFromResult(result));
        }
        return ResultList;
    }

    @Override
    public User readById(Long id) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery(String.format("select * from users where user_id=%s", id));
        if (result.next()){
            User user = createUserFromResult(result);
            return user;
        }
        return null;
    }

    @Override
    public User create(User object) throws SQLException {
        String query = " insert into users (birth_date, email, first_name, is_developer, password, phone_number, surname, username)" + " values (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        preparedStmt.setDate(1, object.getBirthDate());
        preparedStmt.setString(2, object.getEmail());
        preparedStmt.setString(3, object.getFirstName());
        preparedStmt.setBoolean(4, object.getDeveloper());
        preparedStmt.setString(5, object.getPassword());
        preparedStmt.setString(6, object.getPhoneNumber());
        preparedStmt.setString(7, object.getSurname());
        preparedStmt.setString(8,object.getUsername());
        preparedStmt.execute();
        return object;
    }

    @Override
    public User update(User object) throws SQLException {
        String query = "update users set birth_date = ?, email = ?, first_name = ?, is_developer = ?, password = ?, phone_number = ?, surname = ?, username = ? where user_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        // Set
        preparedStmt.setDate(1, object.getBirthDate());
        preparedStmt.setString(2, object.getEmail());
        preparedStmt.setString(3, object.getFirstName());
        preparedStmt.setBoolean(4, object.getDeveloper());
        preparedStmt.setString(5, object.getPassword());
        preparedStmt.setString(6, object.getPhoneNumber());
        preparedStmt.setString(7, object.getSurname());
        preparedStmt.setString(8,object.getUsername());
        // Where
        preparedStmt.setLong(9,object.getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public void remove(User object) throws SQLException {
        String query = "DELETE FROM users WHERE user_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        // Where
        preparedStmt.setLong(1, object.getId());
        preparedStmt.execute();
    }

}
