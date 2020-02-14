package com.example.demo.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

@Controller
class HomeController : AppController() {

    @GetMapping("/")
    fun home(): String {
        return "index"
    }

    @GetMapping("/dashboard")
    fun dashboard(model: ModelMap): ModelAndView {
        val path = if (auth.authorities.contains(SimpleGrantedAuthority("ADMIN"))) "admin" else "hello"
        return ModelAndView("redirect:/$path", model)
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAnyAuthority('USER','MANAGER')")
    fun hello(): String {
        return "hello"
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    fun admin(): String = "not implemented yet"
}