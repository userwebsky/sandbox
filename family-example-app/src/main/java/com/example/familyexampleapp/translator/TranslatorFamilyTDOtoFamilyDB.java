package com.example.familyexampleapp.translator;

import com.example.familyexampleapp.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public abstract class TranslatorFamilyTDOtoFamilyDB {

  public FamilyDB toFamilyDB(FamilyExtendedDTO familyExtendedDTO) {
    FamilyDB familyDB = toFamilyDB(familyExtendedDTO);
    familyDB.getHead().setFamilyId(familyDB);
    return familyDB;
  }

  public FamilyDB toFamilyDB(FamilyDTO familyDTO) {
    FamilyDB familyDB = toFamilyDB(familyDTO);

    return familyDB;
  }

  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "head", ignore = true),
    @Mapping(target = "name", source = "name"),
    @Mapping(target = "origin", source = "origin")
  })
  protected abstract FamilyDB toFamilyDBMap(FamilyDTO familyDTO);

  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "name", source = "name"),
    @Mapping(target = "origin", source = "origin"),
    @Mapping(target = "head", expression = "java(toFamilyDBMap(familyExtendedDTO.getHead()))")
  })
  protected abstract FamilyDB toFamilyDBMap(FamilyExtendedDTO familyExtendedDTO);

  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "name", source = "name"),
    @Mapping(target = "age", source = "age"),
    @Mapping(target = "gender", source = "gender"),
    @Mapping(target = "familyId", ignore = true),
  })
  protected abstract MembersDB toFamilyDBMap(MembersDTO membersDTO);
}
