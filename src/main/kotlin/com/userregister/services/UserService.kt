package com.userregister.services

import com.userregister.domain.users.User
import com.userregister.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(@Autowired val userRepository: UserRepository) {
    fun getAllUsers(): List<User> {
        return userRepository.findAll().toList()
    }

    fun getUserById(id: UUID): User {
        return userRepository.findById(id).orElse(null)
            ?: throw RuntimeException("User with id: $id wasn't found")
    }

    fun createUser(user: User): User {
        val createdUser = userRepository.save(user)
        return createdUser
    }

    fun updateUser(updatedUser: User, id: UUID): User {
       val existingUser = getUserById(id)

        existingUser.name = updatedUser.name
        existingUser.email = updatedUser.email

        return userRepository.save(existingUser)

    }

    fun deleteUserById(id: UUID) {
        userRepository.deleteById(id)
    }
}