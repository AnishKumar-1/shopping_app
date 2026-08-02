package com.authentication.repository;

import com.authentication.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<UserRoles,Long> {
    UserRoles findByRoleName(String roleName);
}
