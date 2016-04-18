package com.bird.core.jaran;

import com.bird.core.DefaultNestedComponentRegistry;
import com.bird.core.ElementSelector;
import com.bird.core.ILoggingEvent;
import com.bird.core.jaran.action.AppenderRefAction;
import com.bird.core.jaran.action.ConfigurationAction;
import com.bird.core.jaran.action.ContextNameAction;
import com.bird.core.jaran.action.LevelAction;
import com.bird.core.jaran.action.LoggerAction;
import com.bird.core.jaran.action.LoggerContextListenerAction;
import com.bird.core.jaran.action.RootLoggerAction;

public class JoranConfigurator extends JoranConfiguratorBase {

    @Override
    public void addInstanceRules(RuleStore rs) {
        // parent rules already added
        super.addInstanceRules(rs);

        rs.addRule(new ElementSelector("configuration"), new ConfigurationAction());

        rs.addRule(new ElementSelector("configuration/contextName"), new ContextNameAction());
        rs.addRule(new ElementSelector("configuration/contextListener"), new LoggerContextListenerAction());

        rs.addRule(new ElementSelector("configuration/logger"), new LoggerAction());
        rs.addRule(new ElementSelector("configuration/logger/level"), new LevelAction());

        rs.addRule(new ElementSelector("configuration/root"), new RootLoggerAction());
        rs.addRule(new ElementSelector("configuration/root/level"), new LevelAction());
        rs.addRule(new ElementSelector("configuration/logger/appender-ref"), new AppenderRefAction<ILoggingEvent>());
        rs.addRule(new ElementSelector("configuration/root/appender-ref"), new AppenderRefAction<ILoggingEvent>());

    }

    @Override
    protected void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {
        DefaultNestedComponentRules.addDefaultNestedComponentRegistryRules(registry);
    }

}
