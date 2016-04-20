package com.bird.core.appender;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

import com.bird.core.Encoder;

/**
 * 类OutputStreamAppender.java的实现描述：TODO 类实现描述:线程非同步，让调用者自己处理线程同步问题，该类不处理,同时挂接{@link OutputStream}
 * 
 * @author dongwei.ydw 2016年4月19日 下午7:34:34
 */
public class OutputStreamAppender<E> extends AppenderBase<E> {

    protected final ReentrantLock lock = new ReentrantLock(true);
    protected Encoder<E>          encoder;
    private OutputStream          outputStream;

    @Override
    protected void append(E event) {
        if (!isStarted()) {
            return;
        }

        subAppend(event);
    }

    public Encoder<E> getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder<E> encoder) {
        this.encoder = encoder;
    }

    public void setOutputStream(OutputStream outputStream) {
        lock.lock();
        try {
            closeOutputStream();

            this.outputStream = outputStream;
            if (encoder == null) {
                System.err.println("Encoder has not been set. Cannot invoke its init method.\n");
                return;
            }

            encoderInit();
        } finally {
            lock.unlock();
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    void encoderInit() {
        if (encoder != null && this.outputStream != null) {
            try {
                encoder.init(outputStream);
            } catch (IOException ioe) {
                this.started = false;
                System.err.println("Failed to initialize encoder for appender named [" + name + "].");
            }
        }
    }

    protected void closeOutputStream() {
        if (this.outputStream != null) {
            try {
                // before closing we have to output out layout's footer
                encoderClose();
                this.outputStream.close();
                this.outputStream = null;
            } catch (IOException e) {
                System.err.println("Could not close output stream for OutputStreamAppender.");
            }
        }
    }

    void encoderClose() {
        if (encoder != null && this.outputStream != null) {
            try {
                encoder.close();
            } catch (IOException ioe) {
                this.started = false;
                System.err.println("Failed to write footer for appender named [" + name + "].");
            }
        }
    }

    protected void subAppend(E event) {
        if (!isStarted()) {
            return;
        }
        try {
            lock.lock();
            try {
                writeOut(event);
            } finally {
                lock.unlock();
            }
        } catch (IOException ioe) {
            this.started = false;
            System.err.println("IO failure in appender\n");
        }
    }

    protected void writeOut(E event) throws IOException {
        this.encoder.doEncode(event);
    }
}
