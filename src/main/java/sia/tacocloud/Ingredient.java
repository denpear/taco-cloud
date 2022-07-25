package sia.tacocloud;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * Для начала сосредоточимся на ингредиентах тако. В нашей предметной области ингредиенты для тако – довольно простые объекты.
 * У каждого ингредиента есть название и тип, позволяющие его визуально классифицировать (белки, сыры, соусы и т. д.).
 * У каждого также есть идентификатор, по которому на него можно ссылаться.
 */

@Data
@AllArgsConstructor
/**
 * Однако мы не собираемся давать возможность использовать его извне,
 * поэтому объявили его приватным, передав атрибут access со значением
 * AccessLevel.PRIVATE. Мы также должны сделать
 * все свойства финальными, поэтому добавили атрибут force со значением true,
 * в результате чего конструктор, сгенерированный библиотекой Lombok, присвоит им значение по умолчанию null, 0 или false,
 * в зависимости от типа свойства.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Table("ingredients")
public class Ingredient {
    @PrimaryKey
    private final String id;
    private final String name;
    private final Type type;

    public enum Type{
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

}
