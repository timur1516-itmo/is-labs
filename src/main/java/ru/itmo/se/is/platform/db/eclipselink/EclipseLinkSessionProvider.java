package ru.itmo.se.is.platform.db.eclipselink;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.sessions.*;

import javax.sql.DataSource;
import java.util.List;

@ApplicationScoped
@NoArgsConstructor
public class EclipseLinkSessionProvider {
    @Resource(lookup = "java:/jdbc/MyDS")
    DataSource dataSource;

    @Inject
    List<RelationalDescriptor> relationalDescriptors;

    @Produces
    @ApplicationScoped
    public Session createSession() {
        Project project = new Project();
        DatabaseLogin login = new DatabaseLogin();
        login.setConnector(new JNDIConnector(dataSource));
        login.setPlatform(new PostgreSQLPlatform());

        project.setDatasourceLogin(login);

        relationalDescriptors.forEach(project::addDescriptor);

        DatabaseSession session = project.createDatabaseSession();
        session.getSessionLog().setLevel(SessionLog.FINE);
        session.login();

        session.executeNonSelectingSQL("SET search_path TO public");

        return session;
    }
}
