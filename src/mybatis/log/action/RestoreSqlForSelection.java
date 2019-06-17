package mybatis.log.action;

import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import mybatis.log.Icons;
import mybatis.log.hibernate.StringHelper;
import mybatis.log.tail.TailRunExecutor;
import mybatis.log.util.ConfigUtil;
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
        final Project project = e.getProject();
        if (project == null) return;
        final String projectBasePath = project.getBasePath();
        CaretModel caretModel = e.getData(LangDataKeys.EDITOR).getCaretModel();
        Caret currentCaret = caretModel.getCurrentCaret();
        String sqlText = currentCaret.getSelectedText();
        if(ConfigUtil.runningMap.get(projectBasePath) == null || ConfigUtil.runningMap.get(projectBasePath) == false) {
            new ShowLogInConsoleAction(project).showLogInConsole(project);
        }
        //激活Restore Sql tab
        ToolWindowManager.getInstance(project).getToolWindow(TailRunExecutor.TOOLWINDOWS_ID).activate(null);
        if(StringUtils.isNotBlank(sqlText) && sqlText.contains(StringConst.PARAMETERS) && (sqlText.contains(StringConst.PREPARING) || sqlText.contains(StringConst.EXECUTING))) {
            String[] sqlArr = sqlText.split("\n");
            if(sqlArr != null && sqlArr.length >= 2) {
                for(int i=0; i<sqlArr.length; ++i) {
                    String currentLine = sqlArr[i];
                    if(StringUtils.isBlank(currentLine)) {
                        continue;
                    }
                    if(currentLine.contains(StringConst.PREPARING) || currentLine.contains(StringConst.EXECUTING)) {
                        preparingLine = currentLine;
                        continue;
                    } else {
                        currentLine += "\n";
                    }
                    if(StringHelper.isEmpty(preparingLine)) {
                        continue;
                    }
                    if(currentLine.contains(StringConst.PARAMETERS)) {
                        parametersLine = currentLine;
                    } else {
                        if(StringUtils.isBlank(parametersLine)) {
                            continue;
                        }
                        parametersLine += currentLine;
                    }
                    if(!parametersLine.endsWith("Parameters: \n") && !parametersLine.endsWith("null\n") && !parametersLine.endsWith(")\n")) {
                        if(i == sqlArr.length -1) {
                            PrintUtil.println(project, "Can't restore sql from selection.", PrintUtil.getOutputAttributes(null, Color.yellow));
                            PrintUtil.println(project, StringConst.SPLIT_LINE, ConsoleViewContentType.USER_INPUT);
                            this.reset();
                            break;
                        }
                        continue;
                    } else {
                        isEnd = true;
                    }
                    if(StringHelper.isNotEmpty(preparingLine) && StringHelper.isNotEmpty(parametersLine) && isEnd) {
                        int indexNum = ConfigUtil.indexNumMap.get(projectBasePath);
                        String preStr = indexNum + "  restore sql from selection  - ==>";
                        ConfigUtil.indexNumMap.put(projectBasePath, ++indexNum);
                        PrintUtil.println(project, preStr, ConsoleViewContentType.USER_INPUT);
                        String restoreSql = RestoreSqlUtil.restoreSql(preparingLine, parametersLine);
                        if(ConfigUtil.sqlFormatMap.get(projectBasePath)) {
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