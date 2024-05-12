package com.example.familyexampleapp.translator;


import com.example.familyexampleapp.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public abstract class TranslatorMemberDBToMemberDTO {

    public MembersDTO toMemberDTO(MembersDB membersDB){
        MembersDTO membersDTO = toDTO(membersDB);
        return membersDTO;
    }
    public MembersExtendedDTO toMemberExtendedDTO(MembersDB membersDB){
        MembersExtendedDTO membersDTO = toExtendedDTO(membersDB);
        return membersDTO;
    }


    @Mappings({
            @Mapping(source = "name",target = "name"),
            @Mapping(source = "age",target = "age"),
            @Mapping(source = "gender",target = "gender")
    })
    protected abstract MembersDTO toDTO(MembersDB membersDB);
    @Mappings({
            @Mapping(source = "name",target = "name"),
            @Mapping(source = "age",target = "age"),
            @Mapping(source = "gender",target = "gender"),
            @Mapping(expression = "java(toFamilyDTODTO(membersDB.getFamilyId()))",target = "family")
    })
    protected abstract MembersExtendedDTO toExtendedDTO(MembersDB membersDB);

    @Mappings({
            @Mapping(source = "name",target = "name"),
            @Mapping(source = "origin",target = "origin")
    })
    protected abstract FamilyDTO toFamilyDTODTO(FamilyDB familyDB);
}
