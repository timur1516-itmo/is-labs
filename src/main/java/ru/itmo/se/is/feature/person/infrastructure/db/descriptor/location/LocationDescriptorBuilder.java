package ru.itmo.se.is.feature.person.infrastructure.db.descriptor.location;

import org.eclipse.persistence.descriptors.RelationalDescriptor;
import ru.itmo.se.is.feature.person.domain.location.Location;
import ru.itmo.se.is.platform.db.eclipselink.descriptor.RelationalDescriptorBuilder;
import ru.itmo.se.is.platform.db.eclipselink.descriptor.annotation.DescriptorBuilder;

@DescriptorBuilder
public class LocationDescriptorBuilder extends RelationalDescriptorBuilder {

    @Override
    public RelationalDescriptor buildDescriptor() {
        RelationalDescriptor locationDescriptor = new RelationalDescriptor();
        locationDescriptor.setJavaClass(Location.class);
        locationDescriptor.descriptorIsAggregate();

        locationDescriptor.addMapping(createDirectMapping("x", "x"));
        locationDescriptor.addMapping(createDirectMapping("y", "y"));
        locationDescriptor.addMapping(createDirectMapping("z", "z"));

        return locationDescriptor;
    }
}
