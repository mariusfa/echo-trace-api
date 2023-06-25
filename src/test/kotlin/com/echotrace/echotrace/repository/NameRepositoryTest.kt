package com.echotrace.echotrace.repository

import com.echotrace.echotrace.EchotraceApplication
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = [EchotraceApplication::class])
@Import(NameRepository::class)
class NameRepositoryTest(
    @Autowired
    private val nameRepository: NameRepository,
    @Autowired
    private val flyway: Flyway,
    @Autowired
    private val jdbcTemplate: JdbcTemplate
) {
    @Test
    fun `test config`() {
    }

    @Test
    fun `test insert name`() {
        flyway.migrate()
        val name = Name(
            id = null,
            name = "test event"
        )
        nameRepository.insert(name)
        val namesRaw = jdbcTemplate.queryForList("SELECT * FROM echotraceschema.name")
        assert(namesRaw.size == 1)
    }

    @Test
    fun `test get names`() {
        flyway.migrate()
        val name = Name(
            id = null,
            name = "test event"
        )
        nameRepository.insert(name)
        val names = nameRepository.getNames()
        assert(names.size == 1)
        assert(names[0].name == "test event")
    }

    @Test
    fun `test get by name`() {
        flyway.migrate()
        val name = Name(
            id = null,
            name = "test event"
        )
        nameRepository.insert(name)
        val nameFound = nameRepository.getByName(name.name)
        assert(nameFound != null)
        assert((nameFound?.name ?: "") == name.name)
    }

    @Test
    fun `test update name`() {
        flyway.migrate()
        val name = Name(
            id = null,
            name = "test event"
        )
        nameRepository.insert(name)
        val nameFound = nameRepository.getByName(name.name)

        val nameUpdated = Name(
            id = nameFound?.id,
            name = "test event updated"
        )
        nameRepository.update(nameUpdated)
        val nameFoundUpdated = nameRepository.getByName(nameUpdated.name)
        assert(nameFoundUpdated != null)
        assert((nameFoundUpdated?.name ?: "") == nameUpdated.name)
    }

    @Test
    fun `test delete name`() {
        flyway.migrate()
        val name = Name(
            id = null,
            name = "test event"
        )
        nameRepository.insert(name)
        val nameFound = nameRepository.getByName(name.name)
        assert(nameFound != null)
        nameRepository.delete(nameFound?.id ?: 0)
        val nameFoundDeleted = nameRepository.getByName(name.name)
        assert(nameFoundDeleted == null)
    }
}