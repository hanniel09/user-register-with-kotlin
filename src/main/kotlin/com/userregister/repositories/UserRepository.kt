package com.userregister.repositories

import com.userregister.domain.users.User
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserRepository : CrudRepository<User, UUID>