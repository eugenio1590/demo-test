package com.example.demo.seeder

import com.example.demo.model.User
import com.example.demo.repository.RoleRepository
import com.example.demo.repository.UserRepository
import com.example.demo.service.UserService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.env.Environment
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class UserSeeder(
    private val env: Environment,
    private val roleRepository: RoleRepository,
    private val userService: UserService
) : ApplicationRunner {

    //@VisibleForTesting
    enum class AdminProperties {
        ADMIN_USERNAME, ADMIN_PASSWORD
    }

    override fun run(args: ApplicationArguments) {
        createAdminUser()
    }

    private fun createAdminUser() {
        env.getProperty(AdminProperties.ADMIN_USERNAME.name).takeUnless { it.isNullOrBlank() }?.also { email ->
            try {
                userService.loadUserByUsername(email)
            } catch (e: UsernameNotFoundException){
                env.getProperty(AdminProperties.ADMIN_PASSWORD.name).takeUnless { it.isNullOrBlank() }?.also { password ->
                    roleRepository.findOneByName("ADMIN")?.let { role ->
                        val user = User(email = email, password = password, roles = setOf(role))
                        userService.registerUserAccount(user)
                    }
                }
            }
        }
    }
}
