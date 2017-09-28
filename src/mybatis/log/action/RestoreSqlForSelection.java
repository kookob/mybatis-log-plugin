package mybatis.log.action;

import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.wm.ToolWindowManager;
import mybatis.log.MyBatisLogConfig;
import mybatis.log.tail.TailRunExecutor;
import mybatis.log.util.RestoreSqlUtil;
import org.apache.commons.lang.StringUtils;
import mybatis.log.Icons;
import mybatis.log.util.PrintUtil;
import mybatis.log.util.StringConst;

import java.awt.*;

/**
 * restore sql for selection
 * @author ob
 */
public class RestoreSqlForSelection extends AnAction {
    private static String prevLine = "";

    public RestoreSqlForSelection(){
        super(null,null, Icons.MyBatisIcon);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        CaretModel caretModel = e.getData(LangDataKeys.EDITOR).getCaretModel();
        Caret currentCaret = caretModel.getCurrentCaret();
        String sqlText = currentCaret.getSelectedText();
        if(!MyBatisLogConfig.running) {
            new ShowLogInConsoleAction(getEventProject(e)).showLogInConsole(getEventProject(e));
        }
        //激活Restore Sql tab
        ToolWindowManager.getInstance(e.getProject()).getToolWindow(TailRunExecutor.TOOLWINDOWS_ID).activate(null);
        if(StringUtils.isNotBlank(sqlText) && sqlText.contains(StringConst.PARAMETERS) && sqlText.contains(StringConst.PREPARING)) {
            String[] sqlArr = sqlText.split("\n");
            if(sqlArr != null && sqlArr.length >= 2) {
                for(int i=0; i<sqlArr.length; ++i) {
                    String currentLine = sqlArr[i];
                    if(currentLine.contains(StringConst.PARAMETERS) && StringUtils.isNotEmpty(prevLine) && prevLine.contains(StringConst.PREPARING)) {
                        String preStr = MyBatisLogConfig.indexNum++ + "  restore sql from selection  - ==>";
                        PrintUtil.println(preStr, ConsoleViewContentType.USER_INPUT);
                        String restoreSql = RestoreSqlUtil.restoreSql(prevLine, currentLine);
                        if(MyBatisLogConfig.sqlFormat) {
                            restoreSql = PrintUtil.format(restoreSql);
                        }
                        PrintUtil.println(restoreSql, PrintUtil.getOutputAttributes(null, new Color(255,200,0)));//高亮显示
                        PrintUtil.println(StringConst.SPLIT_LINE, ConsoleViewContentType.USER_INPUT);
                    } else if(currentLine.contains(StringConst.PREPARING)) {
                        prevLine = currentLine;
                    } else {
                        prevLine = null;
                    }
                }
            }
        } else {
            PrintUtil.println("Can't restore sql from selection.", PrintUtil.getOutputAttributes(null, Color.yellow));
            PrintUtil.println(StringConst.SPLIT_LINE, ConsoleViewContentType.USER_INPUT);
        }
    }
}