package com.example.familyexampleapp.mediator;

import com.example.familyexampleapp.model.*;
import com.example.familyexampleapp.services.FamilyService;
import com.example.familyexampleapp.services.MemberService;
import com.example.familyexampleapp.translator.TranslatorFamilyDBtoFamilyDTO;
import com.example.familyexampleapp.translator.TranslatorFamilyTDOtoFamilyDB;
import com.example.familyexampleapp.translator.TranslatorMemberDBToMemberDTO;
import org.springframework.stereotype.Component;

@Component
public class FamilyMediator {
  TranslatorFamilyDBtoFamilyDTO translatorFamilyDBtoFamilyDTO;
  TranslatorMemberDBToMemberDTO memberDBToMemberDTO;
  TranslatorFamilyTDOtoFamilyDB translatorFamilyTDOtoFamilyDB;
  FamilyService familyService;
  MemberService memberService;

  public FamilyMediator(
    TranslatorFamilyDBtoFamilyDTO translatorFamilyDBtoFamilyDTO,
    TranslatorMemberDBToMemberDTO memberDBToMemberDTO,
    FamilyService familyService,
    TranslatorFamilyTDOtoFamilyDB translatorFamilyTDOtoFamilyDB,
    MemberService memberService
  ) {
    this.translatorFamilyDBtoFamilyDTO = translatorFamilyDBtoFamilyDTO;
    this.memberDBToMemberDTO = memberDBToMemberDTO;
    this.familyService = familyService;
    this.translatorFamilyTDOtoFamilyDB = translatorFamilyTDOtoFamilyDB;
    this.memberService = memberService;
  }

  public void saveFamily(FamilyDTO familyDTO) {
    FamilyDB familyDB = translatorFamilyTDOtoFamilyDB.toFamilyDB(familyDTO);
      familyService.save(familyDB);
  }

  public void updateFamily(FamilyExtendedDTO familyDTO) {
    FamilyDB familyDB = translatorFamilyTDOtoFamilyDB.toFamilyDB(familyDTO);
    familyService.save(familyDB);
  }

  public void saveMember(MembersDTO memberDTO) {
    MembersDB membersDB = (memberDTO);
    memberService.save(membersDB);
  }

  public void updateMember(MembersExtendedDTO memberDTO) {
    MembersDB membersDB = (memberDTO);
    memberService.save(membersDB);
  }
}
