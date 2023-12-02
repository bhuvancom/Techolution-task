package com.techno.demo.controllers;

import com.techno.demo.model.request.NamesDTO;
import com.techno.demo.services.NamesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {
    private final NamesService data;

    public PrivateController(NamesService data) {
        this.data = data;
    }

    @PostMapping
    public ResponseEntity<String> addName(@RequestBody NamesDTO inputNames) {
        inputNames.getName().forEach(data::add);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
