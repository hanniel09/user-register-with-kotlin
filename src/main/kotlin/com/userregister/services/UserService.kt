package com.userregister.services

import com.userregister.domain.users.User
import com.userregister.domain.users.UserRequestDTO
import com.userregister.domain.users.UserResponseDTO
import com.userregister.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(@Autowired val userRepository: UserRepository) {
    fun getAllUsers(): List<UserResponseDTO> {
       val  users: List<User> = userRepository.findAll().toList()
        return users.map {
            user -> UserResponseDTO(
                id = user.id ?: throw IllegalArgumentException("User ID cannot be null"),
                name = user.name,
                email = user.email,
            )
        }
    }

    fun getUserById(id: UUID): UserResponseDTO {
        val user: User = userRepository.findById(id).orElse(null)
            ?: throw RuntimeException("User with id: $id wasn't found")

        return UserResponseDTO(
            id = user.id ?: throw IllegalArgumentException("User ID cannot be null"),
            name = user.name,
            email = user.email,
        )
    }

    fun createUser(userRequestDTO: UserRequestDTO): User {
        val user  = User(
            name = userRequestDTO.name,
            email = userRequestDTO.email,
        )
        return userRepository.save(user)
    }

    fun updateUser(updatedUser: UserRequestDTO, id: UUID): UserResponseDTO {
       val existingUser:User = userRepository.findById(id).orElse(null)
           ?: throw RuntimeException("User with id: $id wasn't found")

        existingUser.name = updatedUser.name
        existingUser.email = updatedUser.email

        val savedUser = userRepository.save(existingUser)

        return UserResponseDTO(
            id = savedUser.id ?: throw IllegalArgumentException("User with id: $id wasn't found"),
            name = savedUser.name,
            email = savedUser.email
        )

    }

    fun deleteUserById(id: UUID) {
        userRepository.deleteById(id)
    }
}