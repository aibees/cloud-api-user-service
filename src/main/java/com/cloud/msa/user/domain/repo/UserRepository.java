package com.cloud.msa.user.domain.repo;

import com.cloud.msa.user.domain.entities.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserMaster, Long> {
    public UserMaster findByEmailAndEmailPostfix(String email, String email_postfilx);

    public List<UserMaster> findAll();

}
