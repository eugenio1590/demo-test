package com.example.demo.unit.service

import com.example.demo.model.Role
import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import com.example.demo.service.UserService
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UserServiceTests : ShouldSpec() {

    private val passwordEncoder = BCryptPasswordEncoder()
    private val userRepository = mockk<UserRepository>()
    private val userService = UserService(passwordEncoder, userRepository)

    private val user = User(email = "user@email.com", password = "132456789")

    init {
        should("be load the user by its email") {
            val roles = setOf(Role(name = "USER"))
            every { userRepository.findOneByEmail(user.email) } returns user.copy(roles = roles)
            userService.loadUserByUsername(user.email).should {
                it.username.shouldBe(user.email)
                it.authorities.shouldNotBeEmpty()
            }
            verify { userRepository.findOneByEmail(user.email) }
        }
        should("get and error when the user's email not found") {
            every { userRepository.findOneByEmail(any()) } returns null
            shouldThrow<UsernameNotFoundException> {
                userService.loadUserByUsername("not_valid_user@email.com")
            }
        }
        should("encrypt the password before saving user information") {
            val account = user.copy()
            every { userRepository.saveAndFlush(account) } returns account

            userService.registerUserAccount(account).apply {
                passwordEncoder.matches(user.password, password).shouldBeTrue()
            }
        }
    }

}