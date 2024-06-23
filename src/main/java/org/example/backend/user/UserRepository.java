package org.example.backend.user;


import org.example.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUserId(String userid);

    Boolean existsByUserNickname(String nickname);

    //loginId을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    User findByUserId(String loginId);
}
