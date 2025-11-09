package ru.itmo.se.is.feature.movie.infrastructure.db.descriptor.coordinates;

import org.eclipse.persistence.descriptors.RelationalDescriptor;
import ru.itmo.se.is.feature.movie.domain.coordinates.Coordinates;
import ru.itmo.se.is.platform.db.eclipselink.descriptor.RelationalDescriptorBuilder;
import ru.itmo.se.is.platform.db.eclipselink.descriptor.annotation.DescriptorBuilder;

@DescriptorBuilder
public class CoordinatesDescriptorBuilder extends RelationalDescriptorBuilder {

    @Override
    public RelationalDescriptor buildDescriptor() {
        RelationalDescriptor coordinatesDescriptor = new RelationalDescriptor();
        coordinatesDescriptor.setJavaClass(Coordinates.class);
        coordinatesDescriptor.descriptorIsAggregate();

        coordinatesDescriptor.addMapping(createDirectMapping("x", "x"));
        coordinatesDescriptor.addMapping(createDirectMapping("y", "y"));

        return coordinatesDescriptor;
    }
}
