package ru.itmo.se.is.platform.db.eclipselink.converter;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

import java.time.ZonedDateTime;

public class ZonedDateTimeConverter implements Converter {
    @Override
    public Object convertObjectValueToDataValue(Object objectValue, Session session) {
        if (objectValue == null) return null;
        ZonedDateTime zdt = (ZonedDateTime) objectValue;
        return java.sql.Timestamp.from(zdt.toInstant());
    }

    @Override
    public Object convertDataValueToObjectValue(Object dataValue, Session session) {
        if (dataValue == null) return null;
        java.sql.Timestamp ts = (java.sql.Timestamp) dataValue;
        return ts.toInstant().atZone(java.time.ZoneId.systemDefault());
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public void initialize(DatabaseMapping mapping, Session session) {
    }
}
