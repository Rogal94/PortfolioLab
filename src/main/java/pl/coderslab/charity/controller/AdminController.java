package pl.coderslab.charity.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String admin(@AuthenticationPrincipal UserDetails customUser, Model model){
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("user", user);
        return "admin";
    }

    @GetMapping("/admins")
    public String adminAdmins(@AuthenticationPrincipal UserDetails customUser, Model model){
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("user", user);
        return "admin";
    }

    @GetMapping("/users")
    public String adminUsers(@AuthenticationPrincipal UserDetails customUser, Model model){
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("user", user);
        return "admin";
    }
}
