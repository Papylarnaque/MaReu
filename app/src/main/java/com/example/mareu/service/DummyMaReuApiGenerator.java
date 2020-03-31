package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Room;

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

    private static final List<Room> DUMMY_ROOMS = Arrays.asList((
            new Room(1, "Room A",10)),
            new Room(1, "Room B",5),
            new Room(3, "Room C", 3));


    static List<Reunion> generateReunions() {
        return new ArrayList<>(DUMMY_REUNIONS);
    }

    static List<Guest> generateGuestsList() {
        return new ArrayList<>(DUMMY_GUESTS);
    }

    static List<Room> generateRoomsList() {
        return new ArrayList<>(DUMMY_ROOMS);
    }
}
