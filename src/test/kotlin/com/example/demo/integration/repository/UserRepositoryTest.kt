package com.example.demo.integration.repository

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(LiquibaseAutoConfiguration::class)
class UserRepositoryTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var repository: UserRepository

    @Test
    @Throws(Exception::class)
    internal fun `should be load the user by his email`() {
        val user = User(email = "user@email.com", password = "123456789")
        entityManager.persist<Any>(user)
        repository.findOneByEmail(user.email).also {
            it.shouldNotBeNull()
            it.email.shouldBe(user.email)
            it.password.shouldBe(user.password)
        }
    }

    @Test
    @Throws(Exception::class)
    internal fun `should return null when the user's email not found`() {
        repository.findOneByEmail("not_found@email.com").shouldBeNull()
    }
}