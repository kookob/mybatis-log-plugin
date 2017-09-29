package mybatis.log.tail;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.execution.ui.actions.CloseAction;
import com.intellij.execution.ui.layout.PlaceInGrid;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonShortcuts;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import mybatis.log.Icons;
import mybatis.log.MyBatisLogConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Copy of com.intellij.execution.RunContentExecutor Runs a process and prints the output in a content tab within the
 * Run toolwindow.
 *
 * @author ob
 */
public class TailContentExecutor implements Disposable {
    private final Project myProject;
    private final List<Filter> myFilterList = new ArrayList<>();
    private Runnable myFilterAction;
    private Runnable myRerunAction;
    private Runnable myStopAction;
    private Runnable myFormatAction;
    private Computable<Boolean> myStopEnabled;
    private String myTitle = " ";//插件窗口标题
    private String myHelpId = null;
    private boolean myActivateToolWindow = true;
    private ConsoleView consoleView = null;

    public TailContentExecutor(@NotNull Project project) {
        myProject = project;
        consoleView = createConsole(project);
        MyBatisLogConfig.consoleViewMap.put(project.getBasePath(), consoleView);
    }

    public TailContentExecutor withTitle(String title) {
        myTitle = title;
        return this;
    }

    public TailContentExecutor withFilter(Runnable filter) {
        myFilterAction = filter;
        return this;
    }

    public TailContentExecutor withRerun(Runnable rerun) {
        myRerunAction = rerun;
        return this;
    }

    public TailContentExecutor withStop(@NotNull Runnable stop, @NotNull Computable<Boolean> stopEnabled) {
        myStopAction = stop;
        myStopEnabled = stopEnabled;
        return this;
    }

    public TailContentExecutor withFormat(Runnable format) {
        myFormatAction = format;
        return this;
    }

    private ConsoleView createConsole(@NotNull Project project) {
        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        consoleBuilder.filters(myFilterList);
        ConsoleView console = consoleBuilder.getConsole();
        return console;
    }

    public void run() {
        FileDocumentManager.getInstance().saveAllDocuments();

        if (myHelpId != null) {
            consoleView.setHelpId(myHelpId);
        }
        Executor executor = TailRunExecutor.getRunExecutorInstance();
        DefaultActionGroup actions = new DefaultActionGroup();

        // Create runner UI layout
        final RunnerLayoutUi.Factory factory = RunnerLayoutUi.Factory.getInstance(myProject);
        final RunnerLayoutUi layoutUi = factory.create("Tail", "Tail", "Tail", myProject);

        final JComponent consolePanel = createConsolePanel(consoleView, actions);
        RunContentDescriptor descriptor = new RunContentDescriptor(new RunProfile() {
            @Nullable
            @Override
            public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
                return null;
            }

            @Override
            public String getName() {
                return myTitle;
            }

            @Nullable
            @Override
            public Icon getIcon() {
                return null;
            }
        }, new DefaultExecutionResult(), layoutUi);

        final Content content = layoutUi.createContent("ConsoleContent", consolePanel, myTitle, AllIcons.Debugger.Console, consolePanel);
        layoutUi.addContent(content, 0, PlaceInGrid.right, false);
        layoutUi.getOptions().setLeftToolbar(createActionToolbar(consolePanel, consoleView, layoutUi, descriptor, executor), "RunnerToolbar");

        Disposer.register(this, descriptor);
        Disposer.register(content, consoleView);
        if (myStopAction != null) {
            Disposer.register(consoleView, new Disposable() {
                @Override
                public void dispose() {
                    myStopAction.run();
                }
            });
        }

        for (AnAction action : consoleView.createConsoleActions()) {
            actions.add(action);
        }

        ExecutionManager.getInstance(myProject).getContentManager().showRunContent(executor, descriptor);

        if (myActivateToolWindow) {
            activateToolWindow();
        }
    }

    @NotNull
    private ActionGroup createActionToolbar(JComponent consolePanel, ConsoleView consoleView, @NotNull final RunnerLayoutUi myUi, RunContentDescriptor contentDescriptor, Executor runExecutorInstance) {
        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new FilterAction());
        actionGroup.add(new RerunAction(consolePanel, consoleView));
        actionGroup.add(new StopAction());
        actionGroup.add(new FormatAction());
        actionGroup.add(new MyCloseAction(runExecutorInstance, contentDescriptor, myProject));
        return actionGroup;
    }

    public void activateToolWindow() {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                ToolWindowManager.getInstance(myProject).getToolWindow(TailRunExecutor.TOOLWINDOWS_ID).activate(null);
            }
        });
    }

    private static JComponent createConsolePanel(ConsoleView view, ActionGroup actions) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(view.getComponent(), BorderLayout.CENTER);
        panel.add(createToolbar(actions), BorderLayout.WEST);
        return panel;
    }

    private static JComponent createToolbar(ActionGroup actions) {
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, actions, false);
        return actionToolbar.getComponent();
    }

    @Override
    public void dispose() {
        Disposer.dispose(this);
    }

    private class RerunAction extends AnAction implements DumbAware {
        private final ConsoleView consoleView;

        public RerunAction(JComponent consolePanel, ConsoleView consoleView) {
            super("Rerun", "Rerun", AllIcons.Actions.Restart);
            this.consoleView = consoleView;
            registerCustomShortcutSet(CommonShortcuts.getRerun(), consolePanel);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            Disposer.dispose(consoleView);
            myRerunAction.run();
        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setVisible(myRerunAction != null);
            e.getPresentation().setIcon(AllIcons.Actions.Restart);
        }
    }

    private class StopAction extends AnAction implements DumbAware {
        public StopAction() {
            super("Stop", "Stop", AllIcons.Actions.Suspend);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            myStopAction.run();
        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setVisible(myStopAction != null);
            e.getPresentation().setEnabled(myStopEnabled != null && myStopEnabled.compute());
        }
    }

    private class FormatAction extends ToggleAction implements DumbAware {
        public FormatAction() {
            super("Format Sql", "Format Sql", Icons.FormatIcon);
        }

        @Override
        public boolean isSelected(AnActionEvent anActionEvent) {
            return MyBatisLogConfig.getConfigVo(getEventProject(anActionEvent)).getSqlFormat();
        }

        @Override
        public void setSelected(AnActionEvent anActionEvent, boolean state) {
            myFormatAction.run();
        }
    }

    private class MyCloseAction extends CloseAction implements DumbAware {
        public MyCloseAction(Executor executor, RunContentDescriptor contentDescriptor, Project project) {
            super(executor, contentDescriptor, project);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            MyBatisLogConfig.getConfigVo(getEventProject(e)).setRunning(false);
            MyBatisLogConfig.getConfigVo(getEventProject(e)).setIndexNum(1);
            super.actionPerformed(e);
        }
    }

    private class FilterAction extends AnAction implements DumbAware {
        public FilterAction() {
            super("Filter", "Filter", Icons.FilterIcon);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            myFilterAction.run();
        }
    }
}
