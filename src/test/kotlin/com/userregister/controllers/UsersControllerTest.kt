import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.userregister.controllers.UsersController
import com.userregister.domain.users.User
import com.userregister.domain.users.UserRequestDTO
import com.userregister.domain.users.UserResponseDTO
import com.userregister.services.UsersService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.util.*

@WebMvcTest(controllers = [UsersController::class])
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [UsersController::class])
class UsersControllerTest(
    @Autowired
    private val mockMvc: MockMvc
) {

    @MockitoBean
    private lateinit var usersService: UsersService

    private val dummyUsers = listOf(
        UserResponseDTO(id = UUID.randomUUID(), name = "John Doe", email = "john.doe@example.com")
    )

    private val mapper = jacksonObjectMapper()

    @BeforeEach
    fun setup() {
        mapper.registerModule(JavaTimeModule())
    }

    @Test
    fun `should return all users`() {
        val users = listOf(
            UserResponseDTO(id = UUID.randomUUID(), name = "John Doe", email = "john.doe@example.com")
        )

        `when`(usersService.getAllUsers()).thenReturn(dummyUsers, users)
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))

        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()").value(users.size))
    }

    @Test
    fun `should return a user by id`() {
        val userId = UUID.randomUUID()
        val user = UserResponseDTO(id = userId, name = "John Doe", email = "john.doe@example.com")

        `when`(usersService.getUserById(userId)).thenReturn(user)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId))
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.name").value(user.name))
            .andExpect(jsonPath("$.email").value(user.email))
    }

    @Test
    fun `should create a new user`() {
        val createUserRequest = UserRequestDTO(name = "John Doe", email = "john.doe@example.com")
        val createdUser = User(id = UUID.randomUUID(), name = createUserRequest.name, email = createUserRequest.email)

        `when`(usersService.createUser(createUserRequest)).thenReturn(createdUser)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createUserRequest))
        )

        result.andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(createdUser.id.toString()))
            .andExpect(jsonPath("$.name").value(createdUser.name))
            .andExpect(jsonPath("$.email").value(createdUser.email))
    }

    @Test
    fun `should update an existing user`() {
        val userId = UUID.randomUUID()
        val updateUserRequest = UserRequestDTO(name = "John Updated", email = "john.updated@example.com")
        val updatedUser =
            UserResponseDTO(id = UUID.randomUUID(), name = updateUserRequest.name, email = updateUserRequest.email)

        `when`(usersService.updateUser(updateUserRequest, userId)).thenReturn(updatedUser)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateUserRequest))
        )

        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(updatedUser.id.toString()))
            .andExpect(jsonPath("$.name").value(updatedUser.name))
            .andExpect(jsonPath("$.email").value(updatedUser.email))
    }

    @Test
    fun `should delete an existing user by id`() {
        val userId = UUID.randomUUID()

        doNothing().`when`(usersService).deleteUserById(userId)

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/$userId"))
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        verify(usersService, times(1)).deleteUserById(userId)
    }
}