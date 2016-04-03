package com.disc99.timeline;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Account {
    AccountId id;
    String name;
    String password;
}
