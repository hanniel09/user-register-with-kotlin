import com.userregister.domain.users.User
import com.userregister.domain.users.UserRequestDTO
import com.userregister.domain.users.UserResponseDTO
import com.userregister.repositories.UserRepository
import com.userregister.services.UserService
import io.mockk.*
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

class UserServiceTest {
    private val userRepository: UserRepository = mockk()
    private val userService = UserService(userRepository)


    @Test
    fun `should return all users`() {
        val user1 = User(id = UUID.randomUUID(), name = "John Doe", email = "john.doe@example.com")
        val user2 = User(id = UUID.randomUUID(), name = "Jane Doe", email = "jane.doe@example.com")
        val users = listOf(user1, user2)
        every { userRepository.findAll() } returns users

        val result = userService.getAllUsers()

        val expected = users.map {
            UserResponseDTO(id = it.id!!, name = it.name, email = it.email)
        }

        assertEquals(expected, result)

        verify { userRepository.findAll() }
    }

    @Test
    fun `should return user when user exists`() {
        val userId = UUID.randomUUID()
        val user = User(id = userId, name = "John Doe", email = "john.doe@example.com")
        val userResponseDTO = UserResponseDTO(id = userId, name = "John Doe", email = "john.doe@example.com")
        every { userRepository.findById(userId) } returns Optional.of(user)


        val result = userService.getUserById(userId)

        assertEquals(userResponseDTO, result)

        verify { userRepository.findById(userId) }

    }

    @Test
    fun `should throw exception when user not found`() {
        val userId = UUID.randomUUID()

        every { userRepository.findById(userId) } returns Optional.empty()

        val exception = assertThrows<RuntimeException> {
            userService.getUserById(userId)
        }

        assertEquals(exception.message, "User with id: $userId wasn't found")

        verify { userRepository.findById(userId) }
    }

    @Test
    fun `should create user successfully`(){
        val userRequestDTO = UserRequestDTO(name = "John Doe", email = "john.doe@example.com")
        val user = User(id = UUID.randomUUID(), name = userRequestDTO.name, email = userRequestDTO.email)

        every { userRepository.save(any<User>()) } returns user

        val result = userService.createUser(userRequestDTO)

        val expected = User(id = user.id!!, name = user.name, email = user.email)

        assertEquals(expected, result)

        verify { userRepository.save(any<User>()) }
    }

    @Test
    fun `should update user successfully`(){
        val userId = UUID.randomUUID()
        val updateUserRequestDTO = UserRequestDTO(name = "John Doe Updated", email = "john.doe.updated@example.com")
        val existingUser = User(id = userId, name = "John Doe", email = "john.doe@example.com")
        val updatedUser = User(id = existingUser.id, name = updateUserRequestDTO.name, email = updateUserRequestDTO.email)

        every { userRepository.findById(userId) } returns Optional.of(existingUser)

        every { userRepository.save(any()) } returns updatedUser

        val result = userService.updateUser(updateUserRequestDTO, userId)

        val expected = UserResponseDTO(id = updatedUser.id!!, name = updatedUser.name, email = updatedUser.email)


        assertEquals(expected, result)

        verify { userRepository.findById(userId) }
        verify { userRepository.save(any()) }

    }

    @Test
    fun `should delete User by id successfully`(){
        val userId = UUID.randomUUID()

        every { userRepository.deleteById(userId) } just runs

        userService.deleteUserById(userId)

        verify { userRepository.deleteById(userId) }
    }
}
