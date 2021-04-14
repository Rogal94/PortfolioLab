package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.DonationRepository;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final DonationRepository donationRepository;

    public UserController(UserService userService, DonationRepository donationRepository) {
        this.userService = userService;
        this.donationRepository = donationRepository;
    }

    @GetMapping("/edit")
    public String userEdit(@AuthenticationPrincipal UserDetails customUser, Model model) {
        User user = userService.findByEmail(customUser.getUsername());
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("loggedUser", user);
        return "userEdit";
    }

    @GetMapping("/edit/error")
    public String userEditError(@AuthenticationPrincipal UserDetails customUser, Model model) {
        model.addAttribute("error", "error");
        User user = userService.findByEmail(customUser.getUsername());
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("loggedUser", user);
        return "userEdit";
    }

    @PostMapping("/edit")
    public String userEditPost(@Valid User user, BindingResult result, @RequestParam String password2, Model model, @AuthenticationPrincipal UserDetails customUser) {
        if(!user.getPassword().equals(password2)) {
            model.addAttribute("error", "error");
            return "redirect:/user/edit/error";
        }
        User userToEdit = userService.findByEmail(customUser.getUsername());

        if(userToEdit.getEmail().equals(user.getEmail())) {
            List<FieldError> errorsToKeep =
                    result.getFieldErrors().stream()
                            .filter(fer -> !fer.getField().equals("email"))
                            .collect(Collectors.toList());

            result = new BeanPropertyBindingResult(user, "user");

            for (FieldError fieldError : errorsToKeep) {
                result.addError(fieldError);
            }
            model.addAttribute("noError","noError");
        }
        if(result.hasErrors()) {
            return "userEdit";
        }

        userToEdit.setEmail(user.getEmail());
        userToEdit.setFirstName(user.getFirstName());
        userToEdit.setLastName(user.getLastName());
        userToEdit.setPassword(user.getPassword());
        userService.editUser(userToEdit);
        return "redirect:/form";
    }

    @GetMapping("/donations")
    public String userDonations(@AuthenticationPrincipal UserDetails customUser, Model model) {
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("loggedUser", user);
        model.addAttribute("donationList",donationRepository.findAllByUserIdOrderByStatusDescReceivedDescCreatedDesc(user.getId()));
        return "donations";
    }

    @GetMapping("/donations/details/{id}")
    public String userDonationDetails(@AuthenticationPrincipal UserDetails customUser, Model model, @PathVariable Long id) {
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("loggedUser", user);
        model.addAttribute("donation",donationRepository.getOne(id));
        return "donationDetails";
    }

    @GetMapping("/donations/received/{id}")
    public String userDonationForm(@AuthenticationPrincipal UserDetails customUser, Model model, @PathVariable Long id) {
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("loggedUser", user);
        Donation donation = donationRepository.getOne(id);
        donation.setStatus("odebrane");
        donationRepository.save(donation);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        donation.setReceived(LocalDateTime.now().format(formatter));
        model.addAttribute("donation",donationRepository.getOne(id));
        return "donationDetails";
    }
}
