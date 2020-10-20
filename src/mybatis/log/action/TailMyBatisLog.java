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
		final Project project = e.getProject();
		if (project == null) return;
		new ShowLogInConsoleAction(project).showLogInConsole(project);
	}
}
