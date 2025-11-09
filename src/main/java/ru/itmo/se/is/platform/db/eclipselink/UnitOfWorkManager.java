package ru.itmo.se.is.platform.db.eclipselink;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;

import java.util.ArrayDeque;
import java.util.Deque;

@ApplicationScoped
public class UnitOfWorkManager {
    private final ThreadLocal<Deque<UnitOfWork>> uowDeque =
            ThreadLocal.withInitial(ArrayDeque::new);
    @Inject
    Session session;

    public UnitOfWork getCurrent() {
        Deque<UnitOfWork> stack = uowDeque.get();
        return stack.isEmpty() ? null : stack.peek();
    }

    public UnitOfWork beginRequired() {
        Deque<UnitOfWork> stack = uowDeque.get();
        if (stack.isEmpty()) {
            UnitOfWork uow = session.acquireUnitOfWork();
            stack.push(uow);
            return uow;
        }
        return stack.peek();
    }

    public UnitOfWork beginRequiresNew() {
        Deque<UnitOfWork> stack = uowDeque.get();
        UnitOfWork uow = session.acquireUnitOfWork();
        stack.push(uow);
        return uow;
    }

    public void end() {
        Deque<UnitOfWork> stack = uowDeque.get();
        if (!stack.isEmpty()) {
            stack.pop();
        }
    }
}
