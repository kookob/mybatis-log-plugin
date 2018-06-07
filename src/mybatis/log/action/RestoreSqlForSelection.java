package mybatis.log.action;

import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import mybatis.log.ConfigVo;
import mybatis.log.Icons;
import mybatis.log.MyBatisLogConfig;
import mybatis.log.hibernate.StringHelper;
import mybatis.log.tail.TailRunExecutor;
import mybatis.log.util.PrintUtil;
import mybatis.log.util.RestoreSqlUtil;
import mybatis.log.util.StringConst;
import org.apache.commons.lang.StringUtils;

import java.awt.*;

/**
 * restore sql from selection
 * @author ob
 */
public class RestoreSqlForSelection extends AnAction {
    private static String preparingLine = "";
    private static String parametersLine = "";
    private static boolean isEnd = false;

    public RestoreSqlForSelection(){
        super(null,null, Icons.MyBatisIcon);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = getEventProject(e);
        ConfigVo configVo = MyBatisLogConfig.getConfigVo(project);
        CaretModel caretModel = e.getData(LangDataKeys.EDITOR).getCaretModel();
        Caret currentCaret = caretModel.getCurrentCaret();
        String sqlText = currentCaret.getSelectedText();
        if(!configVo.getRunning()) {
            new ShowLogInConsoleAction(project).showLogInConsole(project);
        }
        //激活Restore Sql tab
        ToolWindowManager.getInstance(project).getToolWindow(TailRunExecutor.TOOLWINDOWS_ID).activate(null);
        if(StringUtils.isNotBlank(sqlText) && sqlText.contains(StringConst.PARAMETERS) && sqlText.contains(StringConst.PREPARING)) {
            String[] sqlArr = sqlText.split("\n");
            if(sqlArr != null && sqlArr.length >= 2) {
                for(int i=0; i<sqlArr.length; ++i) {
                    String currentLine = sqlArr[i];
                    if(StringUtils.isBlank(currentLine)) {
                        continue;
                    }
                    if(currentLine.contains(StringConst.PREPARING)) {
                        preparingLine = currentLine;
                        continue;
                    } else {
                        currentLine += "\n";
                    }
                    if(StringHelper.isEmpty(preparingLine)) {
                        continue;
                    }
                    parametersLine = currentLine.contains(StringConst.PARAMETERS) ? currentLine : parametersLine + currentLine;
                    if(!parametersLine.endsWith("Parameters: \n") && !parametersLine.endsWith("null\n") && !parametersLine.endsWith(")\n")) {
                        continue;
                    } else {
                        isEnd = true;
                    }
                    if(StringHelper.isNotEmpty(preparingLine) && StringHelper.isNotEmpty(parametersLine) && isEnd) {
                        String preStr = configVo.getIndexNum() + "  restore sql from selection  - ==>";
                        configVo.setIndexNum(configVo.getIndexNum() + 1);
                        PrintUtil.println(project, preStr, ConsoleViewContentType.USER_INPUT);
                        String restoreSql = RestoreSqlUtil.restoreSql(preparingLine, parametersLine);
                        if(configVo.getSqlFormat()) {
                            restoreSql = PrintUtil.format(restoreSql);
                        }
                        PrintUtil.println(project, restoreSql, PrintUtil.getOutputAttributes(null, new Color(255,200,0)));//高亮显示
                        PrintUtil.println(project, StringConst.SPLIT_LINE, ConsoleViewContentType.USER_INPUT);
                        this.reset();
                    }
                }
            } else {
                PrintUtil.println(project, "Can't restore sql from selection.", PrintUtil.getOutputAttributes(null, Color.yellow));
                PrintUtil.println(project, StringConst.SPLIT_LINE, ConsoleViewContentType.USER_INPUT);
                this.reset();
            }
        } else {
            PrintUtil.println(project, "Can't restore sql from selection.", PrintUtil.getOutputAttributes(null, Color.yellow));
            PrintUtil.println(project, StringConst.SPLIT_LINE, ConsoleViewContentType.USER_INPUT);
            this.reset();
        }
    }

    private void reset(){
        preparingLine = "";
        parametersLine = "";
        isEnd = false;
    }
}