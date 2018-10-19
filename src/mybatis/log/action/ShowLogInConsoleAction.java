package mybatis.log.action;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Disposer;
import mybatis.log.action.gui.FilterSetting;
import mybatis.log.tail.TailContentExecutor;
import mybatis.log.util.ConfigUtil;
import mybatis.log.util.StringConst;
import org.apache.commons.lang.StringUtils;


/**
 * 插件入口
 * @author ob
 */
public class ShowLogInConsoleAction extends DumbAwareAction {

    public ShowLogInConsoleAction(Project project) {
        super();
        ConfigUtil.properties = PropertiesComponent.getInstance(project);
        ConfigUtil.settingDialog = new FilterSetting();
        ConfigUtil.init(project);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) return;
    }

    public void showLogInConsole(final Project project) {
        if (project == null) return;
        final String projectBasePath = project.getBasePath();
        ConfigUtil.runningMap.put(projectBasePath, true);
        final TailContentExecutor executor = new TailContentExecutor(project);
        Disposer.register(project, executor);
        executor.withRerun(new Runnable() {
            @Override
            public void run() {
                showLogInConsole(project);
            }
        });
        executor.withStop(new Runnable() {
            @Override
            public void run() {
                ConfigUtil.runningMap.put(projectBasePath, false);
                ConfigUtil.indexNumMap.put(projectBasePath, 1);
            }
        }, new Computable<Boolean>() {
            @Override
            public Boolean compute() {
                return ConfigUtil.runningMap.get(projectBasePath);
            }
        });
        executor.withFormat(new Runnable() {
            @Override
            public void run() {
                ConfigUtil.sqlFormatMap.put(projectBasePath, !ConfigUtil.sqlFormatMap.get(projectBasePath));
            }
        });
        executor.withFilter(new Runnable() {
            @Override
            public void run() {
                //启动filter配置
                FilterSetting dialog = ConfigUtil.settingDialog;
                dialog.pack();
                dialog.setSize(600, 320);//配置大小
                dialog.setLocationRelativeTo(null);//位置居中显示
                String[] filters = ConfigUtil.properties.getValues(StringConst.FILTER_KEY);//读取过滤字符
                if (filters != null && filters.length > 0) {
                    dialog.getTextArea().setText(StringUtils.join(filters, "\n"));
                }
                dialog.setVisible(true);
            }
        });
        executor.run();
    }

}
