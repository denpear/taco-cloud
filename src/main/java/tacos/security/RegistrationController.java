package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.data.UserRepository;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @return simply returns a logical view name of registration
     */
    @GetMapping
    public String registerFrom(){
        return "registration";
    }

    /**
     * Вы, несомненно, заметили, что в RegistrationController внедря-
     * ется компонент PasswordEncoder. Это тот самый bean-компонент
     * Pass wordEncoder, который мы объявили выше. При обработке формы
     * RegistrationController передает его методу toUser() для шифрова-
     * ния пароля перед сохранением в базу данных. То есть отправленный
     * пароль сохраняется в зашифрованной форме, и служба управления
     * учетными записями пользователей сможет аутентифицировать поль-
     * зователя по этому зашифрованному паролю.
     * @param form
     * @return
     */
    @PostMapping
    public String processRegistration(RegistrationForm form){
        userRepo.save(form.toUser(passwordEncoder));
        return  "redirect:/login";
    }

}
