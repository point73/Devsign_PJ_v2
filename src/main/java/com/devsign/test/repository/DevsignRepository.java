package com.devsign.test.repository;

import com.devsign.test.entity.DevsignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DevsignRepository extends JpaRepository<DevsignEntity, Long> {
    // 이메일로 회원 정보 조회 (select * from member_table where member_email=?)
    Optional<DevsignEntity> findByEmail(String email);

    Optional<DevsignEntity> findByUserId(String userId);
}
