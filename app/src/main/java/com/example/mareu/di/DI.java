package com.example.mareu.di;

import com.example.mareu.service.DummyMaReuApiService;
import com.example.mareu.service.MaReuApiService;

/**
 * Dependency injector to get instance of services
 */
public class DI {

    private static final MaReuApiService service = new DummyMaReuApiService();

    /**
     * Get an instance on @{@link MaReuApiService}
     *
     * @return
     */
    public static MaReuApiService getMaReuApiService() {
        return service;
    }

    /**
     * Get always a new instance on @{@link MaReuApiService}. Useful for tests, so we ensure the context is clean.
     *
     * @return
     */
    public static MaReuApiService getNewInstanceApiService() {
        return new DummyMaReuApiService();
    }
}
