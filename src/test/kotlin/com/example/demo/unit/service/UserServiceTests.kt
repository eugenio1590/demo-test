package com.example.demo.unit.service

import com.example.demo.model.Role
import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import com.example.demo.service.UserService
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserServiceTests : ShouldSpec() {

    private val userRepository = mockk<UserRepository>()
    private val userService = UserService(userRepository)

    init {
        should("be load the user by his email") {
            val roles = setOf(Role(name = "USER"))
            val user = User(email = "user@email.com", password = "132456789", roles = roles)
            every { userRepository.findOneByEmail(user.email) } returns user
            userService.loadUserByUsername(user.email).should {
                it.username.shouldBe(user.email)
                it.password.shouldBe(user.password)
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
    }

}