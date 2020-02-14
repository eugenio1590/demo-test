package com.example.demo.config

import nz.net.ultraq.thymeleaf.LayoutDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class BeansConfig {

    @get:Bean
    val passwordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    @get:Bean
    val layoutDialect: LayoutDialect = LayoutDialect()

}