package ru.itmo.se.is.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.sequencing.NativeSequence;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.JNDIConnector;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@ApplicationScoped
@NoArgsConstructor
public class DatabaseLoginConfig {

    @Produces
    @ApplicationScoped
    public DatabaseLogin createDatabaseLogin() {
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/jdbc/MyDS");

            JNDIConnector connector = new JNDIConnector();
            connector.setDataSource(ds);

            DatabaseLogin login = new DatabaseLogin();
            login.setPlatform(new PostgreSQLPlatform());
            login.setDefaultSequence(new NativeSequence());
            login.shouldUseNativeSequencing();
            login.setUsesExternalTransactionController(true);
            login.setConnector(connector);

            return login;
        } catch (NamingException e) {
            throw new RuntimeException("Failed to lookup DataSource", e);
        }
    }
}
