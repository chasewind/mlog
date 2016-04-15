package com.bird.core.jaran.action;

import com.bird.core.AggregationType;
import com.bird.core.jaran.PropertySetter;

public class IADataForBasicProperty {

    final PropertySetter  parentBean;
    final AggregationType aggregationType;
    final String          propertyName;
    boolean               inError;

    IADataForBasicProperty(PropertySetter parentBean, AggregationType aggregationType, String propertyName){
        this.parentBean = parentBean;
        this.aggregationType = aggregationType;
        this.propertyName = propertyName;
    }
}
