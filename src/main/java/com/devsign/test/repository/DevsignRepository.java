package com.devsign.test.repository;

import com.devsign.test.entity.DevsignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DevsignRepository extends JpaRepository<DevsignEntity, Long> {
    // 이메일로 회원 정보 조회 (select * from member_table where member_email=?)
    Optional<DevsignEntity> findByEmail(String email);

    Optional<DevsignEntity> findByUserId(String userId);

    // 탈퇴를 위한 조회 메서드
    List<DevsignEntity> findAllByWithdrawal(boolean withdrawal);
    Optional<DevsignEntity> findByWithdrawal(boolean withdrawal);
}
