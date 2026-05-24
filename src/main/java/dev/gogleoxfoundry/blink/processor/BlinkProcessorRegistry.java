package dev.gogleoxfoundry.blink.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BlinkProcessorRegistry {
    private static final List<BlinkProcessor> PROCESSORS = List.of(
        new FurnaceProcessor(),
        new CampfireProcessor(),
        new CropProcessor()
    );

    static {
        Set<String> ids = new HashSet<>();
        for (BlinkProcessor processor : PROCESSORS) {
            if (processor == null) {
                throw new IllegalStateException("Blink processor registry contains null entry");
            }
            String id = processor.id();
            if (id == null || id.isBlank()) {
                throw new IllegalStateException("Blink processor registry contains blank id");
            }
            if (!ids.add(id)) {
                throw new IllegalStateException("Duplicate Blink processor id: " + id);
            }
        }
    }

    private BlinkProcessorRegistry() {
    }

    public static List<BlinkProcessor> all() {
        return PROCESSORS;
    }
}
