package com.dividends.dohi.service;


import com.dividends.dohi.exception.impl.AlreadyExistUserException;
import com.dividends.dohi.model.Auth;
import com.dividends.dohi.persist.entity.MemberEntity;
import com.dividends.dohi.persist.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder  passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("couldn't find user ->" + username));
    }
    public MemberEntity register(Auth.SignUp member){
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if (exists){
            throw new AlreadyExistUserException();
        }
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        return this.memberRepository.save(member.toEntity());
    }

    public MemberEntity authenticate(Auth.SignIn member){

        var user = this.memberRepository.findByUsername(member.getUsername())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 ID입니다."));

        if(!this.passwordEncoder.matches(member.getPassword(), user.getPassword())){
           throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
        return user;
    }

}
