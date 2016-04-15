package com.bird.core.jaran.action;

import com.bird.core.AggregationType;
import com.bird.core.jaran.PropertySetter;

public class IADataForComplexProperty {

    final PropertySetter  parentBean;
    final AggregationType aggregationType;
    final String          complexPropertyName;
    private Object        nestedComplexProperty;
    boolean               inError;

    public IADataForComplexProperty(PropertySetter parentBean, AggregationType aggregationType,
                                    String complexPropertyName){
        this.parentBean = parentBean;
        this.aggregationType = aggregationType;
        this.complexPropertyName = complexPropertyName;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public Object getNestedComplexProperty() {
        return nestedComplexProperty;
    }

    public String getComplexPropertyName() {
        return complexPropertyName;
    }

    public void setNestedComplexProperty(Object nestedComplexProperty) {
        this.nestedComplexProperty = nestedComplexProperty;
    }

}
