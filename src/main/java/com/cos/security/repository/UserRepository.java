package com.cos.security.repository;

import com.cos.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있다.
// @Repository 가 없어도 IOC -> JpaRepository 상속하고 있으니
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * findBy 규칙 -> Username 문법
     * select * from user where username = 1?
     */
    public User findByUsername(String username); // JPA Query Method
}
