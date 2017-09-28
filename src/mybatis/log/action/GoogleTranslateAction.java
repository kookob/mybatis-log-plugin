package mybatis.log.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.ide.CopyProvider;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.text.StringUtil;

import java.awt.datatransfer.DataFlavor;
import java.net.URLEncoder;

/**
 * Translate with Google
 * @author ob
 */
public class GoogleTranslateAction extends AnAction implements DumbAware {
    public GoogleTranslateAction() {
    }

    public void actionPerformed(AnActionEvent var1) {
        DataContext var2 = var1.getDataContext();
        CopyProvider var3 = (CopyProvider) PlatformDataKeys.COPY_PROVIDER.getData(var2);
        if (var3 != null) {
            var3.performCopy(var2);
            String var4 = (String) CopyPasteManager.getInstance().getContents(DataFlavor.stringFlavor);
            if (StringUtil.isNotEmpty(var4)) {
                BrowserUtil.browse("https://translate.google.com/#en/zh-CN/" + URLEncoder.encode(var4));
            }
        }
    }

    public void update(AnActionEvent var1) {
        Presentation var2 = var1.getPresentation();
        DataContext var3 = var1.getDataContext();
        CopyProvider var4 = (CopyProvider) PlatformDataKeys.COPY_PROVIDER.getData(var3);
        boolean var5 = var4 != null && var4.isCopyEnabled(var3) && var4.isCopyVisible(var3);
        var2.setEnabled(var5);
        var2.setVisible(var5);
    }
}