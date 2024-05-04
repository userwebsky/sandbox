package com.example.demo.controllers;

import com.example.demo.models.Family;
import com.example.demo.models.FamilyDTO;
import com.example.demo.services.FamilyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/family")
@AllArgsConstructor
public class FamilyController {

  private final FamilyService familyService;

  @GetMapping("/all")
  public ResponseEntity<List<Family>> getAllFamilies() {
      return familyService.getAllFamilies();
  }

  @GetMapping("/{id}")
  public ResponseEntity<FamilyDTO> getFamilyById(@PathVariable long id) {
    return familyService.getFamilyById(id);
  }

  @PostMapping("/")
  public ResponseEntity<FamilyDTO> createFamily(@RequestBody @Validated FamilyDTO familyDTO) {
      return familyService.createFamily(familyDTO);
  }

  @PutMapping("/{id}")
    public ResponseEntity<FamilyDTO> updateFamily(@PathVariable long id, @RequestBody @Validated FamilyDTO familyDTO) {
        return familyService.updateFamily(id, familyDTO);
    }

  @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable long id) {
      return familyService.deleteFamily(id);
    }
}
