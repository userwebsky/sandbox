package com.example.demo.controllers;

import com.example.demo.models.FamilyDTO;
import com.example.demo.services.FamilyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/family")
@AllArgsConstructor
public class FamilyController {

  private final FamilyService familyService;

  @GetMapping("/all")
  public List<FamilyDTO> getAllFamilies() {
    List<FamilyDTO> families = new ArrayList<>();
      return families;
  }

  @GetMapping("/{uid}")
  public FamilyDTO getFamilyByUid(@PathVariable String uid) {
    List<FamilyDTO> families = new ArrayList<>();
    return families.stream()
              //.filter(familyDTO -> familyDTO.getUid().equals(uid))
              .findFirst()
              .orElse(null);
  }

  @PostMapping("/")
  public ResponseEntity<FamilyDTO> createFamily(@RequestBody @Validated FamilyDTO familyDTO) {
      return familyService.createFamily(familyDTO);
  }

  @PatchMapping("/{uid}")
  public FamilyDTO updateFamilyName(@PathVariable String uid, @RequestParam String name) {
        FamilyDTO familyDTO = getFamilyByUid(uid);
        if (familyDTO != null) {
            familyDTO.setName(name);
        }
        return familyDTO;
  }


  @PutMapping("/{uid}")
  public FamilyDTO updateFamily(@PathVariable String uid, @RequestBody @Validated FamilyDTO updatedFamilyDTO) {
        FamilyDTO familyDTO = getFamilyByUid(uid);
        if (familyDTO != null) {
            familyDTO.setName(updatedFamilyDTO.getName());
            //familyDTO.setMemberDTOS(updatedFamilyDTO.getMemberDTOS());
        }
        return familyDTO;
  }

  @DeleteMapping("/{uid}")
    public void deleteFamily(@PathVariable String uid) {
        List<FamilyDTO> families = new ArrayList<>();
        //families.removeIf(familyDTO -> familyDTO.getUid().equals(uid));
    }
}
