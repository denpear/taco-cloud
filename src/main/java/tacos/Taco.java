package tacos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ингредиенты – это основные строительные блоки тако. Чтобы понять,
 * как они объединяются, мы определим класс Taco (листинг 2.2).
 */

@Data
// Exclude createdAt from equals() method so that tests won't fail trying to
// compare java.util.Date with java.sql.Timestamp (even though they're essentially
// equal). Need to figure out a better way than this, but excluding this property
// for now.
@EqualsAndHashCode(exclude = "createdAt")
public class Taco {

    private Long id;

    @NotNull
    @Size(min=5, message = "Name must be at least 5 characters long")
    private String name;

    private Date createdAt = new Date();

    /**
     * Объект Taco может включать в список несколько объектов Ingredient,
     * а один объект Ingredient может быть частью списков в нескольких
     * объектах Taco.
     */
    @NotNull
    @Size (min=1,message = "You must choose at least 1 ingredient")
    private List<Ingredient> ingredients = new ArrayList<>();

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

}
