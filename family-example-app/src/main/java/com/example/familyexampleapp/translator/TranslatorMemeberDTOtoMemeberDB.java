package com.example.familyexampleapp.translator;

import com.example.familyexampleapp.model.MembersDB;
import com.example.familyexampleapp.model.MembersDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public abstract class TranslatorMemeberDTOtoMemeberDB {

  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "name", source = "name"),
    @Mapping(target = "age", source = "age"),
    @Mapping(target = "gender", source = "gender")
  })
  protected abstract MembersDB toMemebersDB(MembersDTO membersDTO);
}
