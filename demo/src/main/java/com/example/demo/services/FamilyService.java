package com.example.demo.services;

import com.example.demo.mappers.FamilyMapper;
import com.example.demo.models.Family;
import com.example.demo.models.FamilyDTO;
import com.example.demo.repositories.FamilyRepository;
import com.example.demo.repositories.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FamilyService {
  private final FamilyRepository familyRepository;
  private final MemberRepository memberRepository;

  public List<Family> getAllFamilies() {
        return familyRepository.findAll();
    }

  public ResponseEntity<FamilyDTO> createFamily(FamilyDTO familyDTO) {
    FamilyDTO response = null;
    try {
      Family family = new Family();
      FamilyMapper.mapFamilyDTOToFamily(familyDTO, family);
      memberRepository.save(family.getHead());
      familyRepository.save(family);
      response = FamilyMapper.mapFamilyToFamilyDTO(family);
    } catch (Exception e) {
      e.printStackTrace();
    }

    /*
    org.springframework.dao.InvalidDataAccessApiUsageException: org.hibernate.TransientPropertyValueException
    : object references an unsaved transient instance - save the transient instance before flushing :
    com.example.demo.models.Family.head -> com.example.demo.models.Member
    Napotkany błąd "org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing"
    sugeruje, że próbujesz zapisać obiekt (Family), który zawiera odwołanie do innego obiektu (Member),
    który nie został jeszcze zapisany w bazie danych.
W twoim kodzie może się to dziać, ponieważ mapujesz FamilyDTO na Family,
ale nie zapisujesz członka rodziny. Jeśli jednostka Member ma odniesienie w modelu
Family i jest pobierana leniwie, wówczas obowiązkowe jest zapisanie Member przed zapisaniem Family.
    * */

    return ResponseEntity.ok(response);
  }
}
