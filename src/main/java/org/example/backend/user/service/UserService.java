package org.example.backend.user.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.user.UserRepository;
import org.example.backend.user.dto.LoginReqDto;
import org.example.backend.user.dto.SingUpReqDto;
import org.example.backend.user.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean isUseridExist(String userid) {
        return userRepository.existsByUserId(userid);
    }

    public boolean isNicknameExist(String nickname) {
        return userRepository.existsByUserNickname(nickname);
    }

    public boolean sendVerificationCode(String email) {
        try {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User findUser(String loginId) {
        return userRepository.findByUserId(loginId);
    }

    @Transactional
    public boolean signUp(SingUpReqDto singUpReqDto) {
        User user = new User();
        user.setUserId(singUpReqDto.getUserid());
        user.setUserName(singUpReqDto.getUsername());
        user.setUserNickname(singUpReqDto.getNickname());
        user.setUserPassword(bCryptPasswordEncoder.encode(singUpReqDto.getPassword()));
        user.setUserEmail(singUpReqDto.getEmail());
        user.setUserRole("ROLE_ADMIN");

        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean login(LoginReqDto loginReqDto) {
        User user = userRepository.findByUserId(loginReqDto.loginId());
        user.setLoginAt(Timestamp.from(Instant.now()));
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
