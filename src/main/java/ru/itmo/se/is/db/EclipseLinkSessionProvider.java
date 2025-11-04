package ru.itmo.se.is.db;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.internal.sessions.DatabaseSessionImpl;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.transaction.wildfly.WildFlyTransactionController;

@ApplicationScoped
@NoArgsConstructor
public class EclipseLinkSessionProvider {
    @Inject
    private Project project;

    private DatabaseSessionImpl session;

    @Produces
    @ApplicationScoped
    public DatabaseSessionImpl createDatabaseSession() {
        return session;
    }

    @PostConstruct
    public void postConstruct() {
        session = (DatabaseSessionImpl) project.createDatabaseSession();
        session.setExternalTransactionController(new WildFlyTransactionController());
        session.login();
    }

    @PreDestroy
    public void cleanup() {
        if (session != null && session.isLoggedIn()) {
            session.logout();
        }
    }
}
