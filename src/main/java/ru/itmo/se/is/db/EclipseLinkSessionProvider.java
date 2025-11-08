package ru.itmo.se.is.db;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.sessions.*;
import ru.itmo.se.is.db.descriptor.*;

import javax.sql.DataSource;

@ApplicationScoped
@NoArgsConstructor
public class EclipseLinkSessionProvider {
    @Resource(lookup = "java:/jdbc/MyDS")
    DataSource dataSource;

    @Produces
    @ApplicationScoped
    public Session createSession() {
        Project project = new Project();
        DatabaseLogin login = new DatabaseLogin();
        login.setConnector(new JNDIConnector(dataSource));
        login.setPlatform(new PostgreSQLPlatform());

        project.setDatasourceLogin(login);

        addDescriptors(project);

        DatabaseSession session = project.createDatabaseSession();
        session.getSessionLog().setLevel(SessionLog.FINE);
        session.login();

        session.executeNonSelectingSQL("SET search_path TO public");

        return session;
    }

    private void addDescriptors(Project project) {
        project.addDescriptor(new MovieDescriptorBuilder().buildDescriptor());
        project.addDescriptor(new PersonDescriptorBuilder().buildDescriptor());
        project.addDescriptor(new CoordinatesDescriptorBuilder().buildDescriptor());
        project.addDescriptor(new LocationDescriptorBuilder().buildDescriptor());
        project.addDescriptor(new ImportOperationDescriptorBuilder().buildDescriptor());
    }
}
