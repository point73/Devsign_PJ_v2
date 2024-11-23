package com.devsign.test.entity;

import com.devsign.test.dto.DevsignDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "devsign_table")
public class DevsignEntity {
    @Id // pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false, length = 50)
    private String userId; // "userId" 필드

    @Column(name = "password", nullable = false)
    private String password; // "password" 필드

    @Column(name = "name", nullable = false, length = 50)
    private String name; // "name" 필드

    @Column(name = "university")
    private String university; // "university" 필드

    @Column(name = "major")
    private String major; // "major" 필드

    @Column(name = "student_number")
    private String studentNumber; // "student_number" 필드

    @Column(name = "join_date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime joinDate; // "join_date" 필드

    @Column(name = "withdrawal", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean withdrawal; // "withdrawal" 필드

    @Column(name = "withdrawal_date")
    private LocalDateTime withdrawalDate; // "withdrawal_date" 필드

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender; // "gender" 필드

    @Column(name = "birth")
    private LocalDate birth; // "birth" 필드

    @Column(name = "tel")
    private String tel; // "tel" 필드

    @Column(name = "email")
    private String email; // "email" 필드

    @Column(name = "change_date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime changeDate;

    @Column(name = "last_date_time")
    private LocalDateTime lastDateTime; // "last_date" 필드

    @Column(name = "try_number", columnDefinition = "TINYINT UNSIGNED DEFAULT 0")
    private int tryNumber; // "try_number" 필드


    public static DevsignEntity toDevsignEntity(DevsignDTO devsignDTO) {
        DevsignEntity devsignEntity = new DevsignEntity();
        devsignEntity.setUserId(devsignDTO.getUserId());
        devsignEntity.setPassword(devsignDTO.getPassword());
        devsignEntity.setName(devsignDTO.getName());
        devsignEntity.setUniversity(devsignDTO.getUniversity());
        devsignEntity.setMajor(devsignDTO.getMajor());
        devsignEntity.setStudentNumber(devsignDTO.getStudentNumber());
        devsignEntity.setGender(devsignDTO.getGender());
        devsignEntity.setBirth(devsignDTO.getBirth());
        devsignEntity.setTel(devsignDTO.getTel());
        devsignEntity.setEmail(devsignDTO.getEmail());
        return devsignEntity;
    }
}
