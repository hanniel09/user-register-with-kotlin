package com.userregister.domain.users

import java.util.UUID

data class UserResponseDTO(
    val id: UUID,
    val name: String,
    val email: String,
)