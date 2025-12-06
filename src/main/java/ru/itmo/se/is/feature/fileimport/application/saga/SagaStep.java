package ru.itmo.se.is.feature.fileimport.application.saga;

public interface SagaStep {
    void execute(SagaContext context) throws Exception;

    void compensate(SagaContext context) throws Exception;
}
