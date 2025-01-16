package com.github.awesome.springboot.commons.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个{@link Runnable}的实现，提供异常处理和日志记录功能。
 * <p>
 * 该类包装了一个给定的{@link Runnable}，并添加了异常处理，记录任务的开始、失败和完成时间。
 * 同时，它还会记录执行时间（毫秒）。
 * </p>
 * <p>
 * 通过异常处理机制，即使被包装的任务抛出异常，也能够确保异常被记录，而不会中断其他任务的执行。
 * </p>
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.1.0
 */
public class ExceptionAwareRunnable implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ExceptionAwareRunnable.class);

    /**
     * 该 runnable 实例的唯一标识符，用于日志记录。
     */
    private final String identifier;

    /**
     * 要执行的委托{@link Runnable}任务。
     */
    private final Runnable delegate;

    /**
     * 构造一个{@link ExceptionAwareRunnable}实例，指定标识符和委托的 runnable。
     *
     * @param identifier 一个唯一的标识符，用于日志记录
     * @param delegate   要执行的{@link Runnable}任务
     */
    public ExceptionAwareRunnable(String identifier, Runnable delegate) {
        this.identifier = identifier;
        this.delegate = delegate;
    }

    /**
     * 执行委托的{@link Runnable}并记录执行状态和时间。
     * <p>
     * 该方法会记录执行的开始和结束时间，并以毫秒为单位记录执行时间。
     * 如果执行过程中发生异常，将会捕获并记录该异常。
     * </p>
     */
    @Override
    public void run() {
        final long startMillis = System.currentTimeMillis();
        try {
            log.info("{} Runnable started", identifier);
            delegate.run();
        } catch (Exception e) {
            log.error("{} Runnable failed", identifier, e);
        } finally {
            log.info("{} Runnable finished in {}ms", identifier, System.currentTimeMillis() - startMillis);
        }
    }

}
