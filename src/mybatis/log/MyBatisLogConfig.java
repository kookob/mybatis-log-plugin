package mybatis.log;

import com.intellij.ide.util.PropertiesComponent;
import mybatis.log.action.gui.FilterSetting;

/**
 * 配置项参数
 * @author ob
 */
public class MyBatisLogConfig {
    public static boolean running = false;
    public static boolean sqlFormat = false;
    public static int indexNum = 1;
    public static PropertiesComponent properties;//filter setting
    public static FilterSetting settingDialog;

}
