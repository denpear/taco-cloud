package tacos.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tacos.Ingredient;
import tacos.data.IngredientRepository;


/**
 * снабжен аннотацией @Component, то есть он обнаруживается механизмом сканирования
 * и создается как bean-компонент в контексте приложения Spring.
 * Механизм автоконфигурации Spring Boot обнаружит этот и все другие
 * bean-компоненты, реализующие интерфейс Converter, и автоматически
 * зарегистрирует их для использования в Spring MVC, когда потребуется
 * преобразовать параметры запроса в свойства.
 */
@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {
    private IngredientRepository ingredientRepository;

    @Autowired
    public IngredientByIdConverter(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * Метод convert() просто принимает строку, служащую идентификатором ингредиента,
     * и использует ее для поиска соответствующего ингредиента в ассоциативном массиве Map.
     */
    @Override
    public Ingredient convert(String id) {
        return ingredientRepository.findById(id).orElse(null);
    }
}
