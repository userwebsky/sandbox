package com.example.sec;
/*
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.mongodb.repository.MongoRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private MongoRepository<User, String> userRepository;

  @Test
  public void whenSaveUser_thenUserIsInDb() {
    // given
    User testUser = new User();
    testUser.setUsername("testUsername");
    testUser.setPassword("testPassword");

    String mongoid = entityManager.persistAndGetId(testUser).toString();
    entityManager.flush();

    // when
    User foundUser = userRepository.findById(mongoid).orElse(null);

    // then
    assertThat(foundUser).isNotNull();
    assertThat(foundUser.getUsername()).isEqualTo(testUser.getUsername());
    assertThat(foundUser.getPassword()).isEqualTo(testUser.getPassword());
  }
}*/
