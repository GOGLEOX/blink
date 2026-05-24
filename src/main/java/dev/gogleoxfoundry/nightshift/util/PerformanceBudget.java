package dev.gogleoxfoundry.nightshift.util;

public final class PerformanceBudget {
    private final int maxOperations;
    private int operationsUsed;

    public PerformanceBudget(int maxOperations) {
        this.maxOperations = Math.max(0, maxOperations);
    }

    public boolean tryConsume() {
        return tryConsume(1);
    }

    public boolean tryConsume(int operations) {
        if (operations <= 0) {
            return true;
        }
        if (operations > remainingOperations()) {
            return false;
        }

        operationsUsed += operations;
        return true;
    }

    public int maxOperations() {
        return maxOperations;
    }

    public int operationsUsed() {
        return operationsUsed;
    }

    public int remainingOperations() {
        return Math.max(0, maxOperations - operationsUsed);
    }

    public boolean exhausted() {
        return operationsUsed >= maxOperations;
    }
}
