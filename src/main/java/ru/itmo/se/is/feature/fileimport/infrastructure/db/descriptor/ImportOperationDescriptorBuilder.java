package ru.itmo.se.is.feature.fileimport.infrastructure.db.descriptor;

import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.sequencing.NativeSequence;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportStatus;
import ru.itmo.se.is.platform.db.eclipselink.converter.ZonedDateTimeConverter;
import ru.itmo.se.is.platform.db.eclipselink.descriptor.RelationalDescriptorBuilder;
import ru.itmo.se.is.platform.db.eclipselink.descriptor.annotation.DescriptorBuilder;

@DescriptorBuilder
public class ImportOperationDescriptorBuilder extends RelationalDescriptorBuilder {
    @Override
    public RelationalDescriptor buildDescriptor() {
        RelationalDescriptor importOperationDescriptor = new RelationalDescriptor();
        importOperationDescriptor.setJavaClass(ImportOperation.class);
        importOperationDescriptor.setTableName("import_operation");
        importOperationDescriptor.addPrimaryKeyFieldName("id");

        importOperationDescriptor.setSequenceNumberFieldName("id");
        importOperationDescriptor.setSequenceNumberName("import_operation_id_seq");
        NativeSequence sequence = new NativeSequence("import_operation_id_seq");
        sequence.setPreallocationSize(1);
        importOperationDescriptor.setSequence(sequence);

        importOperationDescriptor.addMapping(createIdMapping());

        importOperationDescriptor.addMapping(createEnumMapping("status", "status", ImportStatus.class));

        DirectToFieldMapping startDtMapping = createDirectMapping("startDt", "start_dt");
        startDtMapping.setConverter(new ZonedDateTimeConverter());
        importOperationDescriptor.addMapping(startDtMapping);

        DirectToFieldMapping endDtMapping = createDirectMapping("endDt", "end_dt");
        endDtMapping.setConverter(new ZonedDateTimeConverter());
        importOperationDescriptor.addMapping(endDtMapping);

        importOperationDescriptor.addMapping(createDirectMapping("importedCnt", "imported_cnt"));
        importOperationDescriptor.addMapping(createDirectMapping("errorMessage", "error_message"));

        return importOperationDescriptor;
    }
}
