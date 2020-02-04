package com.example.demo.integration.repository

import com.example.demo.model.Role
import com.example.demo.repository.RoleRepository
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
class RoleRepositoryTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var repository: RoleRepository

    @Test
    @Throws(Exception::class)
    internal fun `should be load the role by his name`() {
        val role = Role(name = "ROLE")
        entityManager.persist<Any>(role)
        repository.findOneByName(role.name).also {
            it.shouldNotBeNull()
            it.name.shouldBe(role.name)
        }
    }
}