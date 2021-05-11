package com.github.hpchugo.aws_project01.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/dog/{name}")
    public ResponseEntity<?> dogTest(@PathVariable String name){
        LOG.info("Test controller - name {}", name);
        return ResponseEntity.ok(String.format("Name: %s", name));
    }

    @GetMapping("/dog/color/{color}")
    public ResponseEntity<?> dogColor(@PathVariable String color){
        LOG.info("Test controller - color {}", color);
        return ResponseEntity.ok(String.format("Name: %s", color));
    }
}
