package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.model.DonationRepository;
import pl.coderslab.charity.model.InstitutionRepository;


@Controller
public class HomeController {

    private final InstitutionRepository institutionRepository;
    private final DonationRepository donationRepository;

    public HomeController(InstitutionRepository institutionRepository, DonationRepository donationRepository) {
        this.institutionRepository = institutionRepository;
        this.donationRepository = donationRepository;
    }

    @RequestMapping("/")
    public String homeAction(Model model){
        if(donationRepository.selectTotalsQuantity() == null) {
            model.addAttribute("totalsQuantity", "0");
        }else{
            model.addAttribute("totalsQuantity", donationRepository.selectTotalsQuantity());
        }
        model.addAttribute("institutionList", institutionRepository.findAll());
        model.addAttribute("totalsDonation",  donationRepository.selectTotalsDonation());
        return "index";
    }
}
