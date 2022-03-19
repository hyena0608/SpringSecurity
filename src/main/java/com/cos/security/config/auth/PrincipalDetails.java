package com.cos.security.config.auth;

import com.cos.security.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인 진행
 * 로그인 진행이 완료 되면 시큐리티 session을 만들어준다. (Security ContextHolder)
 * 시큐리티가 가지고 있는 세션에 들어갈 수 있는 오브젝트 => Authentication 타입 객체
 *  Authentication 안에 User 정보가 있어야 된다.
 * User 오브젝트 타입 => UserDetails 타입 객체
 *
 * Security Session => Authentication 객체 => (유저 정보 저장할 때) UserDetails(PrincipalDetails) 타입
 * Security Session에 Authentication 객체를 꺼내면 UserDetails 타입을 받아 쓸 수 있다.
 *
 * @Override
 */

@Data
public class PrincipalDetails implements UserDetails {

    private User user; // 콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 User의 권한 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        // 1년 동안 로그인 X -> 휴먼 계정으로 전환
        // 현재시간 - 로그인시간 => 1년을 초과하면 return false;

        return true;
    }
}
