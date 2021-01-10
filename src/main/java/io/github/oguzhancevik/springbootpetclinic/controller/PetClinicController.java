package io.github.oguzhancevik.springbootpetclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PetClinicController {

    @RequestMapping("/welcome")
    @ResponseBody
    public String welcome() {
        return "Welcome";
    }

    @RequestMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("message", "Welcome to PetClinic");
        return "index";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

}
