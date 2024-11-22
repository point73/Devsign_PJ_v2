package com.devsign.test.dto;

import com.devsign.test.entity.DevsignEntity;
import com.devsign.test.entity.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password") // 비밀번호를 제외한 문자열 출력
public class DevsignDTO {
    private Long id;
    private String userId;
    private String password;
    private String name;
    private String university;
    private String major;
    private String studentNumber;
    private String tel;
    private Gender gender;
    private LocalDate birth;
    private String email;

    public static DevsignDTO toDevsignDTO(DevsignEntity devsignEntity) {
        DevsignDTO devsignDTO = new DevsignDTO();
        devsignDTO.setId(devsignEntity.getId());
        devsignDTO.setUserId(devsignEntity.getUserId());
        devsignDTO.setPassword(devsignEntity.getPassword());
        devsignDTO.setName(devsignEntity.getName());
        devsignDTO.setUniversity(devsignEntity.getUniversity());
        devsignDTO.setMajor(devsignEntity.getMajor());
        devsignDTO.setStudentNumber(devsignEntity.getStudentNumber());
        devsignDTO.setTel(devsignEntity.getTel());
        devsignDTO.setGender(devsignEntity.getGender());
        devsignDTO.setBirth(devsignEntity.getBirth());
        devsignDTO.setEmail(devsignEntity.getEmail());
        return devsignDTO;
    }
}
