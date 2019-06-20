package mybatis.log.action.gui;

import mybatis.log.util.ConfigUtil;
import mybatis.log.util.StringConst;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * filter setting
 * @author ob
 */
public class FilterSetting extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea;

    public FilterSetting() {
        this.setTitle("Filter Setting"); //设置标题
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        //保存配置字符
        if(textArea != null && StringUtils.isNotBlank(textArea.getText())) {
            String[] filters = textArea.getText().split("\n");
            ConfigUtil.properties.setValues(StringConst.FILTER_KEY, filters);
        } else {
            ConfigUtil.properties.setValues(StringConst.FILTER_KEY, null);
        }
        this.setVisible(false);
    }

    private void onCancel() {
        // add your code here if necessary
        this.setVisible(false);
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public static void main(String[] args) {
        FilterSetting dialog = new FilterSetting();
        dialog.pack();
        dialog.setSize(600, 320);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);
    }
}
