package com.devsign.test.service;

import com.devsign.test.dto.DevsignDTO;
import com.devsign.test.entity.DevsignEntity;
import com.devsign.test.repository.DevsignRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevsignService {
    private final DevsignRepository devsignRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 로직
    @Transactional
    public void save(DevsignDTO devsignDTO) {
        DevsignEntity devsignEntity = DevsignEntity.toDevsignEntity(devsignDTO);
        String encodedPassword = passwordEncoder.encode(devsignDTO.getPassword());

        devsignEntity.setPassword(encodedPassword);
        // 자동 값 설정
        devsignEntity.setJoinDate(LocalDateTime.now()); // 가입 날짜
        devsignEntity.setChangeDate(LocalDateTime.now()); // 변경 날짜
        devsignEntity.setLastDateTime(LocalDateTime.now()); // 마지막 로그인 날짜와 시간(기본값 설정)
        devsignEntity.setTryNumber(0); // 시도 횟수 초기화 (기본값 설정)
        devsignEntity.setWithdrawal(false); // 탈퇴 여부 기본값 (false)
        devsignRepository.save(devsignEntity);
    }

    // 로그인 로직
    public DevsignDTO login(DevsignDTO devsignDTO) {
        Optional<DevsignEntity> byUserId = devsignRepository.findByUserId(devsignDTO.getUserId());
        if (byUserId.isPresent() && !byUserId.get().isWithdrawal()) {
            // 조회 결과가 있다(해당 아이디를 가진 회원 정보가 있다)
            DevsignEntity devsignEntity = byUserId.get();

            // 계정 잠금 여부 확인
            if(devsignEntity.getTryNumber() >= 5) {
                LocalDateTime lockTime = devsignEntity.getLockTime();
                if(lockTime != null  && lockTime.plusMinutes(5).isAfter(LocalDateTime.now())) {
                    // 5분 이내라면 로그인 차단
                    return null;
                } else {
                    // 5분이 지나면 시도 횟수 초기화
                    devsignEntity.setTryNumber(0);
                    devsignEntity.setLockTime(null);
                    devsignRepository.save(devsignEntity);
                }
            }

            // 비밀번호 확인
            if (passwordEncoder.matches(devsignDTO.getPassword(), devsignEntity.getPassword())) {
                // 비밀번호 일치
                // 로그인 성공 시 시도 횟수 초기화
                devsignEntity.setTryNumber(0);
                devsignRepository.save(devsignEntity);

                // entity -> dto 변환
                DevsignDTO dto = DevsignDTO.toDevsignDTO(devsignEntity);
                return dto;
            } else {
                devsignEntity.setTryNumber(devsignEntity.getTryNumber() + 1);
                if (devsignEntity.getTryNumber() >= 5) {
                    // 로그인 실패가 5회 이상이면 잠금 시간 설정
                    devsignEntity.setLockTime(LocalDateTime.now());
                }
                devsignRepository.save(devsignEntity);
                // 비밀번호 불일치(로그인 실패)
                return null;
            }
        } else {
            // 조회 결과가 없다(해당 아이디를 가진 회원이 없다)
            return null;
        }
    }

    // 모든 계정 찾기
    public List<DevsignDTO> findAll() {
        List<DevsignEntity> devsignEntityList = devsignRepository.findAllByWithdrawal(false);
        List<DevsignDTO> devsignDTOList = new ArrayList<>();
        for (DevsignEntity devsignEntity : devsignEntityList) {
            devsignDTOList.add(DevsignDTO.toDevsignDTO(devsignEntity));
        }
        return devsignDTOList;
    }

    // 계정 아이디로 찾기
    public DevsignDTO findById(Long id) {
        Optional<DevsignEntity> optionalDevsignEntity = devsignRepository.findById(id);
        if (optionalDevsignEntity.isPresent()) {
            return DevsignDTO.toDevsignDTO(optionalDevsignEntity.get());
        } else {
            return null;
        }
    }

    // 정보 수정을 위한 계정 정보 불러오기
    public DevsignDTO updateForm(String myUserId) {
        Optional<DevsignEntity> optionalDevsignEntity = devsignRepository.findByUserId(myUserId);
        if (optionalDevsignEntity.isPresent()) {
            return DevsignDTO.toDevsignDTO(optionalDevsignEntity.get());
        } else {
            return null;
        }
    }

    // 정보 수정 로직
    @Transactional
    public void update(DevsignDTO devsignDTO, String myUserId) {
        Optional<DevsignEntity> optionalDevsignEntity = devsignRepository.findByUserId(myUserId);

        if (optionalDevsignEntity.isPresent()) {
            DevsignEntity existingEntity = optionalDevsignEntity.get();
            String encodedPassword = passwordEncoder.encode(devsignDTO.getPassword());

            // 클라이언트 입력 데이터를 업데이트
            existingEntity.setUserId(devsignDTO.getUserId());
            existingEntity.setPassword(encodedPassword);
            existingEntity.setName(devsignDTO.getName());
            existingEntity.setUniversity(devsignDTO.getUniversity());
            existingEntity.setMajor(devsignDTO.getMajor());
            existingEntity.setStudentNumber(devsignDTO.getStudentNumber());
            existingEntity.setGender(devsignDTO.getGender());
            existingEntity.setBirth(devsignDTO.getBirth());
            existingEntity.setTel(devsignDTO.getTel());
            existingEntity.setEmail(devsignDTO.getEmail());

            existingEntity.setChangeDate(LocalDateTime.now());

        } else {
            throw new EntityNotFoundException("ID에 해당하는 회원 정보를 찾을 수 없습니다: " + myUserId);
        }
    }

    // 탈퇴 로직
    @Transactional
    public void delete(Long id) {
        Optional<DevsignEntity> optionalDevsignEntity = devsignRepository.findById(id);
        if (optionalDevsignEntity.isPresent()) {
            DevsignEntity existingEntity = optionalDevsignEntity.get();

            existingEntity.setWithdrawal(true);
            existingEntity.setWithdrawalDate(LocalDateTime.now());
        }
        else {
            throw new EntityNotFoundException("회원 정보를 찾을 수 없습니다: ");
        }
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
