package com.example.demo.controller

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

abstract class AppController {

    protected val auth: Authentication
        get() = SecurityContextHolder.getContext().authentication

}