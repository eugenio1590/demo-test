package com.example.demo.service

import com.example.demo.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findOneByEmail(username)?.let {
            val roles: List<GrantedAuthority> = it.roles.map { role -> SimpleGrantedAuthority(role.name) }
            User(it.email, it.password, roles)
        } ?: throw UsernameNotFoundException(username)
    }
}