package sia.tacocloud;

import lombok.Data;

import java.util.List;

/**
 * Ингредиенты – это основные строительные блоки тако. Чтобы понять,
 * как они объединяются, мы определим класс Taco (листинг 2.2).
 */

@Data
public class Taco {
    private String name;
    private List<Ingredient> ingredients;
}
