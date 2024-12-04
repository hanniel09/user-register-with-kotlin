package com.userregister.domain.users

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UserRequestDTO(
    @field:NotBlank(message = "User name cannot be blank")
    @field:NotNull(message = "User name cannot be Null")
    var name: String,

    @field:NotBlank(message = "User email cannot be blank")
    @field:Email(message = "Email must be valid")
    var email: String
)