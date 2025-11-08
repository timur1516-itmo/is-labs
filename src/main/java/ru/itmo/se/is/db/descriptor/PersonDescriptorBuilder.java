package ru.itmo.se.is.db.descriptor;

import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.descriptors.VersionLockingPolicy;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.mappings.AggregateObjectMapping;
import org.eclipse.persistence.sequencing.NativeSequence;
import ru.itmo.se.is.entity.Location;
import ru.itmo.se.is.entity.Person;
import ru.itmo.se.is.entity.value.Color;
import ru.itmo.se.is.entity.value.Country;

public class PersonDescriptorBuilder extends BaseDescriptorBuilder {

    @Override
    public RelationalDescriptor buildDescriptor() {
        RelationalDescriptor personDescriptor = new RelationalDescriptor();
        personDescriptor.setJavaClass(Person.class);
        personDescriptor.setTableName("person");
        personDescriptor.addPrimaryKeyFieldName("id");

        personDescriptor.setSequenceNumberFieldName("id");
        personDescriptor.setSequenceNumberName("person_id_seq");
        NativeSequence sequence = new NativeSequence("person_id_seq");
        sequence.setPreallocationSize(1);
        personDescriptor.setSequence(sequence);

        personDescriptor.addMapping(createIdMapping());

        personDescriptor.addMapping(createDirectMapping("name", "name"));
        personDescriptor.addMapping(createEnumMapping("eyeColor", "eye_color", Color.class));
        personDescriptor.addMapping(createEnumMapping("hairColor", "hair_color", Color.class));
        personDescriptor.addMapping(createDirectMapping("weight", "weight"));
        personDescriptor.addMapping(createEnumMapping("nationality", "nationality", Country.class));

        AggregateObjectMapping locationMapping = new AggregateObjectMapping();
        locationMapping.setAttributeName("location");
        locationMapping.setReferenceClass(Location.class);
        locationMapping.addFieldNameTranslation("location_x", "x");
        locationMapping.addFieldNameTranslation("location_y", "y");
        locationMapping.addFieldNameTranslation("location_z", "z");
        personDescriptor.addMapping(locationMapping);

        personDescriptor.addMapping(createDirectMapping("version", "version"));

        DatabaseField versionField = new DatabaseField("version");
        VersionLockingPolicy lockingPolicy = new VersionLockingPolicy(versionField);
        lockingPolicy.storeInObject();
        lockingPolicy.setIsCascaded(false);
        personDescriptor.setOptimisticLockingPolicy(lockingPolicy);

        return personDescriptor;
    }
}
