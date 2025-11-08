package ru.itmo.se.is.db.descriptor;

import org.eclipse.persistence.descriptors.RelationalDescriptor;
import ru.itmo.se.is.entity.Coordinates;

public class CoordinatesDescriptorBuilder extends BaseDescriptorBuilder {

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
