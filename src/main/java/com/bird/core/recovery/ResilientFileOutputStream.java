package com.bird.core.recovery;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class ResilientFileOutputStream extends ResilientOutputStreamBase {

    private File             file;
    private FileOutputStream fos;

    public ResilientFileOutputStream(File file, boolean append) throws FileNotFoundException{
        this.file = file;
        fos = new FileOutputStream(file, append);
        this.os = new BufferedOutputStream(fos);
        this.presumedClean = true;
    }

    public FileChannel getChannel() {
        if (os == null) {
            return null;
        }
        return fos.getChannel();
    }

    public File getFile() {
        return file;
    }

    @Override
    String getDescription() {
        return "file [" + file + "]";
    }

    @Override
    OutputStream openNewOutputStream() throws IOException {
        // see LOGBACK-765
        fos = new FileOutputStream(file, true);
        return new BufferedOutputStream(fos);
    }

    @Override
    public String toString() {
        return "com.bird.core.recovery.ResilientFileOutputStream@" + System.identityHashCode(this);
    }

}
