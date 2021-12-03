package com.phonebook.tests;

import com.phonebook.spring.ApplicationConfig;
import com.phonebook.spring.PhoneBook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
public class PhoneBookTest {

    @Autowired
    private PhoneBook phoneBook;

    @Test
    public void get_person_phone_numbers() {
        final Set<String> expected = new HashSet<>(asList("+79601232233"));
        assertEquals("phone numbers do not match",
                expected,
                phoneBook.findAll().get("Alex"));
    }

    @Test
    public void add_new_contact() {
        final Set<String> expected = new HashSet<>(asList("+79214852312"));
        final String name = "TestNewContact";
        phoneBook.addPhone(name, expected.stream().collect(Collectors.toList()).get(0));
        assertEquals("Contact wasn't created",
                expected,
                phoneBook.findAll().get(name));
    }

    @Test
    public void remove_contact() {
        final String expectedPhone = "+79216485825";
        final String name = "TestRemoveContact";
        phoneBook.addPhone(name, expectedPhone);
        phoneBook.removePhone(expectedPhone);
        assertEquals("Contact wasn't Removed", phoneBook.findAll().get(name),null);
    }

    @Test
    public void add_phone_to_existing_person() {
        final String phoneOne = "+79214569832";
        final String phoneTwo = "+79215558696";
        final Set<String> expected = new HashSet<>(asList(phoneOne, phoneTwo));
        final String name = "TestAddPhone";
        phoneBook.addPhone(name, phoneOne);
        phoneBook.addPhone(name, phoneTwo);

        assertEquals("Contact wasn't updated",
                expected,
                phoneBook.findAll().get(name));
    }

    @Test
    public void remove_phone_from_existing_person() {
        final String phone = "+79213215566";
        final String name = "Billy";
        phoneBook.removePhone(phone);

        assertFalse(phoneBook.findAll().get(name).contains(phone));
    }

}
