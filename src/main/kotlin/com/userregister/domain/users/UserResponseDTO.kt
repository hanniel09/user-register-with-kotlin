package com.userregister.domain.users

import java.util.UUID

class UserResponseDTO(
    var id: UUID,
    var name: String,
    var email: String,
)