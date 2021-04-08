package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.model.InstitutionRepository;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/institutions")
public class AdminInstitutionsController {
    private final UserService userService;
    private final InstitutionRepository institutionRepository;

    public AdminInstitutionsController(UserService userService, InstitutionRepository institutionRepository) {
        this.userService = userService;
        this.institutionRepository = institutionRepository;
    }

    @GetMapping("")
    public String adminInstitutions(@AuthenticationPrincipal UserDetails customUser, Model model){
        List<Institution> institutionList = institutionRepository.findAll();
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("institutionsList", institutionList);
        model.addAttribute("user", user);
        return "institutions";
    }

    @GetMapping("/add")
    public String adminInstitutionsAdd(@AuthenticationPrincipal UserDetails customUser, Model model){
        Institution institution = new Institution();
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("institution", institution);
        model.addAttribute("user", user);
        return "institutionsForm";
    }
    @PostMapping("/add")
    public String adminInstitutionsAddForm(@Valid Institution institution, BindingResult result, @AuthenticationPrincipal UserDetails customUser, Model model){
        if(result.hasErrors()) {
            return "institutionsForm";
        }
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("user", user);
        institutionRepository.save(institution);
        return "redirect:/admin/institutions";
    }

    @GetMapping("/edit/{id}")
    public String adminInstitutionsEdit(@AuthenticationPrincipal UserDetails customUser, Model model, @PathVariable Long id){
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("institution", institutionRepository.getOne(id));
        model.addAttribute("user", user);
        return "institutionsForm";
    }

    @PostMapping("/edit/{id}")
    public String adminInstitutionsEditForm(@Valid Institution institution, BindingResult result, @AuthenticationPrincipal UserDetails customUser, Model model, @PathVariable Long id){
        if(result.hasErrors()) {
            return "institutionsForm";
        }
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("user", user);
        institutionRepository.save(institution);
        return "redirect:/admin/institutions";
    }

    @GetMapping("/delete/{id}")
    public String adminInstitutionsDelete(@AuthenticationPrincipal UserDetails customUser, Model model, @PathVariable Long id){
        User user = userService.findByEmail(customUser.getUsername());
        model.addAttribute("user", user);
        institutionRepository.delete(institutionRepository.getOne(id));
        return "redirect:/admin/institutions";
    }
}
