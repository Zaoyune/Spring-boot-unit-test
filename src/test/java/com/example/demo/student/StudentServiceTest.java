package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

/*While doing unit testing using junit you will come across places where you want
to mock classes. Mocking is done when you invoke methods of a class that has external
communication like database calls or rest calls.*/
    @Mock private StudentRepository studentRepository;
    private StudentService underTestStudent;

    /*Since we already tested our StudentRepository and we already know
    * it works fine we will just Mock its implementation inside
    * our StudentService test*/

    @BeforeEach//this will run before each test
    void setUp() {
        underTestStudent = new StudentService(studentRepository);
    }

    @Test
    void CanGetAllStudents() {
        // when
        underTestStudent.getAllStudents();
        // then
        /*CTRL + ENTER to import Mockito.Verify */
        verify(studentRepository).findAll();
        /*if we use this one it should fail*/
        //verify(studentRepository).deleteAll();
    }

    @Test
    void addStudent() {
        // given
        Student student = new Student(
                "Hassan",
                "hassan.benharouga@gmal.com",
                Gender.MALE
        );

        // when
        underTestStudent.addStudent(student);

        // then
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentRepository)
                .save(studentArgumentCaptor.capture());
        /*We want to capture the actual Student object that was paste inside
        * the save method => studentRepository.save(student)*/

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
        /*Now we are checking if the captured student is equal to the actual student we saved*/
    }

    @Test
    void WillThrowWhenEmailIsTaken() {
        // given
        Student student = new Student(
                "Hassan",
                "hassan.benharouga@gmal.com",
                Gender.MALE
        );
        /*By default, our boolean method are set to false, so we need to make it
        * return true*/
        //given(studentRepository.selectExistsEmail(student.getEmail()))
        given(studentRepository.selectExistsEmail(anyString()))
                .willReturn(Boolean.TRUE);
        // when
        // then
        assertThatThrownBy(() ->underTestStudent.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");
            /*We should respect the same Exception message otherwise it won't work*/

        verify(studentRepository, never()).save(any());
        /*here we are saying that our repository or our mock never saves any student*/
        }

    @Test
    //@Disabled//disable these tests so they won't run
    void CanDeleteStudent() {
        // given
        long testIdStudent=10;
        given(studentRepository.existsById(testIdStudent))
                .willReturn(true);
        // when
        underTestStudent.deleteStudent(testIdStudent);
        // then
        verify(studentRepository).deleteById(testIdStudent);
    }

    @Test
    void willThrowWhenDeleteStudentNotFound() {
        // given
        long id = 10;
        given(studentRepository.existsById(id))
                .willReturn(false);
        // when
        // then
        assertThatThrownBy(() -> underTestStudent.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + id + " does not exists");

        verify(studentRepository, never()).deleteById(any());
    }
}
