package com.bird.core;

public interface EventEvaluator<E> extends ContextAware, LifeCycle {

    /**
     * Evaluates whether the event passed as parameter matches some user-specified criteria.
     * <p>
     * The <code>Evaluator</code> is free to evaluate the event as it pleases. In particular, the evaluation results
     * <em>may</em> depend on previous events.
     * 
     * @param event The event to evaluate
     * @return true if there is a match, false otherwise.
     * @throws NullPointerException can be thrown in presence of null values
     * @throws EvaluationException may be thrown during faulty evaluation
     */
    boolean evaluate(E event) throws NullPointerException, EvaluationException;

    /**
     * Evaluators are named entities.
     * 
     * @return The name of this evaluator.
     */
    String getName();

    /**
     * Evaluators are named entities.
     */
    void setName(String name);
}
