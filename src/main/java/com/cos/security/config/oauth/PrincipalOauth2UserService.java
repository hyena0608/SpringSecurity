package com.cos.security.config.oauth;

import com.cos.security.config.auth.PrincipalDetails;
import com.cos.security.model.User;
import com.cos.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 비밀번호 인코딩 용
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입 중복 방지 메서드 만들기 위해 가져옴
    private final UserRepository userRepository;


    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration: " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 헀는지 확인 가능
        System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인을 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
        // userRequest 정보-> loadUser함수 호출 -> 구글로부터 회원 프로필 받아준다.
        System.out.println("getAttribute: " + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getClientId(); // Google
        String providerId = oAuth2User.getAttribute("sub"); // Google providerId, PK
        String username = provider + "_" + providerId; // google_2343987489324893274793284793847 => 유저 네임 충돌 방지
        String password = bCryptPasswordEncoder.encode("의미없기에아무거나적었어요"); // OAuth2로 로그인할 때 필요 없지만 일단 만듬
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        // Authentication 객체 안에 들어가게 된다. => 세션 정보로 들어간다.
       return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
