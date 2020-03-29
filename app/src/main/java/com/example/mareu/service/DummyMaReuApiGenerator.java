package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class DummyMaReuApiGenerator {

    public static final List<Reunion> DUMMY_REUNIONS = Arrays.asList((
            new Reunion(1, "Test 1", "10h00", "Room A", Arrays.asList(
                    new Guest(1, "Fabrice", "Dutailly", "fabrice.dutailly@gmail.com", "url"),
                    new Guest(2, "Antoine", "Herbert", "antoine.herbert@gmail.com", "url2")))),
            new Reunion(2, "Test 2", "13h00", "Room B", Arrays.asList((
                    new Guest(2, "Antoine", "Herbert", "antoine.herbert@gmail.com", "url2")))),
            new Reunion(2, "Test 2", "13h00", "Room B", Arrays.asList((
                    new Guest(1, "Fabrice", "Dutailly", "fabrice.dutailly@gmail.com", "url")),
                    new Guest(3, "Laurent", "Borel", "laurent.borel@gmail.com", "url2"))));

    public static final List<Guest> DUMMY_GUESTS = Arrays.asList((
            new Guest(1, "Fabrice", "Dutailly", "fabrice.dutailly@gmail.com", "url")),
            new Guest(2, "Antoine", "Herbert", "antoine.herbert@gmail.com", "url2"),
            new Guest(3, "Laurent", "Borel", "laurent.borel@gmail.com", "url2"));


    static List<Reunion> generateReunions() {
        return new ArrayList<>(DUMMY_REUNIONS);
    }

    static List<Guest> generateGuestList() {
        return new ArrayList<>(DUMMY_GUESTS);
    }
}
