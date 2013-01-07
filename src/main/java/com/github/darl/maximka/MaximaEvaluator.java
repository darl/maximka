package com.github.darl.maximka;

/**
 * Vladislav Dolbilov (dvladislv@gmail.com)
 */
public interface MaximaEvaluator {

    /**
     *
     * @param expr expression to evaluate
     * @return calculated expression in maxima format
     * @throws MaximaException on any exception
     */
    String eval(String expr) throws MaximaException;

}
