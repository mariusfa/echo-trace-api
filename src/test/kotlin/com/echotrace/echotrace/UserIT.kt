package com.echotrace.echotrace

import com.echotrace.echotrace.repository.fakes.UserRepositoryFake
import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserIT(
    @Autowired
    private val mvc: MockMvc,
    @Autowired
    private val userRepositoryFake: UserRepositoryFake
) {

    @Test
    fun `should test register user`() {
        mvc.post("/user/register") {
            contentType = APPLICATION_JSON
            content = """
                {
                    "username": "test user",
                    "password": "test password"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
        }
        val user = userRepositoryFake.users[0]
        assert(user != null)
        assert(user.name == "test user")
        assert(user.hashedPassword != null)
        assert(user.apiToken != null)
    }

    @Test
    fun `should test login user`() {
        mvc.post("/user/register") {
            contentType = APPLICATION_JSON
            content = """
                {
                    "username": "test user",
                    "password": "test password"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
        }
        mvc.post("/user/login") {
            contentType = APPLICATION_JSON
            content = """
                {
                    "username": "test user",
                    "password": "test password"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { exists() }
        }
    }

    @Test
    fun `should test get api token`() {
        mvc.post("/user/register") {
            contentType = APPLICATION_JSON
            content = """
                {
                    "username": "test user",
                    "password": "test password"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
        }

        val response = mvc.post("/user/login") {
            contentType = APPLICATION_JSON
            content = """
                {
                    "username": "test user",
                    "password": "test password"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { exists() }
        }.andReturn().response.contentAsString
        val token = JsonPath.read<String>(response, "$.token")

        mvc.get("/user/api-token") {
            contentType = APPLICATION_JSON
            header("Authorization", "Bearer $token")
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { exists() }
        }
    }
}