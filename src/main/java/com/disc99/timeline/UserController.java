package com.disc99.timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class UserController {

    @Autowired
    AccountRepository accountRepository;

    @RequestMapping(value = "/register", method = POST)
    public String regist(@Valid @ModelAttribute RegisterRequest request, BindingResult bindingResult) {
        System.out.println(String.format("%s : %s", request, bindingResult.hasErrors()));
        if (bindingResult.hasErrors()) {
            return "/signup";
        }

        accountRepository.save(new Account(request.getUsername(), request.getPassword()));

        return "redirect:/timeline";
    }
}
