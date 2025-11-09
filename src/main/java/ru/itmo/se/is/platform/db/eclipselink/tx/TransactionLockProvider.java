package ru.itmo.se.is.platform.db.eclipselink.tx;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.concurrent.locks.ReentrantLock;

@Getter
@ApplicationScoped
public class TransactionLockProvider {
    private final ReentrantLock lock = new ReentrantLock();
}
