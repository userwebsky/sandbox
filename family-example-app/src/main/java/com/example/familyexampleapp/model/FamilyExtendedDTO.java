package com.example.familyexampleapp.model;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FamilyExtendedDTO extends FamilyDTO {
    private MembersDTO head;
}
