package mybatis.log.util;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import mybatis.log.action.gui.FilterSetting;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置项参数
 *
 * @author ob
 */
public class ConfigUtil {
    public static PropertiesComponent properties;//filter setting
    public static FilterSetting settingDialog;
    public static Map<String, ConsoleView> consoleViewMap = new HashMap<>();
    public static Map<String, Boolean> runningMap = new HashMap<>();
    public static Map<String, Boolean> sqlFormatMap = new HashMap<>();
    public static Map<String, Integer> indexNumMap = new HashMap<>();

    public static void init(Project project) {
        if(project != null) {
            ConfigUtil.runningMap.put(project.getBasePath(), false);
            ConfigUtil.sqlFormatMap.put(project.getBasePath(), false);
            ConfigUtil.indexNumMap.put(project.getBasePath(), 1);
        }
    }
}
