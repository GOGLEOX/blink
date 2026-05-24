package dev.gogleoxfoundry.nightshift.processor;

import dev.gogleoxfoundry.nightshift.util.AdvancementContext;

public interface NightshiftProcessor {
    String id();

    ProcessorResult process(AdvancementContext context);
}
