package com.astik.repository;

import java.util.Optional;
import com.astik.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> 
{
	Optional<User> findByUserId(String userId);
	User findByUserIdAndPassword(String userId,String password);
	boolean existsByUserId(String userId);
}
