package com.studentsystemapp.repo;

import com.studentsystemapp.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<BaseUser, Long> {

    public int deleteByUsername(String username);
    public Optional<BaseUser> getByUsername(String username);


    public Optional<BaseUser> findByUsername(String username);

    Optional<BaseUser> findByEmail(String email);
}
