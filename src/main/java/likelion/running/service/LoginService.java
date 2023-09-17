package likelion.running.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.running.domain.Kakao.KakaoResult;
import likelion.running.domain.member.Authority;
import likelion.running.domain.member.Member;
import likelion.running.domain.member.MemberAuthority;
import likelion.running.domain.member.MemberJpaRepository;
import likelion.running.domain.signUp.SignUpResult;
import likelion.running.domain.token.KakaoToken;
import likelion.running.web.dto.memberDto.KakaoSignUpDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@Service
public class LoginService implements UserDetailsService {
    private final MemberJpaRepository memberJpaRepository;
    @Autowired
    public LoginService(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {
        return memberJpaRepository.findOneWithAuthoritiesByMemberId(memberName)
                .map(member -> createMember(memberName,member))
                .orElseThrow(()->new UsernameNotFoundException(memberName + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createMember(String memberName, Member member){
        if (!member.isActivated()) {
            throw new RuntimeException(memberName + " -> 활성화되어 있지 않습니다.");
        }

        List<GrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().getAuthorityName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(member.getMemberId(),
                member.getPassword(),
                grantedAuthorities);
    }

}
