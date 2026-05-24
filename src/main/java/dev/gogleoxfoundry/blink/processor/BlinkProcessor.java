package dev.gogleoxfoundry.blink.processor;

import dev.gogleoxfoundry.blink.util.AdvancementContext;

public interface BlinkProcessor {
    String id();

    ProcessorResult process(AdvancementContext context);
}
