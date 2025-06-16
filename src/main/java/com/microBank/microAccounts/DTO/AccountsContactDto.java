package com.microBank.microAccounts.DTO;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

// data carriors
@ConfigurationProperties(prefix = "accounts")
public record AccountsContactDto(String message, Map<String, String> contactDetails, List<String> callSupport){

}
