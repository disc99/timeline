package com.disc99.timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.validation.Valid;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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



    // DEBUG LOGIC -------------------------------------------------------------------------------------
    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/debug/users")
    @ResponseBody
    public List<Account> a() {
        return jdbcTemplate.query("select id, name, password from accounts", (rs, rowNum) -> {
            return Account.builder()
                    .id(new AccountId(rs.getLong("id")))
                    .name("name")
                    .password("password")
                    .build();
        });
    }
}
