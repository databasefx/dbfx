package cn.navigational.dbfx.utils;

import ch.qos.logback.core.OutputStreamAppender;
import cn.navigational.dbfx.controller.LogController;


/**
 * Custom log output target
 *
 * @param <E>
 * @author yangkui
 * @since 1.0
 */
public class SqlConsoleAppender<E> extends OutputStreamAppender<E> {

    private final LogController target = LogController.Companion.getLogController();

    @Override
    public void start() {
        var stream = target.getOutput();
        if (target.getListenerLog()) {
            setOutputStream(stream);
        }
        super.start();
    }


    /**
     * Returns the current value of the <b>target</b> property. The default value
     * of the option is {@link LogController}.
     * <p>
     */
    public String getTarget() {
        return target.getClass().getName();
    }
}
