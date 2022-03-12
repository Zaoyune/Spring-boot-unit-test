package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class DemoApplicationTests {
	Calculator underTest = new Calculator();

	@Test//this methode is a test methode for Junit
	void idShouldAddTwoNumbers() {
		// given
		int numberOne = 20;
		int numberTwo = 30;

		// When
		int result = underTest.add(numberOne, numberTwo);

		// then
		int expected = 50;
		assertThat(result).isEqualTo(expected);
		/*import AssertionsForClassTypes.assertThat*/
	}

	class Calculator{
		int add(int a, int b){
			return a+b;
		}
	}

}
