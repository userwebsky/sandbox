package com.example.familyexampleapp.mediator;

import com.example.familyexampleapp.model.FamilyDB;
import com.example.familyexampleapp.model.FamilyDTO;
import com.example.familyexampleapp.model.FamilyExtendedDTO;
import com.example.familyexampleapp.services.FamilyService;
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

  public FamilyMediator(
    TranslatorFamilyDBtoFamilyDTO translatorFamilyDBtoFamilyDTO,
    TranslatorMemberDBToMemberDTO memberDBToMemberDTO,
    FamilyService familyService,
    TranslatorFamilyTDOtoFamilyDB translatorFamilyTDOtoFamilyDB
  ) {
    this.translatorFamilyDBtoFamilyDTO = translatorFamilyDBtoFamilyDTO;
    this.memberDBToMemberDTO = memberDBToMemberDTO;
    this.familyService = familyService;
    this.translatorFamilyTDOtoFamilyDB = translatorFamilyTDOtoFamilyDB;
  }

  public void saveFamily(FamilyDTO familyDTO) {
    FamilyDB familyDB = translatorFamilyTDOtoFamilyDB.toFamilyDB(familyDTO);
      familyService.save(familyDB);
  }

  public void updateFamily(FamilyExtendedDTO familyDTO) {
    FamilyDB familyDB = translatorFamilyTDOtoFamilyDB.toFamilyDB(familyDTO);
    familyService.save(familyDB);
  }
}
