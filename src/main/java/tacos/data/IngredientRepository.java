package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.Ingredient;

/**
 * интерфейс Repository параметризован. Первый па-
 * раметр – это тип объектов, которые будут храниться в репозитории,
 * в данном случае Ingredient. Второй параметр – это тип поля иден-
 * тификатора хранимого объекта
 */
public interface IngredientRepository extends CrudRepository<Ingredient,String> {

}
