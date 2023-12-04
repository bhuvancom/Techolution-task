package com.techno.demo.controllers;


import com.techno.demo.services.NamesService;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
@Log
public class PublicController {
    private final NamesService data;

    public PublicController(NamesService data) {
        this.data = data;
    }

    @GetMapping()
    public List<String> getNames() {
        return data.getNames();
    }
}
