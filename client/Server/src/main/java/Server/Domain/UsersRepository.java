package Server.Domain;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring
public interface UsersRepository extends CrudRepository<User, Long> {

}