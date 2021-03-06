package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.model.CategoryRepository;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.DonationRepository;
import pl.coderslab.charity.model.InstitutionRepository;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/form")
public class DonationController {
    private final CategoryRepository categoryRepository;
    private final InstitutionRepository institutionRepository;
    private final DonationRepository donationRepository;
    private final UserService userService;

    public DonationController(CategoryRepository categoryRepository, InstitutionRepository institutionRepository, DonationRepository donationRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.institutionRepository = institutionRepository;
        this.donationRepository = donationRepository;
        this.userService = userService;
    }

    @GetMapping("")
    public String formAction(@AuthenticationPrincipal UserDetails customUser, Model model){
        User user = userService.findByEmail(customUser.getUsername());
        Donation donation = new Donation();
        model.addAttribute("categoriesList", categoryRepository.findAll());
        model.addAttribute("institutionList", institutionRepository.findAll());
        model.addAttribute("donation", donation);
        model.addAttribute("loggedUser", user);
        return "form";
    }

    @PostMapping("")
    public String formActionPost(Donation donation, @AuthenticationPrincipal UserDetails customUser, Model model){
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("loggedUser", user);
        donation.setStatus("nieodebrane");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        donation.setCreated(LocalDateTime.now().format(formatter));
        donation.setUser(user);
        donationRepository.save(donation);
        return "formConfirmation";
    }
}
