
package com.bridgelabz.cryptotracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity // enables @PreAuthorize 
public class MethodSecurityConfig {
}

