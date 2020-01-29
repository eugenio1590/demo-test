package com.example.demo.config

import com.example.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @get:Bean
    val passwordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Autowired
    lateinit var userService: UserService

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        with(http) {
            authorizeRequests {
                it.antMatchers("/css/**", "/index").permitAll()
                    .antMatchers("/user/**").hasRole("USER")
            }
            formLogin {
                it.loginPage("/login")
            }
        }

    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService)
            .passwordEncoder(passwordEncoder)
    }
}