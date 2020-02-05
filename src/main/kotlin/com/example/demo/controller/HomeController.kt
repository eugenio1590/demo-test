package com.example.demo.controller

import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @GetMapping("/")
    fun home(): String {
        return "index"
    }

    @GetMapping("/hello")
    @Secured(value = ["USER", "MANAGER"])
    fun hello(): String {
        return "hello"
    }
}