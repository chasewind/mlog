package com.bird.core.jaran;

import static com.bird.core.CoreConstants.SAFE_JORAN_CONFIGURATION;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.xml.sax.InputSource;

import com.bird.core.Context;
import com.bird.core.ContextAwareBase;
import com.bird.core.DefaultNestedComponentRegistry;
import com.bird.core.ElementPath;
import com.bird.core.InterpretationContext;
import com.bird.core.Interpreter;
import com.bird.core.SaxEvent;
import com.bird.core.exceptions.JoranException;

public abstract class GenericConfigurator extends ContextAwareBase {

    private final BeanDescriptionCache beanDescriptionCache = new BeanDescriptionCache();

    protected Interpreter              interpreter;

    public final void doConfigure(URL url) throws JoranException {
        InputStream in = null;
        try {
            informContextOfURLUsedForConfiguration(getContext(), url);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);

            in = urlConnection.getInputStream();
            doConfigure(in);
        } catch (IOException ioe) {
            String errMsg = "Could not open URL [" + url + "].";
            addError(errMsg, ioe);
            throw new JoranException(errMsg, ioe);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    String errMsg = "Could not close input stream";
                    addError(errMsg, ioe);
                    throw new JoranException(errMsg, ioe);
                }
            }
        }
    }

    public final void doConfigure(String filename) throws JoranException {
        doConfigure(new File(filename));
    }

    public final void doConfigure(File file) throws JoranException {
        FileInputStream fis = null;
        try {
            informContextOfURLUsedForConfiguration(getContext(), file.toURI().toURL());
            fis = new FileInputStream(file);
            doConfigure(fis);
        } catch (IOException ioe) {
            String errMsg = "Could not open [" + file.getPath() + "].";
            addError(errMsg, ioe);
            throw new JoranException(errMsg, ioe);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (java.io.IOException ioe) {
                    String errMsg = "Could not close [" + file.getName() + "].";
                    addError(errMsg, ioe);
                    throw new JoranException(errMsg, ioe);
                }
            }
        }
    }

    public static void informContextOfURLUsedForConfiguration(Context context, URL url) {
        ConfigurationWatchListUtil.setMainWatchURL(context, url);
    }

    public final void doConfigure(InputStream inputStream) throws JoranException {
        doConfigure(new InputSource(inputStream));
    }

    protected BeanDescriptionCache getBeanDescriptionCache() {
        return beanDescriptionCache;
    }

    protected abstract void addInstanceRules(RuleStore rs);

    protected abstract void addImplicitRules(Interpreter interpreter);

    protected void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {

    }

    protected ElementPath initialElementPath() {
        return new ElementPath();
    }

    protected void buildInterpreter() {
        RuleStore rs = new SimpleRuleStore(context);
        addInstanceRules(rs);
        this.interpreter = new Interpreter(context, rs, initialElementPath());
        InterpretationContext interpretationContext = interpreter.getInterpretationContext();
        interpretationContext.setContext(context);
        addImplicitRules(interpreter);
        addDefaultNestedComponentRegistryRules(interpretationContext.getDefaultNestedComponentRegistry());
    }

    public final void doConfigure(final InputSource inputSource) throws JoranException {
        SaxEventRecorder recorder = new SaxEventRecorder(context);
        recorder.recordEvents(inputSource);
        doConfigure(recorder.saxEventList);
    }

    public void doConfigure(final List<SaxEvent> eventList) throws JoranException {
        buildInterpreter();
        // disallow simultaneous configurations of the same context
        // 相同的上下文拒绝存在相同的配置
        synchronized (context.getConfigurationLock()) {
            interpreter.getEventPlayer().play(eventList);
        }
    }

    /**
     * Recall the event list previously registered as a safe point.
     */
    @SuppressWarnings("unchecked")
    public List<SaxEvent> recallSafeConfiguration() {
        return (List<SaxEvent>) context.getObject(SAFE_JORAN_CONFIGURATION);
    }
}
