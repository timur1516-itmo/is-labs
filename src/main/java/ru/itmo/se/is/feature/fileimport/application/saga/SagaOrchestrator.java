package ru.itmo.se.is.feature.fileimport.application.saga;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.se.is.shared.exception.BusinessException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@ApplicationScoped
public class SagaOrchestrator {
    public void executeSaga(List<SagaStep> steps, SagaContext context) throws Exception {
        List<SagaStep> executedSteps = new ArrayList<>();
        try {
            for (SagaStep step : steps) {
                try {
                    executedSteps.add(step);
                    step.execute(context);
                } catch (BusinessException e) {
                    context.setError(e);
                }
            }
            if (context.getError() != null)
                throw context.getError();
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            Collections.reverse(executedSteps);

            executedSteps.forEach(step -> {
                try {
                    step.compensate(context);
                } catch (Exception ex2) {
                    log.warn("Saga compensation error: {}", ex2.getMessage());
                }
            });
            throw ex;
        }
    }
}
