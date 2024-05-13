package com.example.familyexampleapp.mediator;

import com.example.familyexampleapp.translator.TranslatorFamilyDBtoFamilyDTO;
import com.example.familyexampleapp.translator.TranslatorMemberDBToMemberDTO;
import org.springframework.stereotype.Component;

@Component
public class FamilyMediator {
  TranslatorFamilyDBtoFamilyDTO translatorFamilyDBtoFamilyDTO;
  TranslatorMemberDBToMemberDTO memberDBToMemberDTO;

  public FamilyMediator(
    TranslatorFamilyDBtoFamilyDTO translatorFamilyDBtoFamilyDTO,
    TranslatorMemberDBToMemberDTO memberDBToMemberDTO
  ) {
    this.translatorFamilyDBtoFamilyDTO = translatorFamilyDBtoFamilyDTO;
    this.memberDBToMemberDTO = memberDBToMemberDTO;
  }
}
