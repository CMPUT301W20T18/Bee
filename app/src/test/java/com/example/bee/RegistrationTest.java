package com.example.bee;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegistrationTest {
    RegistrationActivity reg = new RegistrationActivity();

    @Test
    public void isNumeric_isCorrect(){
        assertFalse(reg.isNumeric("a"));
        assertTrue(reg.isNumeric("1"));
    }
    @Test
    public void validateEmail_isCorrect(){
        assertTrue(reg.validateEmail("a@gmail.com"));
        assertFalse(reg.validateEmail("agmail.com"));
        assertFalse(reg.validateEmail("a@gmail"));
    }
    @Test
    public void validateUsername_isCorrect(){
        assertTrue(reg.validateUsername("Sophie"));
        assertFalse(reg.validateUsername("gugouegougbfeoufgegroerfv"));
    }
    @Test
    public void validatePhone_isCorrect(){
        assertTrue(reg.validatePhone("5879375617"));
        assertFalse(reg.validatePhone("u123456789"));
    }
    @Test
    public void validatePassword_isCorrect(){
        assertTrue(reg.validatePassword("1234Q!"));
        assertFalse(reg.validatePassword("Q!1"));
        assertFalse(reg.validatePassword("65010584604"));

    }
    @Test
    public void registerUser_isCorrect(){
//        assertTrue(reg.registerUser("Sophie","4567W!","5879375617","sophie@gmail.com","Sophie","White"));
//        assertTh(reg.registerUser("Sophie","4567W!","5879375617","sophie@gmail.com","Sophie","White"));
    }



}
