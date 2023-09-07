package com.project.TaskManger.cc;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/ll")
public class cc {
    @GetMapping
    public ResponseEntity<String> s(){
        return ResponseEntity.ok("ee");
    }
}
