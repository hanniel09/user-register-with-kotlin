package com.userregister.controllers

import com.userregister.domain.users.User
import com.userregister.domain.users.UserRequestDTO
import com.userregister.domain.users.UserResponseDTO
import com.userregister.services.UsersService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController()
@RequestMapping("/api/users")
class UsersController (@Autowired val usersService: UsersService) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponseDTO>> {
        val users = usersService.getAllUsers()
        return ResponseEntity(users, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: UUID): ResponseEntity<UserResponseDTO> {
        val user = usersService.getUserById(id)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PostMapping("/register")
    fun createUser(@RequestBody @Valid userRequestDTO: UserRequestDTO): ResponseEntity<User> {
        val user =usersService.createUser(userRequestDTO)
        return ResponseEntity(user, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateUser(@RequestBody @Valid userRequestDTO: UserRequestDTO, @PathVariable id:UUID): ResponseEntity<UserResponseDTO> {
        val user = usersService.updateUser(userRequestDTO, id)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID): ResponseEntity<Void> {
        usersService.deleteUserById(id)
        return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
    }
}