package com.example.mareu.service;

import com.example.mareu.model.Reunion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class DummyMaReuApiGenerator {

    public static final List<Reunion> DUMMY_REUNIONS = Arrays.asList(
            new Reunion(1,"Test 1", "10h00", "Room A", "fabrice.dutailly@gmail.com, antoine.herbert@gmail.com"),
            new Reunion(2,"Test 2", "13h00", "Room B", "loic.deloison@gmail.com")
            );


    static List<Reunion> generateReunions() {
        return new ArrayList<>(DUMMY_REUNIONS);
    }
}
