package ru.itmo.se.is.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.util.concurrent.locks.ReentrantLock;

@ApplicationScoped
public class UniqueLockProvider {
    private final ReentrantLock uniqueLock = new ReentrantLock();

    @Produces
    @ApplicationScoped
    public ReentrantLock getUniqueLock() {
        return uniqueLock;
    }
}
