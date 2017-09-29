package mybatis.log;

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
public class MyBatisLogConfig {
    public static PropertiesComponent properties;//filter setting
    public static FilterSetting settingDialog;
    public static Map<String, ConfigVo> configMap = new HashMap<>();
    public static Map<String, ConsoleView> consoleViewMap = new HashMap<>();

    public static ConfigVo getConfigVo(Project project) {
        ConfigVo configVo = configMap.get(project.getBasePath());
        if(configVo == null) {
            //初始化参数设置
            configVo = new ConfigVo();
            configVo.setRunning(false);
            configVo.setSqlFormat(false);
            configVo.setIndexNum(1);
            MyBatisLogConfig.setConfigVo(project, configVo);
        }
        return configVo;
    }

    public static void setConfigVo(Project project, ConfigVo configVo) {
        configMap.put(project.getBasePath(), configVo);
    }
}
