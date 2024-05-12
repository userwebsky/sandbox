package com.example.familyexampleapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MembersExtendedDTO extends MembersDTO{
    private FamilyDTO family;
}
