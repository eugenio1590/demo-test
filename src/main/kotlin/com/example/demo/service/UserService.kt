package com.example.demo.service

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val passwordEncoder: BCryptPasswordEncoder,
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findOneByEmail(username)?.let {
            val roles: List<GrantedAuthority> = it.roles.map { role -> SimpleGrantedAuthority(role.name) }
            org.springframework.security.core.userdetails.User(it.email, it.password, roles)
        } ?: throw UsernameNotFoundException(username)
    }

    fun registerUserAccount(account: User): User {
        account.password = passwordEncoder.encode(account.password)
        return userRepository.saveAndFlush(account)
    }

}