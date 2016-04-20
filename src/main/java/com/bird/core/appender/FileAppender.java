package com.bird.core.appender;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Map;
import java.util.Map.Entry;

import com.bird.core.CoreConstants;
import com.bird.core.recovery.ResilientFileOutputStream;

/**
 * 类FileAppender.java的实现描述：TODO 类实现描述：输出日志到文件
 * 
 * @author dongwei.ydw 2016年4月19日 下午7:39:02
 */
public class FileAppender<E> extends OutputStreamAppender<E> {

    static protected String COLLISION_WITH_EARLIER_APPENDER_URL = CoreConstants.CODES_URL + "#earlier_fa_collision";

    /** 判断是进行追加操作还是把之前的清空之后再写 */
    protected boolean       append                              = true;

    protected String        fileName                            = null;

    private boolean         prudent                             = false;

    public void setFile(String file) {
        if (file == null) {
            fileName = file;
        } else {
            fileName = file.trim();
        }
    }

    /**
     * 判断是进行追加操作还是把之前的清空之后再写
     */
    public boolean isAppend() {
        return append;
    }

    /**
     * 获取文件原始属性，这里返回的是文件名
     */
    final public String rawFileProperty() {
        return fileName;
    }

    public String getFile() {
        return fileName;
    }

    @Override
    public void start() {
        int errors = 0;
        if (getFile() != null) {
            addInfo("File property is set to [" + fileName + "]");

            if (checkForFileCollisionInPreviousFileAppenders()) {
                addError("Collisions detected with FileAppender/RollingAppender instances defined earlier. Aborting.");
                addError(CoreConstants.MORE_INFO_PREFIX + COLLISION_WITH_EARLIER_APPENDER_URL);
                errors++;
            }

            if (prudent) {
                if (!isAppend()) {
                    setAppend(true);
                    addError("Setting \"Append\" property to true on account of \"Prudent\" mode");
                }
            }

            try {
                openFile(getFile());
            } catch (java.io.IOException e) {
                errors++;
                addError("openFile(" + fileName + "," + append + ") call failed.", e);
            }
        } else {
            errors++;
            addError("\"File\" property not set for appender named [" + name + "].");
        }
        if (errors == 0) {
            super.start();
        }
    }

    protected boolean checkForFileCollisionInPreviousFileAppenders() {
        boolean collisionsDetected = false;
        if (fileName == null) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) context.getObject(CoreConstants.RFA_FILENAME_PATTERN_COLLISION_MAP);
        if (map == null) {
            return collisionsDetected;
        }
        for (Entry<String, String> entry : map.entrySet()) {
            if (fileName.equals(entry.getValue())) {
                addErrorForCollision("File", entry.getValue(), entry.getKey());
                collisionsDetected = true;
            }
        }
        if (name != null) {
            map.put(getName(), fileName);
        }
        return collisionsDetected;
    }

    protected void addErrorForCollision(String optionName, String optionValue, String appenderName) {
        addError("'" + optionName + "' option has the same value \"" + optionValue + "\" as that given for appender ["
                 + appenderName + "] defined earlier.");
    }

    public void openFile(String file_name) throws IOException {
        lock.lock();
        try {
            File file = new File(file_name);
            boolean result = createMissingParentDirectories(file);
            if (!result) {
                addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
            }

            ResilientFileOutputStream resilientFos = new ResilientFileOutputStream(file, append);
            resilientFos.setContext(context);
            setOutputStream(resilientFos);
        } finally {
            lock.unlock();
        }
    }

    public boolean isPrudent() {
        return prudent;
    }

    public void setPrudent(boolean prudent) {
        this.prudent = prudent;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    private void safeWrite(E event) throws IOException {
        ResilientFileOutputStream resilientFOS = (ResilientFileOutputStream) getOutputStream();
        FileChannel fileChannel = resilientFOS.getChannel();
        if (fileChannel == null) {
            return;
        }

        boolean interrupted = Thread.interrupted();

        FileLock fileLock = null;
        try {
            fileLock = fileChannel.lock();
            long position = fileChannel.position();
            long size = fileChannel.size();
            if (size != position) {
                fileChannel.position(size);
            }
            super.writeOut(event);
        } catch (IOException e) {
            resilientFOS.postIOFailure(e);
        } finally {
            if (fileLock != null && fileLock.isValid()) {
                fileLock.release();
            }

            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    protected void writeOut(E event) throws IOException {
        if (prudent) {
            safeWrite(event);
        } else {
            super.writeOut(event);
        }
    }

    static public boolean createMissingParentDirectories(File file) {
        File parent = file.getParentFile();
        if (parent == null) {
            return true;
        }

        parent.mkdirs();
        return parent.exists();
    }
}
