package com.phonebook.main;

import com.phonebook.spring.ApplicationConfig;
import com.phonebook.spring.PhoneBook;
import com.phonebook.spring.PhoneBookFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * PhoneBook entry point
 */
public class PhoneBookMain {

    public static void main(String[] args) {
        ApplicationContext context = newApplicationContext(args);

        Scanner sc = new Scanner(System.in);
        sc.useDelimiter(System.getProperty("line.separator"));

        PhoneBook phoneBook = context.getBean("phoneBook", PhoneBook.class);
        PhoneBookFormatter renderer = (PhoneBookFormatter) context.getBean("phoneBookFormatter");

        renderer.info("type 'ADD' 'name' 'phone sequence' to add a phone(s) to a new user or an existing one");
        renderer.info("type 'REMOVE_PHONE' 'phone' to remove a phone number");
        renderer.info("type 'SHOW' to show contents of a phonebook");
        renderer.info("type 'exit' to quit.");
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.equals("exit")) {
                renderer.info("Have a good day...");
                break;
            }
            try {
                if (line.matches("ADD.*")) {
                    String name = line.split(" ")[1];
                    String[] phoneSequence = line.split(" ")[2].split(",");
                    for (String phone : phoneSequence) {
                        phoneBook.addPhone(name, phone);
                    }
                }
                else if (line.matches("REMOVE_PHONE.*")) {
                    phoneBook.removePhone(line.split(" ")[1]);
                }
                else if (line.equals("SHOW")) {
                    renderer.show(phoneBook.findAll());
                }
                else {
                    throw new UnsupportedOperationException("Unsupported operation! Please use ADD, REMOVE_PHONE or SHOW.");
                }
            } catch (Exception e) {
                renderer.error(e);
            }
        }
    }

    static ApplicationContext newApplicationContext(String... args) {
        return args.length > 0 && args[0].equals("classPath")
                ? new ClassPathXmlApplicationContext("application-config.xml")
                : new AnnotationConfigApplicationContext(ApplicationConfig.class);
    }

}
