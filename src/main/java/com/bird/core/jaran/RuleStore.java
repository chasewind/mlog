package com.bird.core.jaran;

import java.util.List;

import com.bird.core.ElementPath;
import com.bird.core.ElementSelector;
import com.bird.core.jaran.action.Action;

public interface RuleStore {

    void addRule(ElementSelector elementSelector, String actionClassStr) throws ClassNotFoundException;

    void addRule(ElementSelector elementSelector, Action action);

    List<Action> matchActions(ElementPath elementPath);
}
