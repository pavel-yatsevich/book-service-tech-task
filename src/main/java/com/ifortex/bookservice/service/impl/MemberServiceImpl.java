package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.repo.MemberRepository;
import com.ifortex.bookservice.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@Primary
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    @Override
    public Member findMember() {
        try {
            return memberRepository.findMember();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Member> findMembers() {
        try {
            return memberRepository.findMembers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
