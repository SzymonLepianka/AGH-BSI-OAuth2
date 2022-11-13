package Group28.OAuth.DAO;

import java.sql.SQLException;
import java.util.List;

public interface IDataBaseAccessObject<T> {
    List<T> readAll() throws SQLException;
    T readById(Long id) throws SQLException;

    T create(T object) throws SQLException;
    T update(T object) throws SQLException;
    void remove(T object) throws SQLException;

}
