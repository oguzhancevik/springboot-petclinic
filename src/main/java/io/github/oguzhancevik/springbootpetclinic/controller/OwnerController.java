package io.github.oguzhancevik.springbootpetclinic.controller;

import io.github.oguzhancevik.springbootpetclinic.model.Owner;
import io.github.oguzhancevik.springbootpetclinic.service.PetClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("owners")
public class OwnerController {

    private PetClinicService petClinicService;

    @Autowired
    public void setPetClinicService(PetClinicService petClinicService) {
        this.petClinicService = petClinicService;
    }

    @GetMapping
    public String ownersPage(Model model) {
        model.addAttribute("owners", petClinicService.findOwners());
        return "list-owner";
    }

    @GetMapping("/new")
    public String createOwnerPage(Model model) {
        model.addAttribute("owner", new Owner());
        return "create-owner";
    }

    @PostMapping(value = "/new")
    public String createOwner(@Valid Owner owner, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "create-owner";
        petClinicService.saveOwner(owner);
        return "redirect:/owners";
    }

    @GetMapping("/{id}")
    public String editOwnerPage(@PathVariable("id") Long id, Model model) {
        Owner owner = petClinicService.findOwner(id);
        model.addAttribute("owner", owner);
        return "create-owner";
    }

    @GetMapping("/delete/{id}")
    public String deleteOwner(@PathVariable("id") Long id) {
        petClinicService.deleteOwner(id);
        return "redirect:/owners";
    }

}
