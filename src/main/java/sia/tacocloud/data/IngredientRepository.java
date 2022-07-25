package sia.tacocloud.data;

import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.Ingredient;
import java.util.Optional;

/**
 * интерфейс Repository параметризован. Первый па-
 * раметр – это тип объектов, которые будут храниться в репозитории,
 * в данном случае Ingredient. Второй параметр – это тип поля иден-
 * тификатора хранимого объекта
 */
public interface IngredientRepository extends CrudRepository<Ingredient,String> {
}
