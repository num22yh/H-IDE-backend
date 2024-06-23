package org.example.backend.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.user.dto.LoginReqDto;
import org.example.backend.user.dto.SingUpReqDto;
import org.example.backend.user.jwt.JWTUtil;
import org.example.backend.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @PostMapping("/check-userid")
    public ResponseEntity<String> checkUserid(@RequestBody String userid) {
        boolean isExist = userService.isUseridExist(userid);
        if (!isExist) {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용 중인 아이디입니다.");
        }

    }

    @PostMapping("/check-nickname")
    public ResponseEntity<String> checkNickname(@RequestBody String nickname) {
        boolean isExist = userService.isNicknameExist(nickname);
        if (!isExist) {
            return ResponseEntity.ok("사용 가능한 닉네임입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용 중인 닉네임입니다.");
        }
    }

    @PostMapping("/send-verificationCode")
    public ResponseEntity<String> sendVerificationCode(@RequestBody String email) {
        boolean isEmailSend = userService.sendVerificationCode(email);
        if (isEmailSend) {
            return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송에 실패했습니다.");
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Validated SingUpReqDto singUpReqDto) {

        if (userService.isUseridExist(singUpReqDto.getUserid())) {
            throw new RuntimeException("Userid already exists: " + singUpReqDto.getUserid());
        }
        if (userService.isNicknameExist(singUpReqDto.getUsername())) {
            throw new RuntimeException("Username already exists: " + singUpReqDto.getUsername());
        }

        boolean isSingUp = userService.signUp(singUpReqDto);

        if (isSingUp) {
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 가입에 실패했습니다.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginReqDto loginReqDto) {
        try {
            // 스프링 시큐리티에서 userid password를 검증하기 위해 token(객체)에 담아야 함
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginReqDto.loginId(), loginReqDto.password(), null);

            // token에 담은 검증을 위한 AuthenticationManager(검증담당)로 전달
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 인증 객체를 SecurityContext에 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // JWT 발급
            String token = jwtUtil.createJwt(loginReqDto.loginId(), authentication.getAuthorities().iterator().next().getAuthority(), 60 * 60 * 10L);

            // 로그인 기록 갱신
           userService.login(loginReqDto);

            // 응답 헤더에 JWT 추가
            String id = loginReqDto.loginId();
            return ResponseEntity.ok().header("Authorization", "Hide " + token).header("UserId",id).body("로그인에 성공하셨습니다.");


        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인에 실패했습니다.");
        }
    }
}