package sia.tacocloud;

import lombok.Data;

/**
 * Для начала сосредоточимся на ингредиентах тако. В нашей предметной области ингредиенты для тако – довольно простые объекты.
 * У каждого ингредиента есть название и тип, позволяющие его визуально классифицировать (белки, сыры, соусы и т. д.).
 * У каждого также есть идентификатор, по которому на него можно ссылаться.
 */
@Data
public class Ingredient {

    private final String id;
    private final String name;
    private final Type type;

    public enum Type{
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

}
