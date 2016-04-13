package com.disc99.timeline;

import lombok.Value;
import org.springframework.security.core.authority.AuthorityUtils;

import static com.disc99.timeline.Role.USER;


@Value
public class User extends org.springframework.security.core.userdetails.User {

    Account account;

    public User(Account account) {
        super(account.getName(), account.getPassword(), AuthorityUtils.createAuthorityList(USER));
        this.account = account;
    }

    public AccountId accountId() {
        return account.getId();
    }
}
