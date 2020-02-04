package com.example.demo.unit.seeder

import com.example.demo.model.Role
import com.example.demo.model.User
import com.example.demo.repository.RoleRepository
import com.example.demo.seeder.UserSeeder
import com.example.demo.seeder.UserSeeder.AdminProperties
import com.example.demo.service.UserService
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verifyAll
import io.mockk.verifyOrder
import org.springframework.core.env.Environment
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserSeederTest : ShouldSpec() {

    private val env = mockk<Environment>()
    private val roleRepository = mockk<RoleRepository>()
    private val userService = mockk<UserService>()
    private val userSeeder = UserSeeder(env, roleRepository, userService)

    private val role = Role(name = "ADMIN")
    private val user = User(email = "user_admin@email.com", password = "123456789", roles = setOf(role))

    init {
        should("create a user with the ADMIN role") {
            val result = slot<User>()
            every { env.getProperty(AdminProperties.ADMIN_USERNAME.name) } returns user.email
            every { env.getProperty(AdminProperties.ADMIN_PASSWORD.name) } returns user.password
            every { userService.loadUserByUsername(user.email) } throws UsernameNotFoundException("email not found")
            every { roleRepository.findOneByName(role.name) } returns role
            every { userService.registerUserAccount(capture(result)) } returns user

            userSeeder.run(mockk())

            verifyOrder {
                userService.loadUserByUsername(user.email)
                roleRepository.findOneByName(role.name)
                userService.registerUserAccount(any())
            }
            with(result){
                isCaptured.shouldBeTrue()
                with(captured) {
                    email.shouldBe(user.email)
                    roles.shouldNotBeEmpty()
                }
            }
        }

        should("not create a user when it exists") {
            every { userService.loadUserByUsername(user.email) } returns mockk()

            verifyAll {
                roleRepository.findOneByName(role.name)?.wasNot(Called)
                userService.registerUserAccount(any()) wasNot Called
            }
        }
    }
}