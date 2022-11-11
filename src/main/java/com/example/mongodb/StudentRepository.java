package com.example.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {

    Optional<Student> findStudentByEmail(String email);

//    //using real mongoDbQuery
//    @Query("typeHere")
//    void test();
}
