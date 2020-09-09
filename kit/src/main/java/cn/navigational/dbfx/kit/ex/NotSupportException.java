package cn.navigational.dbfx.kit.ex;

/**
 * Some functions are not implemented.
 * If these functions are called, the exception will be thrown.</p>
 *
 * @author yangkui
 * @since 1.0
 */
public class NotSupportException extends RuntimeException {
    public NotSupportException() {
    }

    public NotSupportException(String message) {
        super(message);
    }
}
