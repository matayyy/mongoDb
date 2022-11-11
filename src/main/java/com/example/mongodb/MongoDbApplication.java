package com.example.mongodb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class MongoDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoDbApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate) {
        return args -> {

            Address address = new Address("Poland", "3city", "NE71");
            String email = "studentEmail";

            Student student = new Student(
                    "studnetName",
                    "studentLastname",
                    email,
                    Gender.MALE, address,
                    List.of("Computer Science"),
                    BigDecimal.TEN,
                    LocalDateTime.now());

            //usingMongoTemplateAndQuery(repository, mongoTemplate, email, student);

            repository.findStudentByEmail(email)
                    .ifPresentOrElse(s -> {
                        System.out.println("Student already exist: " + s);
                    }, () -> {
                        System.out.println("Inserting student: " + student);
                        repository.insert(student);
                    });
        };
    }

    private static void usingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
        //to avoid errors checking if email is not taken. Write custom query
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Student> students = mongoTemplate.find(query, Student.class);
        if (students.size() > 1) {
            throw new IllegalStateException("Found many students with email: " + email);
        }

        if (students.isEmpty()) {
            System.out.println("Inserting student: " + student);
            repository.insert(student);
        }
        else {
            System.out.println("Student already exist: " + student);
        }
    }

}
