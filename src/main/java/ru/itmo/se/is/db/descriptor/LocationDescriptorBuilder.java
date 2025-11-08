package ru.itmo.se.is.db.descriptor;

import org.eclipse.persistence.descriptors.RelationalDescriptor;
import ru.itmo.se.is.entity.Location;

public class LocationDescriptorBuilder extends BaseDescriptorBuilder {

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
