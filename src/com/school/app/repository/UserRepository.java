// فایل: com/school/app/repository/UserRepository.java
package com.school.app.repository;

import com.school.app.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    User save(User user);
    boolean existsByUsername(String username);
}