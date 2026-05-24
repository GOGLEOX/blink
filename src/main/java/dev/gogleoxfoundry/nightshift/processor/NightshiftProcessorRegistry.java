package dev.gogleoxfoundry.nightshift.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class NightshiftProcessorRegistry {
    private static final List<NightshiftProcessor> PROCESSORS = List.of(
        new FurnaceProcessor(),
        new CampfireProcessor(),
        new CropProcessor()
    );

    static {
        Set<String> ids = new HashSet<>();
        for (NightshiftProcessor processor : PROCESSORS) {
            if (processor == null) {
                throw new IllegalStateException("Nightshift processor registry contains null entry");
            }
            String id = processor.id();
            if (id == null || id.isBlank()) {
                throw new IllegalStateException("Nightshift processor registry contains blank id");
            }
            if (!ids.add(id)) {
                throw new IllegalStateException("Duplicate Nightshift processor id: " + id);
            }
        }
    }

    private NightshiftProcessorRegistry() {
    }

    public static List<NightshiftProcessor> all() {
        return PROCESSORS;
    }
}
