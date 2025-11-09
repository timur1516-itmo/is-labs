package ru.itmo.se.is.platform.db.eclipselink.descriptor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import org.eclipse.persistence.descriptors.RelationalDescriptor;

import java.util.List;

@ApplicationScoped
public class DescriptorsProducer {
    @Produces
    public List<RelationalDescriptor> descriptors(Instance<RelationalDescriptorBuilder> builders) {
        return builders.stream()
                .map(RelationalDescriptorBuilder::buildDescriptor)
                .toList();
    }
}
