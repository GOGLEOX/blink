package dev.gogleoxfoundry.nightshift.processor;

public record ProcessorResult(String processorId, int operationsUsed) {
    public ProcessorResult {
        if (processorId == null || processorId.isBlank()) {
            throw new IllegalArgumentException("processorId must not be blank");
        }
        if (operationsUsed < 0) {
            throw new IllegalArgumentException("operationsUsed must not be negative");
        }
    }

    public static ProcessorResult none(String processorId) {
        return new ProcessorResult(processorId, 0);
    }
}
