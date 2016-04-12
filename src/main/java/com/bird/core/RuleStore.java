package com.bird.core;

import java.util.List;

public interface RuleStore {

    void addRule(ElementSelector elementSelector, String actionClassStr) throws ClassNotFoundException;

    void addRule(ElementSelector elementSelector, Action action);

    List<Action> matchActions(ElementPath elementPath);
}
