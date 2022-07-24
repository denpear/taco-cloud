package sia.tacocloud;

import lombok.Data;

/**
 * Этот класс можно было бы снабдить аннотацией @Table, но
 * в данном случае в этом нет необходимости.
 * В таблице "Ingredient_Ref" нет столбца типа identity,
 * поэтому никакие свойства в классе
 * IngredientRef не нужно аннотировать с помощью
 * @Id.
 */

@Data
public class IngredientRef {
    private final String ingredient;
}
