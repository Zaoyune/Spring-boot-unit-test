package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTestStudent;

    @AfterEach//ALT+INSERT
    void tearDown() {
        underTestStudent.deleteAll();
        // in each test we will delete all after the test if finish
    }

    @Test
    void ItShouldCheckWhenStudentEmailExists() {
        // given
        String email = "hassan.benharouga@gmal.com";
        Student student = new Student(
                "Hassan",
                email,
                Gender.MALE
        );
        underTestStudent.save(student);

        // when
        boolean ExpectedExistingEmail = underTestStudent.selectExistsEmail(email);

        // then
        assertThat(ExpectedExistingEmail).isTrue();

    }

    @Test
    void ItShouldCheckWhenStudentEmailDoesNotExists() {
        // given
        String email = "hassan.benharouga@gmal.com";

        // when
        boolean ExpectedExistingEmail = underTestStudent.selectExistsEmail(email);

        // then
        assertThat(ExpectedExistingEmail).isFalse();

    }
}
