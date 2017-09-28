package mybatis.log.util;

import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.editor.markup.TextAttributes;
import mybatis.log.hibernate.BasicFormatterImpl;
import mybatis.log.hibernate.Formatter;
import mybatis.log.tail.TailContentExecutor;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * 打印简单工具类
 * @author ob
 */
public class PrintUtil {
    public static ConsoleViewContentType getOutputAttributes(@Nullable Color foregroundColor, @Nullable Color backgroundColor) {
        //@Nullable Color foregroundColor, @Nullable Color backgroundColor, @Nullable Color effectColor, EffectType effectType, @FontStyle int fontType
        return new ConsoleViewContentType("styleName", new TextAttributes(foregroundColor, backgroundColor, null, null, Font.PLAIN));
    }

    public static void println(String line, ConsoleViewContentType consoleViewContentType) {
        if (TailContentExecutor.consoleView != null) {
            TailContentExecutor.consoleView.print(line + "\n", consoleViewContentType);
        }
    }

    /**
     * 增删改sql改用蓝色标识
     *
     * @param line
     */
    public static void println(String line) {
        if (StringUtils.isNotBlank(line)) {
            String lowerLine = line.toLowerCase().trim();
            if (lowerLine.startsWith("insert") || lowerLine.startsWith("update")) {
                println(line, ConsoleViewContentType.SYSTEM_OUTPUT);
            } else if (lowerLine.startsWith("delete")) {
                println(line, getOutputAttributes(Color.RED, null));
            } else {
                println(line, ConsoleViewContentType.ERROR_OUTPUT);
            }
        }
    }

    /**
     * format sql statements
     * @param sql
     * @return
     */
    public static String format(String sql) {
        Formatter formatter = new BasicFormatterImpl();
        return formatter.format(sql);
    }
}
