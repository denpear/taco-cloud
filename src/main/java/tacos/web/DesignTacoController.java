package tacos.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.TacoUDT;
import tacos.data.IngredientRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @SessionAttributes("tacoOrder") указывает, что объект TacoOrder,
 * объявленный в классе чуть ниже, должен поддерживаться на уровне
 * сеанса. Это важно, потому что создание тако также является первым
 * шагом в создании заказа, и созданный нами заказ необходимо будет
 * перенести в сеанс, охватывающий несколько запросов.
 */

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository) {
                this.ingredientRepository = ingredientRepository;
    }

    /**
     * метод addIngredientsToModel(), который снабжен аннотацией @ModelAttribute.
     * Этот метод тоже будет вызываться в процессе обработки запроса
     * и создавать список объектов Ingredient, который
     * затем будет помещен в модель. Пока список жестко «зашит» в код.
     *
     * @param model Model – это объект, в котором данные пересылаются между контроллером и любым представлением,
     *  ответственным за преобразование этих данных в разметку HTML.
     * В конечном итоге данные, помещенные в атрибуты модели,
     * копируются в атрибуты запроса сервлета, где представление найдет
     * их и использует для отображения страницы в браузере пользователя.
     */

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(i -> ingredients.add(i));

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }

    /**
     * Объект TacoOrder, упомянутый выше в аннотации @SessionAttributes,
     * хранит состояние собираемого заказа, пока клиент выбирает ингредиенты для тако несколькими запросами.
     * @return создают новый объект TacoOrder для размещения в модели.
     */
    @ModelAttribute (name = "tacoOrder")
    public TacoOrder order(){
        return new TacoOrder();
    }

    /**
     * Объект Taco помещается в модель, чтобы представление, отображаемое в ответ
     * на запрос GET с путем /design, имело объект для отображения.
     * @return создают новый объект Taco для размещения в модели.
     */

    @ModelAttribute(name = "taco")
    public Taco taco(){
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(){
        return "design";
    } //имя представления, которое будет отправлено пользователю.

    private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Type type){
        return StreamSupport.stream(ingredients.spliterator(),false).filter(x->x.getType().equals(type)).collect(Collectors.toList());
    }

    /**
     * Аннотация @PostMapping, которую мы применили к методу pro-
     * cessTaco(), сообщает аннотации @RequestMapping на уровне класса,
     * что processTaco() будет обрабатывать запросы POST с путем /design.
     * Это именно то, что нам нужно для обработки творений мастеров тако.
     * @param taco
     * @param tacoOrder
     * @return
     */
    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder){
        if (errors.hasErrors()) return "design";
        tacoOrder.addTaco(new TacoUDT(taco.getName(), taco.getIngredients()));
        log.info("Processing taco: {} ", taco);
        return "redirect:/orders/current"; //имя представления, которое будет отправлено пользователю.
    }
}
