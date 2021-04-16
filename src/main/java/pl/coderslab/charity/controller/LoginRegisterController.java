package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.user.*;
import pl.coderslab.charity.email.EmailService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoginRegisterController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;

    public LoginRegisterController(UserService userService, EmailService emailService, TokenService tokenService, TokenRepository tokenRepository, UserRepository userRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login/{error}")
    public String loginError(Model model, @PathVariable String error) {
        model.addAttribute("error", error);
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user",new User());
        return "register";
    }

    @GetMapping("/register/error")
    public String registerError(Model model) {
        model.addAttribute("user",new User());
        model.addAttribute("error", "error");
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid User user, BindingResult result, @RequestParam String password2, Model model) {
        if(!user.getPassword().equals(password2)) {
            model.addAttribute("error", "error");
            return "redirect:/register/error";
        }
        if(result.hasErrors()) {
            return "register";
        }
        userService.saveUser(user);
        Token token = tokenService.createToken(user);
        String subject = "Potwierdzenie rejestracji";
        String text = "Link: " + "http://localhost:8080/token/register/" + token.getToken();
        //emailService.sendSimpleMessage(user.getEmail(),subject,text);
        return "redirect:/login/tokenEnabled";
    }

    @GetMapping("/token/register/{tokenStr}")
    public String registerToken(@PathVariable String tokenStr) {
        Token token = tokenRepository.findByToken(tokenStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(LocalDateTime.now().isBefore(LocalDateTime.parse(token.getExpiryDate(),formatter))) {
            User user = token.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            return "redirect:/login/enabled";
        }
        return "redirect:/login/unValid";
    }

    @GetMapping("/password")
    public String passwordReset() {
        return "resetPassword";
    }

    @PostMapping("/password")
    public String passwordResetEmail(@RequestParam String email) {
        if(userService.findByEmail(email) != null) {
            User user = userService.findByEmail(email);
            Token token = tokenService.createToken(user);
            String subject = "Reset hasła";
            String text = "Link: " + "http://localhost:8080/token/password/" + token.getToken();
            //emailService.sendSimpleMessage(user.getEmail(),subject,text);
            return "redirect:/login/tokenPassword";
        }
    return "redirect:/login/noExist";
    }

    @GetMapping("/token/password/{tokenStr}")
    public String passwordToken(@PathVariable String tokenStr, Model model) {
        Token token = tokenRepository.findByToken(tokenStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(LocalDateTime.now().isBefore(LocalDateTime.parse(token.getExpiryDate(),formatter))) {
            model.addAttribute("user" ,new User());
            model.addAttribute("token", tokenStr);
            return "resetPasswordForm";
        }
        return "redirect:/login/unValid";
    }

    @GetMapping("/token/password/error/{tokenStr}")
    public String passwordTokenError(@PathVariable String tokenStr, Model model) {
        Token token = tokenRepository.findByToken(tokenStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(LocalDateTime.now().isBefore(LocalDateTime.parse(token.getExpiryDate(),formatter))) {
            model.addAttribute("user" ,new User());
            model.addAttribute("error","error");
            model.addAttribute("token", tokenStr);
            return "resetPasswordForm";
        }
        return "redirect:/login/unValid";
    }

    @PostMapping("/token/password/{tokenStr}")
    public String passwordForm(@Valid User user, BindingResult result, @PathVariable String tokenStr, Model model, @RequestParam String password2) {
        if(!user.getPassword().equals(password2)) {
            model.addAttribute("error", "error");
            return "redirect:/token/password/error/" + tokenStr;
        }
        List<FieldError> errorsToKeep =
                result.getFieldErrors().stream()
                        .filter(fer -> fer.getField().equals("password"))
                        .collect(Collectors.toList());

            result = new BeanPropertyBindingResult(user, "user");

            for (FieldError fieldError : errorsToKeep) {
                result.addError(fieldError);
            }
        if(result.hasErrors()) {
            return "resetPasswordForm";
        }
        Token token = tokenRepository.findByToken(tokenStr);
        User userToEdit = token.getUser();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(LocalDateTime.now().isBefore(LocalDateTime.parse(token.getExpiryDate(),formatter))) {
            userToEdit.setPassword(user.getPassword());
            userService.editUser(userToEdit);
            return "redirect:/login/success";
        }
        return "redirect:/login/unValid";
    }

    @GetMapping("/reset/password")
    public String newPassword(Model model) {
        model.addAttribute("user", new User());
        return "newPassword";
    }

    @GetMapping("/reset/password/{error}")
    public String newPasswordError(Model model,@PathVariable String error) {
        model.addAttribute("error",error);
        model.addAttribute("user", new User());
        return "newPassword";
    }

    @PostMapping("/reset/password")
    public String newPasswordForm(@Valid User user, BindingResult result, Model model, @RequestParam String password2) {
        if(!user.getPassword().equals(password2)) {
            return "redirect:/reset/password/error";
        }
        if(userService.findByEmail(user.getEmail()) == null) {
            return "redirect:/reset/password/email";
        }
        if(userService.findByEmail(user.getEmail()).isAccountNonExpired()) {
            return "redirect:/reset/password/email";
        }
        List<FieldError> errorsToKeep =
                result.getFieldErrors().stream()
                        .filter(fer -> fer.getField().equals("password"))
                        .collect(Collectors.toList());

        result = new BeanPropertyBindingResult(user, "user");

        for (FieldError fieldError : errorsToKeep) {
            result.addError(fieldError);
        }
        if(result.hasErrors()) {
            return "newPassword";
        }
        User userToEdit = userService.findByEmail(user.getEmail());
        userToEdit.setPassword(user.getPassword());
        userService.editUser(userToEdit);
        return "redirect:/login/success";
    }
}
