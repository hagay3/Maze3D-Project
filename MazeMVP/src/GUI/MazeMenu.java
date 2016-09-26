package GUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MazeMenu {
  Display d;

  Shell s;
  
  @SuppressWarnings("unused")
  public MazeMenu(MazeWindow mazeWindow) {
    this.d = mazeWindow.display;
    this.s = mazeWindow.shell;
    
    Menu m = new Menu(s, SWT.BAR);

    // create a file menu and add an exit item
    final MenuItem file = new MenuItem(m, SWT.CASCADE);
    file.setText("File");
    final Menu filemenu = new Menu(s, SWT.DROP_DOWN);
    file.setMenu(filemenu);
    // create an open menu and to sub-menu items
    final MenuItem openItem = new MenuItem(filemenu, SWT.CASCADE);
    openItem.setText("Open");
    final Menu submenu = new Menu(s, SWT.DROP_DOWN);
    openItem.setMenu(submenu);
    final MenuItem childItem = new MenuItem(submenu, SWT.PUSH);
    childItem.setText("Child");
    final MenuItem dialogItem = new MenuItem(submenu, SWT.PUSH);
    dialogItem.setText("Dialog");
    //add a separator
    final MenuItem separator = new MenuItem(filemenu, SWT.SEPARATOR);
    // create an exit menu item
    final MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
    exitItem.setText("Exit");

    // create an edit menu and add cut copy and paste items
    final MenuItem edit = new MenuItem(m, SWT.CASCADE);
    edit.setText("Edit");
    final Menu editmenu = new Menu(s, SWT.DROP_DOWN);
    edit.setMenu(editmenu);
    final MenuItem cutItem = new MenuItem(editmenu, SWT.PUSH);
    cutItem.setText("Cut");
    final MenuItem copyItem = new MenuItem(editmenu, SWT.PUSH);
    copyItem.setText("Copy");
    final MenuItem pasteItem = new MenuItem(editmenu, SWT.PUSH);
    pasteItem.setText("Paste");

    //create a Window menu and add Child item
    final MenuItem window = new MenuItem(m, SWT.CASCADE);
    window.setText("Window");
    final Menu windowmenu = new Menu(s, SWT.DROP_DOWN);
    window.setMenu(windowmenu);
    final MenuItem maxItem = new MenuItem(windowmenu, SWT.PUSH);
    maxItem.setText("Maximize");
    final MenuItem minItem = new MenuItem(windowmenu, SWT.PUSH);
    minItem.setText("Minimize");

    // create a Help menu and add an about item
    final MenuItem help = new MenuItem(m, SWT.CASCADE);
    help.setText("Help");
    final Menu helpmenu = new Menu(s, SWT.DROP_DOWN);
    help.setMenu(helpmenu);
    final MenuItem aboutItem = new MenuItem(helpmenu, SWT.PUSH);
    aboutItem.setText("About");

    childItem.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        Shell parent = (Shell) maxItem.getParent().getParent();
        ChildShell cs = new ChildShell(parent);
      }

      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });

    dialogItem.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        Shell parent = (Shell) maxItem.getParent().getParent();
        DialogExample de = new DialogExample(parent);
        de.open();
      }

      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });

    exitItem.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        System.exit(0);
      }

      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });

    cutItem.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        System.out.println("Cut");
      }

      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });

    copyItem.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        System.out.println("Copy");
      }

      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });

    pasteItem.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        System.out.println("Paste");
      }

      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });

    maxItem.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        Shell parent = (Shell) maxItem.getParent().getParent();
        parent.setMaximized(true);
      }

      public void widgetDefaultSelected(SelectionEvent e) {
      }
    });

    minItem.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        Shell parent = (Shell) minItem.getParent().getParent();
        parent.setMaximized(false);
      }

      public void widgetDefaultSelected(SelectionEvent e) {
      }
    });

    aboutItem.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        Shell parent = (Shell) minItem.getParent().getParent();
        parent.setMaximized(false);
      }

      public void widgetDefaultSelected(SelectionEvent e) {
      }
    });

    s.setMenuBar(m);
    
  }
}

class DialogExample extends Dialog {
  DialogExample(Shell parent) {
    super(parent);
  }

  public String open() {
    Shell parent = getParent();
    Shell dialog = new Shell(parent, SWT.DIALOG_TRIM
        | SWT.APPLICATION_MODAL);
    dialog.setSize(100, 100);
    dialog.setText("Java Source and Support");
    dialog.open();
    Display display = parent.getDisplay();
    while (!dialog.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    return "After Dialog";
  }

  public static void main(String[] argv) {
    new DialogExample(new Shell());
  }
}
class ChildShell {

  ChildShell(Shell parent) {
    Shell child = new Shell(parent);
    child.setSize(200, 200);
    child.open();
  }
}

