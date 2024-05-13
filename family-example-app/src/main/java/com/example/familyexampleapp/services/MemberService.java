package com.example.familyexampleapp.services;

import com.example.familyexampleapp.model.MembersDB;
import com.example.familyexampleapp.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
  MemberRepository memberRepository;

  public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  public void save(MembersDB membersDB) {
    memberRepository.save(membersDB);
  }

  public List<MembersDB> getMembersByName(String name) {
    return (List<MembersDB>) memberRepository.findByName(name);
  }
}
