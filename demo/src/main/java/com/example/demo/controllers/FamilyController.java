package com.example.demo.controllers;

import com.example.demo.models.Family;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/family")
public class FamilyController {

  @GetMapping("/all")
  public List<Family> getAllFamilies() {
    List<Family> families = new ArrayList<>();
    families.add(new Family("1", "Smith", new ArrayList<>()));
      families.add(new Family("2", "Johnson", new ArrayList<>()));
      families.add(new Family("3", "Williams", new ArrayList<>()));
      return families;
  }

  @GetMapping("/{uid}")
  public Family getFamilyByUid(@PathVariable String uid) {
    List<Family> families = new ArrayList<>();
    families.add(new Family("1", "Smith", new ArrayList<>()));
    families.add(new Family("2", "Johnson", new ArrayList<>()));
    families.add(new Family("3", "Williams", new ArrayList<>()));
    return families.stream()
              .filter(family -> family.getUid().equals(uid))
              .findFirst()
              .orElse(null);
  }

  @PostMapping("/")
  public Family createFamily(@RequestBody @Validated Family family) {

      return family;
  }

  @PatchMapping("/{uid}")
  public Family updateFamilyName(@PathVariable String uid, @RequestParam String name) {
        Family family = getFamilyByUid(uid);
        if (family != null) {
            family.setName(name);
        }
        return family;
  }


  @PutMapping("/{uid}")
  public Family updateFamily(@PathVariable String uid, @RequestBody @Validated Family updatedFamily) {
        Family family = getFamilyByUid(uid);
        if (family != null) {
            family.setName(updatedFamily.getName());
            family.setMembers(updatedFamily.getMembers());
        }
        return family;
  }

  @DeleteMapping("/{uid}")
    public void deleteFamily(@PathVariable String uid) {
        List<Family> families = new ArrayList<>();
        families.add(new Family("1", "Smith", new ArrayList<>()));
        families.add(new Family("2", "Johnson", new ArrayList<>()));
        families.add(new Family("3", "Williams", new ArrayList<>()));
        families.removeIf(family -> family.getUid().equals(uid));
    }
}
