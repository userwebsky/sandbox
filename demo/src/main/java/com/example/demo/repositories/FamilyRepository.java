package com.example.demo.repositories;

import com.example.demo.models.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<Family,Long> {
  @Override
  Optional<Family> findById(Long id);

  /*
  @Query(value = "SELECT * FROM family where name=?1 and origin=?2",nativeQuery = true)
    List<FamilyDB> findByname(String name, String origin);
  * */
}
