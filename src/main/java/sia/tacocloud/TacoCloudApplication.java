package sia.tacocloud;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sia.tacocloud.data.IngredientRepository;
import sia.tacocloud.Ingredient.Type;

@SpringBootApplication
public class TacoCloudApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(TacoCloudApplication.class, args);
    }

    /**
     * Настройте простые автоматические контроллеры,
     * предварительно сконфигурированные с кодом статуса ответа и/или представлением для отображения тела ответа.
     * Это полезно в случаях, когда нет необходимости в пользовательской логике контроллера - например,
     * для отображения домашней страницы, выполнения простых перенаправлений URL сайта,
     * возврата статуса 404 с содержимым HTML, 204 без содержимого и т.д.
     * @param registry
     * @see ViewControllerRegistry
     */

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }

    /**
     * Поскольку CommandLineRunner и ApplicationRunner являются функциональными
     * интерфейсами, их легко объявить как bean-компоненты в классе конфигурации с помощью
     * метода с аннотацией @Bean, который возвращает лямбда-функцию.
     * Эти два интерфейса очень похожи. Оба являются функциональными интерфейсами,
     * требующими реализации одного метода run().
     * Удобство применения интерфейса CommandLineRunner или ApplicationRunner
     * для первоначальной загрузки данных состоит в том,
     * что в этом случае вместо SQL-сценария используются репозитории,
     * то есть они одинаково хорошо будут работать и с реляционными,
     * и с нереляционными базами данных.
     * @param repo
     * @return
     */
    @Bean
    public CommandLineRunner dataLoader(IngredientRepository repo){
        return args -> {
            repo.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
            repo.save(new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
            repo.save(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
            repo.save(new Ingredient("CARN", "Carnitas", Type.PROTEIN));
            repo.save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
            repo.save(new Ingredient("LETC", "Lettuce", Type.VEGGIES));
            repo.save(new Ingredient("CHED", "Cheddar", Type.CHEESE));
            repo.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
            repo.save(new Ingredient("SLSA", "Salsa", Type.SAUCE));
            repo.save(new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
        };
    }
}
