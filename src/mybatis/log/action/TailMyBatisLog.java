package mybatis.log.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;

/**
 * 初始化数据
 * @author ob
 */
public class TailMyBatisLog extends DumbAwareAction {
	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = getEventProject(e);
		new ShowLogInConsoleAction(project).showLogInConsole(project);
	}
}
