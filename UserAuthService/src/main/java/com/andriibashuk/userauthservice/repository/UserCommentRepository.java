package com.andriibashuk.userauthservice.repository;

import com.andriibashuk.userauthservice.entity.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCommentRepository extends JpaRepository<UserComment, Long> {
}
