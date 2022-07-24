package sia.tacocloud;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Для начала сосредоточимся на ингредиентах тако. В нашей предметной области ингредиенты для тако – довольно простые объекты.
 * У каждого ингредиента есть название и тип, позволяющие его визуально классифицировать (белки, сыры, соусы и т. д.).
 * У каждого также есть идентификатор, по которому на него можно ссылаться.
 */

@Data
@Table

public class Ingredient implements Persistable<String> {
    @Id
    private final String id;
    private final String name;
    private final Type type;

    @Override
    public boolean isNew() {
        return true;
    }

    public enum Type{
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

}
