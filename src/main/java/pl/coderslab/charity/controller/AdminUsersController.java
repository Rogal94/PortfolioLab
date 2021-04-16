package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.user.RoleRepository;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserRepository;
import pl.coderslab.charity.user.UserService;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminUsersController(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("")
    public String adminUsers(@AuthenticationPrincipal UserDetails customUser, Model model){
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("loggedUser", user);
        model.addAttribute("userList", userRepository.findAllByRolesOrderByEmail(roleRepository.getOne(1)));
        return "users";
    }

    @GetMapping("/edit/{id}")
    public String adminUsersEdit(@AuthenticationPrincipal UserDetails customUser, Model model, @PathVariable Long id){
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("loggedUser", user);
        User userToEdit = userRepository.getOne(id);
        model.addAttribute("user", userToEdit);
        return "usersForm";
    }

    @PostMapping("/edit/{id}")
    public String adminUsersEditForm(@Valid User user, BindingResult result, @PathVariable Long id){
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
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String adminUsersDelete(@PathVariable Long id){
        User userToDelete = userRepository.getOne(id);
        userToDelete.setRoles(null);
        userRepository.save(userToDelete);
        userRepository.delete(userToDelete);
        return "redirect:/admin/users";
    }

    @GetMapping("/block/{id}")
    public String adminUsersBlock(@PathVariable Long id){
        User userBlock = userRepository.getOne(id);
        userBlock.setAccountNonLocked(!userBlock.isAccountNonLocked());
        userRepository.save(userBlock);
        return "redirect:/admin/users";
    }

    @GetMapping("/password/{id}")
    public String adminUsersPasswordChange(@PathVariable Long id){
        User user = userRepository.getOne(id);
        user.setAccountNonExpired(false);
        userRepository.save(user);
        return "redirect:/admin/users";
    }
}
