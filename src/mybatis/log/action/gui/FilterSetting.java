package mybatis.log.action.gui;

import mybatis.log.MyBatisLogConfig;
import org.apache.commons.lang.StringUtils;
import mybatis.log.util.StringConst;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JLabel label;

    public FilterSetting() {
        this.setTitle("Filter Setting"); //设置标题
        //设置label值，采用html语法分行
        StringBuilder description = new StringBuilder("<html><body>")
                .append("Filter the contents that contain below character, split every line.")
                .append("</body></html>");
        this.label.setText(description.toString());

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        //保存配置字符
        if(textArea != null && StringUtils.isNotBlank(textArea.getText())) {
            String[] filters = textArea.getText().split("\n");
            MyBatisLogConfig.properties.setValues(StringConst.FILTER_KEY, filters);
        } else {
            MyBatisLogConfig.properties.setValues(StringConst.FILTER_KEY, null);
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
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);
    }
}
