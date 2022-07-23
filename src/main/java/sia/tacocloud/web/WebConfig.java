package sia.tacocloud.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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
}
