package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.user.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/admins")
public class AdminAdminsController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminAdminsController(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("")
    public String adminAdmins(@AuthenticationPrincipal UserDetails customUser, Model model){
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("loggedUser", user);
        model.addAttribute("adminList", userRepository.findAllByRolesOrderByEmail(roleRepository.getOne(2)));
        return "admins";
    }

    @GetMapping("/edit/{id}")
    public String adminAdminsEdit(@AuthenticationPrincipal UserDetails customUser, Model model, @PathVariable Long id){
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("loggedUser", user);
        User userToEdit = userRepository.getOne(id);
        model.addAttribute("user", userToEdit);
        return "usersForm";
    }

    @PostMapping("/edit/{id}")
    public String adminAdminsEditForm(@Valid User user, BindingResult result, @PathVariable Long id){
        User userToEdit = userRepository.getOne(id);
        if(!userToEdit.getEmail().equals(user.getEmail())) {
            if(result.hasErrors()) {
                return "usersForm";
            }
        }
        userToEdit.setEmail(user.getEmail());
        userToEdit.setFirstName(user.getFirstName());
        userToEdit.setLastName(user.getLastName());
        userRepository.save(userToEdit);
        return "redirect:/admin/admins";
    }

    @GetMapping("/delete/{id}")
    public String adminAdminsDelete(@AuthenticationPrincipal UserDetails customUser, @PathVariable Long id){
        User user = userService.findByEmail(customUser.getUsername());
        User userToDelete = userRepository.getOne(id);
        if(!user.getId().equals(userToDelete.getId())) {
            userToDelete.setRoles(null);
            userRepository.save(userToDelete);
            userRepository.delete(userToDelete);
        }
        return "redirect:/admin/admins";
    }

    @GetMapping("/add")
    public String adminAdminsAdd(@AuthenticationPrincipal UserDetails customUser, Model model){
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("loggedUser", user);
        model.addAttribute("user", new User());
        return "adminsAdd";
    }

    @PostMapping("/add")
    public String adminAdminsAddForm(User user, @AuthenticationPrincipal UserDetails customUser, Model model){
        if(user.getEmail() != null) {
            if(userService.findByEmail(user.getEmail()) != null) {
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.getOne(2));
                User newAdmin = userService.findByEmail(user.getEmail());
                newAdmin.setRoles(roles);
                userRepository.save(newAdmin);
            }else{
                User loggedUser = userService.findByEmail(customUser.getUsername());
                model.addAttribute("loggedUser", loggedUser);
                model.addAttribute("user", new User());
                model.addAttribute("noExist", "noExist");
                return "adminsAdd";
            }
        }
        return "redirect:/admin/admins";
    }
}