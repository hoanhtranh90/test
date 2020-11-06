package com.example.test.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
    @GetMapping("/")
    public ResponseEntity<?> hello(){

        return ResponseEntity.ok("hello world");
    }
}
