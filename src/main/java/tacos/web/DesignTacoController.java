package tacos.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
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
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private TacoRepository tacoRepo;
    private UserRepository userRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository tacoRepo, UserRepository userRepo) {
        this.ingredientRepo = ingredientRepo;
        this.tacoRepo = tacoRepo;
        this.userRepo = userRepo;
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
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));

        Ingredient.Type[] types = Ingredient.Type.values();
        for (Ingredient.Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }

    /**
     * Объект TacoOrder, упомянутый выше в аннотации @SessionAttributes,
     * хранит состояние собираемого заказа, пока клиент выбирает ингредиенты для тако несколькими запросами.
     * @return создают новый объект TacoOrder для размещения в модели.
     */
    @ModelAttribute (name = "order")
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

    @ModelAttribute(name = "user")
    public User user(Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        return user;
    }



    @GetMapping
    public String showDesignForm(){
        return "design";
    } //имя представления, которое будет отправлено пользователю.

    /**
     * Аннотация @PostMapping, которую мы применили к методу pro-
     * cessTaco(), сообщает аннотации @RequestMapping на уровне класса,
     * что processTaco() будет обрабатывать запросы POST с путем /design.
     * Это именно то, что нам нужно для обработки творений мастеров тако.
     * @param taco
     * @param order
     * @return
     */
    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder order){
        log.info("   --- Saving taco");
        if (errors.hasErrors()) return "design";
        Taco saved = tacoRepo.save(taco);
        order.addTaco(saved);
        log.info("Processing taco: {} ",taco);
        return "redirect:/orders/current"; //имя представления, которое будет отправлено пользователю.
    }

    private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Ingredient.Type type){
        return StreamSupport.stream(ingredients.spliterator(),false).filter(x->x.getType().equals(type)).collect(Collectors.toList());
    }
}
