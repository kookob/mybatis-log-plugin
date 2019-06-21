package mybatis.log.action.gui;

import mybatis.log.hibernate.StringHelper;
import mybatis.log.util.BareBonesBrowserLaunch;
import mybatis.log.util.RestoreSqlUtil;
import mybatis.log.util.StringConst;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author ob
 * @email huanglingbin@chainfly.com
 * @date 2019/6/20
 */
public class SqlText extends JFrame {
    private static String preparingLine = "";
    private static String parametersLine = "";
    private static boolean isEnd = false;

    private JPanel panel1;
    private JButton buttonOK;
    private JButton buttonCopy;
    private JButton buttonClose;
    private JTextArea originalTextArea;
    private JTextArea resultTextArea;
    private JButton buttonClear;
    private JButton paypalDonate;
    private JButton alipayDonate;
    private JButton githubButton;

    public SqlText() {
        this.setTitle("restore sql from text"); //设置标题
        setContentPane(panel1);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(e -> onOK());
        buttonCopy.addActionListener(e -> onCopy());
        buttonClear.addActionListener(e -> onClear());
        buttonClose.addActionListener(e -> onClose());

        paypalDonate.setContentAreaFilled(false);
        paypalDonate.addActionListener(e -> BareBonesBrowserLaunch.openURL("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=2FQY2FH24H4LC&item_name=MyBatis+Log+Plugin&currency_code=USD&source=url"));
        alipayDonate.setContentAreaFilled(false);
        alipayDonate.addActionListener(e -> BareBonesBrowserLaunch.openURL("https://github.com/kookob/mybatis-log-plugin/blob/01b528df60df5cc990b87803e6c0c6ffae19f34c/DONATE.md"));

        githubButton.setBorder(null);
        githubButton.setContentAreaFilled(false);
        githubButton.addActionListener(e -> BareBonesBrowserLaunch.openURL("https://github.com/kookob/mybatis-log-plugin"));

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });
        panel1.registerKeyboardAction(e -> onClose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if(originalTextArea == null || StringUtils.isBlank(originalTextArea.getText())) {
            this.resultTextArea.setText("Can't restore sql from text.");
            return;
        }
        String originalText = originalTextArea.getText();
        if(originalText.contains(StringConst.PARAMETERS) && (originalText.contains(StringConst.PREPARING) || originalText.contains(StringConst.EXECUTING))) {
            String[] sqlArr = originalText.split("\n");
            if(sqlArr != null && sqlArr.length >= 2) {
                String resultSql = "";
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
                        if(org.apache.commons.lang.StringUtils.isBlank(parametersLine)) {
                            continue;
                        }
                        parametersLine += currentLine;
                    }
                    if(!parametersLine.endsWith("Parameters: \n") && !parametersLine.endsWith("null\n") && !parametersLine.endsWith(")\n")) {
                        if(i == sqlArr.length -1) {
                            this.resultTextArea.setText("Can't restore sql from text.");
                            break;
                        }
                        continue;
                    } else {
                        isEnd = true;
                    }
                    if(StringHelper.isNotEmpty(preparingLine) && StringHelper.isNotEmpty(parametersLine) && isEnd) {
                        resultSql += RestoreSqlUtil.restoreSql(preparingLine, parametersLine)
                                + "\n------------------------------------------------------------\n";
                    }
                }
                if(StringHelper.isNotEmpty(resultSql)) {
                    this.resultTextArea.setText(resultSql);
                }
            } else {
                this.resultTextArea.setText("Can't restore sql from text.");
            }
        } else {
            this.resultTextArea.setText("Can't restore sql from text.");
        }
    }

    private void onCopy() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(this.resultTextArea.getText());
        clipboard.setContents(selection, null);
    }

    private void onClear() {
        this.resultTextArea.setText("");
        this.originalTextArea.setText("");
    }

    private void onClose() {
        this.setVisible(false);
    }

    public static void main(String[] args) {
        SqlText dialog = new SqlText();
        dialog.pack();
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
//        System.exit(0);
    }
}
