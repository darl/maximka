package com.github.darl.maximka.abcl;

import com.github.darl.maximka.MaximaEvaluator;
import com.github.darl.maximka.MaximaException;
import org.armedbear.lisp.*;
import org.armedbear.lisp.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Vladislav Dolbilov (dvladislv@gmail.com)
 */
public class ABCLMaximaEvaluator implements MaximaEvaluator {
    private static final Logger log = LoggerFactory.getLogger(ABCLMaximaEvaluator.class);

    private static final Package DEFAULT_PACKAGE;
    private static final Function THROWING_DEBUG_HOOK;
    private static final Function MAXIMA_EVAL_FUNCTION;

    static {
        Interpreter.createInstance();
        iEval("Loading asdf", "(require 'asdf)");

        final ClassLoader classLoader = ABCLMaximaEvaluator.class.getClassLoader();

        iEval("Pushing path to maxima",
                String.format("(pushnew (make-pathname :defaults \"%s\" :name nil :type nil) asdf:*central-registry*)",
                        classLoader.getResource("maxima/maxima.asd")));

        iEval("Loading maxima", "(asdf:load-system :maxima)");

        log.debug("Loading predefined functions");
        final URL resource = classLoader.getResource("init.lisp");
        if (resource != null) {
            Load.load(resource.toString());
        }

        DEFAULT_PACKAGE = Packages.findPackage("CL-USER");
        MAXIMA_EVAL_FUNCTION = (Function) DEFAULT_PACKAGE.findAccessibleSymbol("MAXIMA-EVAL").getSymbolFunction();
        THROWING_DEBUG_HOOK = (Function) Lisp.PACKAGE_SYS.findAccessibleSymbol("%DEBUGGER-HOOK-FUNCTION").getSymbolFunction();
    }

    private static void iEval(String msg, String expr) {
        log.debug(msg + " - " + expr);
        Interpreter.getInstance().eval(expr);
    }

    private static String preprocessExpr(String text) {
        final String trimmed = text.trim();
        return trimmed.endsWith(";") || trimmed.endsWith("$")
                ? trimmed
                : trimmed + ";";
    }

    public String eval(final String text) throws MaximaException {
        final String expression = preprocessExpr(text);

        final LispThread thread = LispThread.currentThread();
        final SpecialBindingsMark mark = thread.markSpecialBindings();

        thread.bindSpecial(Symbol.DEBUGGER_HOOK, THROWING_DEBUG_HOOK);

        try {
            return MAXIMA_EVAL_FUNCTION
                    .execute(new SimpleString(expression))
                    .getStringValue()
                    .trim();
        } catch (Interpreter.UnhandledCondition x) {
            throw new MaximaException("Exception on evaluating expression: " + expression, x);
        } finally {
            thread.resetSpecialBindings(mark);
            thread.resetStack();
        }
    }
}
