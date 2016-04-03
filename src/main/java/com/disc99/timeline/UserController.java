package com.disc99.timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class UserController {

    @Autowired
    AccountRepository accountRepository;

    @RequestMapping(value = "/register", method = POST)
    public String register(@Valid @ModelAttribute RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/signup";
        }
        accountRepository.save(new Account(null, request.getUsername(), request.getPassword()));
        return "redirect:/timeline";
    }
}
