package com.bird.core.jaran;

import com.bird.core.AppenderBase;
import com.bird.core.DefaultNestedComponentRegistry;
import com.bird.core.ElementSelector;
import com.bird.core.jaran.action.AppenderRefAction;
import com.bird.core.jaran.action.ConfigurationAction;
import com.bird.core.layout.PatternLayout;
import com.bird.core.layout.PatternLayoutEncoder;

public class JoranConfigurator extends JoranConfiguratorBase {

    @Override
    public void addInstanceRules(RuleStore rs) {
        super.addInstanceRules(rs);
        rs.addRule(new ElementSelector("configuration"), new ConfigurationAction());
        rs.addRule(new ElementSelector("configuration/appender-ref"), new AppenderRefAction());

    }

    @Override
    protected void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {
        registry.add(AppenderBase.class, "layout", PatternLayout.class);

        registry.add(AppenderBase.class, "encoder", PatternLayoutEncoder.class);
        registry.add(AppenderBase.class, "encoder", PatternLayoutEncoder.class);
    }

}
