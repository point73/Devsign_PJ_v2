package com.devsign.test.service;

import com.devsign.test.dto.DevsignDTO;
import com.devsign.test.entity.DevsignEntity;
import com.devsign.test.repository.DevsignRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevsignService {
    private final DevsignRepository devsignRepository;

    public void save(DevsignDTO devsignDTO) {
        DevsignEntity devsignEntity = DevsignEntity.toDevsignEntity(devsignDTO);
        // 자동 값 설정
        devsignEntity.setJoinDate(LocalDateTime.now()); // 가입 날짜
        devsignEntity.setChangeDate(LocalDateTime.now()); // 변경 날짜
        devsignEntity.setLastDateTime(LocalDateTime.now()); // 마지막 로그인 날짜와 시간(기본값 설정)
        devsignEntity.setTryNumber(0); // 시도 횟수 초기화 (기본값 설정)
        devsignEntity.setWithdrawal(false); // 탈퇴 여부 기본값 (false)
        devsignRepository.save(devsignEntity);
    }

    public DevsignDTO login(DevsignDTO devsignDTO) {
        Optional<DevsignEntity> byUserId = devsignRepository.findByUserId(devsignDTO.getUserId());
        if (byUserId.isPresent()) {
            // 조회 결과가 있다(해당 아이디를 가진 회원 정보가 있다)
            DevsignEntity devsignEntity = byUserId.get();
            if (devsignEntity.getPassword().equals(devsignDTO.getPassword())) {
                // 비밀번호 일치
                // entity -> dto 변환
                DevsignDTO dto = DevsignDTO.toDevsignDTO(devsignEntity);
                return dto;
            } else {
                // 비밀번호 불일치(로그인 실패)
                return null;
            }
        } else {
            // 조회 결과가 없다(해당 아이디를 가진 회원이 없다)
            return null;
        }
    }


    public List<DevsignDTO> findAll() {
        List<DevsignEntity> devsignEntityList = devsignRepository.findAll();
        List<DevsignDTO> devsignDTOList = new ArrayList<>();
        for (DevsignEntity devsignEntity : devsignEntityList) {
            devsignDTOList.add(DevsignDTO.toDevsignDTO(devsignEntity));
        }
        return devsignDTOList;
    }

    public DevsignDTO findById(Long id) {
        Optional<DevsignEntity> optionalDevsignEntity = devsignRepository.findById(id);
        if (optionalDevsignEntity.isPresent()) {
            return DevsignDTO.toDevsignDTO(optionalDevsignEntity.get());
        } else {
            return null;
        }
    }

    public DevsignDTO updateForm(String myUserId) {
        Optional<DevsignEntity> optionalDevsignEntity = devsignRepository.findByUserId(myUserId);
        if (optionalDevsignEntity.isPresent()) {
            return DevsignDTO.toDevsignDTO(optionalDevsignEntity.get());
        } else {
            return null;
        }
    }

    public void update(DevsignDTO devsignDTO, String myUserId) {
        Optional<DevsignEntity> optionalDevsignEntity = devsignRepository.findByUserId(myUserId);

        if (optionalDevsignEntity.isPresent()) {
            DevsignEntity existingEntity = optionalDevsignEntity.get();

            // 클라이언트 입력 데이터를 업데이트
            existingEntity.setUserId(devsignDTO.getUserId());
            existingEntity.setPassword(devsignDTO.getPassword());
            existingEntity.setName(devsignDTO.getName());
            existingEntity.setUniversity(devsignDTO.getUniversity());
            existingEntity.setMajor(devsignDTO.getMajor());
            existingEntity.setStudentNumber(devsignDTO.getStudentNumber());
            existingEntity.setGender(devsignDTO.getGender());
            existingEntity.setBirth(devsignDTO.getBirth());
            existingEntity.setTel(devsignDTO.getTel());
            existingEntity.setEmail(devsignDTO.getEmail());

            existingEntity.setChangeDate(LocalDateTime.now());

            devsignRepository.save(existingEntity);
        } else {
            throw new EntityNotFoundException("ID에 해당하는 회원 정보를 찾을 수 없습니다: " + myUserId);
        }
    }

    public void delete(Long id) {
        devsignRepository.deleteById(id);
    }


    public String userIdCheck(String userId) {
        Optional<DevsignEntity> byDevsignUserId = devsignRepository.findByUserId(userId);
        if(byDevsignUserId.isPresent()) {
            // 조회결과가 있다 -> 사용할 수 없다.
            return null;
        } else {
            // 조회결과가 없다 -> 사용할 수 있다.
            return "ok";
        }
    }
}
