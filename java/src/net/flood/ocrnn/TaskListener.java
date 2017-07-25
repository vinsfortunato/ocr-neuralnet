package net.flood.ocrnn;

/**
 * @author flood2d
 */
public interface TaskListener<T> {
    void onTaskEnd(T result);
}
