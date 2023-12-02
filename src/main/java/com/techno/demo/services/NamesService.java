package com.techno.demo.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NamesService {
    private List<String> names = new ArrayList<>();

    public List<String> getNames(){
        return names;
    }

    public void add(String name) {
        names.add(name);
    }
}
