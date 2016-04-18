package com.bird.core.jaran;

import com.bird.core.AppenderBase;
import com.bird.core.DefaultNestedComponentRegistry;
import com.bird.core.layout.PatternLayout;
import com.bird.core.layout.PatternLayoutEncoder;

public class DefaultNestedComponentRules {

    static public void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {
        registry.add(AppenderBase.class, "layout", PatternLayout.class);

        registry.add(AppenderBase.class, "encoder", PatternLayoutEncoder.class);

    }

}
