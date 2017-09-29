package mybatis.log.action;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Disposer;
import mybatis.log.ConfigVo;
import mybatis.log.MyBatisLogConfig;
import mybatis.log.tail.TailContentExecutor;
import org.apache.commons.lang.StringUtils;
import mybatis.log.action.gui.FilterSetting;
import mybatis.log.util.StringConst;


/**
 * 插件入口
 * @author ob
 */
public class ShowLogInConsoleAction extends DumbAwareAction {

    public ShowLogInConsoleAction() {
    }

    public ShowLogInConsoleAction(Project project) {
        super();
        MyBatisLogConfig.properties = PropertiesComponent.getInstance(project);
        MyBatisLogConfig.settingDialog = new FilterSetting();
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
    }

    public void showLogInConsole(final Project project) {
        ConfigVo configVo = MyBatisLogConfig.getConfigVo(project);
        configVo.setRunning(true);
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
                configVo.setRunning(false);
                configVo.setIndexNum(1);
            }
        }, new Computable<Boolean>() {
            @Override
            public Boolean compute() {
                return configVo.getRunning();
            }
        });
        executor.withFormat(new Runnable() {
            @Override
            public void run() {
                configVo.setSqlFormat(!configVo.getSqlFormat());
            }
        });
        executor.withFilter(new Runnable() {
            @Override
            public void run() {
                //启动filter配置
                FilterSetting dialog = MyBatisLogConfig.settingDialog;
                dialog.pack();
                dialog.setSize(500, 300);//配置大小
                dialog.setLocationRelativeTo(null);//位置居中显示
                String[] filters = MyBatisLogConfig.properties.getValues(StringConst.FILTER_KEY);//读取过滤字符
                if (filters != null && filters.length > 0) {
                    dialog.getTextArea().setText(StringUtils.join(filters, "\n"));
                }
                dialog.setVisible(true);
            }
        });
        executor.run();
    }

}
