package GUI;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import view.CommonView;

public abstract class BasicWindow extends CommonView implements Runnable {

	protected Display display;
	protected Shell shell;
	protected abstract void initWidgets();
	
	@Override
	public void run() {
		display = new Display();  // our display
		shell = new Shell(display); // our window
		initWidgets();
		shell.open();
		// main event loop
		while (!shell.isDisposed()) { // while window isn't closed
			// 1. read events, put then in a queue.
			// 2. dispatch the assigned listener
			if (!display.readAndDispatch()) { // if the queue is empty
				display.sleep(); // sleep until an event occurs
			}
		}
		exit();
	}
	/**
	 * Exit method, dispose gui windows
	 */
	public void exit(){
		shell.dispose(); //dispose shell
		display.dispose(); // dispose OS components
	}
}
