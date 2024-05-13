package com.example.familyexampleapp.repository;

import com.example.familyexampleapp.model.MembersDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MembersDB, Long> {
}
