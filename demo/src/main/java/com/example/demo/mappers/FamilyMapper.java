package com.example.demo.mappers;

import com.example.demo.models.Family;
import com.example.demo.models.FamilyDTO;
import com.example.demo.models.Member;
import com.example.demo.models.MemberDTO;
import org.springframework.stereotype.Component;

@Component
public class FamilyMapper {
  public static FamilyDTO mapFamilyToFamilyDTO(Family family) {
    return new FamilyDTO(family.getId(), family.getName(), family.getOrigin(), FamilyMapper.mapMemberToMemberDTO(family.getHead()));
  }

  private static MemberDTO mapMemberToMemberDTO(Member member) {
    return new MemberDTO(member.getName(), member.getAge(), member.getGender());
  }

  public static void mapFamilyDTOToFamily(FamilyDTO familyDTO, Family family) {
    family.setId(familyDTO.getId());
      family.setName(familyDTO.getName());
      family.setOrigin(familyDTO.getOrigin());
      family.setHead(FamilyMapper.mapMemberDTOToMember(familyDTO.getMemberDTO()));
  }

  public static Member mapMemberDTOToMember(MemberDTO memberDTO) {
    Member member = new Member();
    member.setGender(memberDTO.getGender());
    member.setName(memberDTO.getName());
    member.setAge(memberDTO.getAge());
    return member;
  }
}
