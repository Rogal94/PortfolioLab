package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserService;

import javax.validation.Valid;

@Controller
public class LoginRegisterController {

    private final UserService userService;

    public LoginRegisterController(UserService userService) {
        this.userService = userService;
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
        return "redirect:/login";
    }
}
