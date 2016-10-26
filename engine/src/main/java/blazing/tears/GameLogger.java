package blazing.tears;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameLogger {
    private static Logger mLogger = Logger.getLogger(GameLogger.class.getName());
    private static FileHandler mFileHandler = null;

    public static Logger instance() {
        if (mFileHandler == null) {
            SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");

            try {
                // This block configure the logger with handler and formatter
                mFileHandler = new FileHandler("GameLogger_" + format.format(Calendar.getInstance().getTime()) + ".log");
                mLogger.addHandler(mFileHandler);
                SimpleFormatter formatter = new SimpleFormatter();
                mFileHandler.setFormatter(formatter);

                // the following statement is used to log any messages
                mLogger.info("GameLogger initialized");

                // Stop console logging for this logger
                mLogger.setUseParentHandlers(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return mLogger;
    }
}
