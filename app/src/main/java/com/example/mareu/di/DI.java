package com.example.mareu.di;

import com.example.mareu.service.ApiService;
import com.example.mareu.service.DummyApiService;

/**
 * Dependency injector to get instance of services
 */
public class DI {

    private static final ApiService service = new DummyApiService();

    /**
     * Get an instance on @{@link ApiService}
     *
     * @return service
     */
    public static ApiService getApiService() {
        return service;
    }

    /**
     * Get always a new instance on @{@link ApiService}. Useful for tests, so we ensure the context is clean.
     *
     * @return DummyMaReuApiService
     */
    public static ApiService getNewInstanceApiService() {
        return new DummyApiService();
    }
}
