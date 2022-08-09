package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.User;


public interface UserRepository extends CrudRepository<User,Long> {
    /**
     * Spring Data JPA автоматически генерирует реализацию этого
     * интерфейса во время выполнения. Таким образом, теперь мы
     * готовы написать свою службу хранения учетных
     * записей, использующую этот репозиторий.
     * @param username
     * @return
     */
    User findByUsername(String username);
}
