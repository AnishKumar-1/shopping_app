package com.authentication.repository;

import com.authentication.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserModel,Long> {
    // 'UserModel' is the Java class
    Optional<UserModel> findByEmail(String email);
    public boolean existsByEmail(String email);
}
