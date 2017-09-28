package mybatis.log.action;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import mybatis.log.MyBatisLogConfig;
import mybatis.log.action.gui.FilterSetting;

/**
 * 初始化数据
 * @author ob
 */
public class TailMyBatisLog extends DumbAwareAction {
	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = getEventProject(e);
		new ShowLogInConsoleAction().showLogInConsole(project);
		MyBatisLogConfig.properties = PropertiesComponent.getInstance(project);
		MyBatisLogConfig.settingDialog = new FilterSetting();
	}
}
