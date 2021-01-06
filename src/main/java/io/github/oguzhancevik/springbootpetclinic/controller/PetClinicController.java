package io.github.oguzhancevik.springbootpetclinic.controller;

import org.springframework.stereotype.Controller;
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
    public String indexPage() {
        return "index";
    }

}
