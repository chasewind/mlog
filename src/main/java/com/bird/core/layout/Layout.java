package com.bird.core.layout;

import com.bird.core.ContextAware;
import com.bird.core.LifeCycle;

public interface Layout<E> extends ContextAware, LifeCycle {

    String doLayout(E event);

    String getFileHeader();

    String getPresentationHeader();

    String getPresentationFooter();

    String getFileFooter();

    String getContentType();

}
