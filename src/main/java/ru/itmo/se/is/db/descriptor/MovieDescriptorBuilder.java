package ru.itmo.se.is.db.descriptor;

import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.descriptors.VersionLockingPolicy;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.mappings.AggregateObjectMapping;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.eclipse.persistence.sequencing.NativeSequence;
import ru.itmo.se.is.db.converter.ZonedDateTimeConverter;
import ru.itmo.se.is.entity.Coordinates;
import ru.itmo.se.is.entity.Movie;
import ru.itmo.se.is.entity.Person;
import ru.itmo.se.is.entity.value.MovieGenre;
import ru.itmo.se.is.entity.value.MpaaRating;

public class MovieDescriptorBuilder extends BaseDescriptorBuilder {

    @Override
    public RelationalDescriptor buildDescriptor() {
        RelationalDescriptor movieDescriptor = new RelationalDescriptor();
        movieDescriptor.setJavaClass(Movie.class);
        movieDescriptor.setTableName("movie");
        movieDescriptor.addPrimaryKeyFieldName("id");

        movieDescriptor.setSequenceNumberFieldName("id");
        movieDescriptor.setSequenceNumberName("movie_id_seq");
        NativeSequence sequence = new NativeSequence("movie_id_seq");
        sequence.setPreallocationSize(1);
        movieDescriptor.setSequence(sequence);

        movieDescriptor.addMapping(createIdMapping());

        movieDescriptor.addMapping(createDirectMapping("name", "name"));

        DirectToFieldMapping creationDateMapping = createDirectMapping("creationDate", "creation_date");
        creationDateMapping.setConverter(new ZonedDateTimeConverter());
        movieDescriptor.addMapping(creationDateMapping);

        movieDescriptor.addMapping(createDirectMapping("oscarsCount", "oscars_count"));
        movieDescriptor.addMapping(createDirectMapping("budget", "budget"));
        movieDescriptor.addMapping(createDirectMapping("totalBoxOffice", "total_box_office"));
        movieDescriptor.addMapping(createDirectMapping("length", "length"));
        movieDescriptor.addMapping(createDirectMapping("goldenPalmCount", "golden_palm_count"));
        movieDescriptor.addMapping(createDirectMapping("usaBoxOffice", "usa_box_office"));
        movieDescriptor.addMapping(createDirectMapping("tagline", "tagline"));

        movieDescriptor.addMapping(createEnumMapping("mpaaRating", "mpaa_rating", MpaaRating.class));
        movieDescriptor.addMapping(createEnumMapping("genre", "genre", MovieGenre.class));

        movieDescriptor.addMapping(createPersonMapping("director", "director_id", false));
        movieDescriptor.addMapping(createPersonMapping("screenwriter", "screenwriter_id", true));
        movieDescriptor.addMapping(createPersonMapping("operator", "operator_id", true));

        AggregateObjectMapping coordinatesMapping = new AggregateObjectMapping();
        coordinatesMapping.setAttributeName("coordinates");
        coordinatesMapping.setReferenceClass(Coordinates.class);
        coordinatesMapping.addFieldNameTranslation("coordinates_x", "x");
        coordinatesMapping.addFieldNameTranslation("coordinates_y", "y");
        movieDescriptor.addMapping(coordinatesMapping);

        movieDescriptor.addMapping(createDirectMapping("version", "version"));

        DatabaseField versionField = new DatabaseField("version");
        VersionLockingPolicy lockingPolicy = new VersionLockingPolicy(versionField);
        lockingPolicy.storeInObject();
        lockingPolicy.setIsCascaded(false);
        movieDescriptor.setOptimisticLockingPolicy(lockingPolicy);

        return movieDescriptor;
    }

    private OneToOneMapping createPersonMapping(String attributeName, String foreignKey, boolean isOptional) {
        OneToOneMapping mapping = new OneToOneMapping();
        mapping.setAttributeName(attributeName);
        mapping.setReferenceClass(Person.class);
        mapping.addForeignKeyFieldName(foreignKey, "id");
        mapping.setIsOptional(isOptional);
        mapping.setCascadeAll(false);
        mapping.dontUseIndirection();
        return mapping;
    }
}
