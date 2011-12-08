package runjettyrun.tabs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import runjettyrun.JettyLaunchConfigurationClassPathProvider;
import runjettyrun.Plugin;
import runjettyrun.RunJettyRunMessages;
import runjettyrun.tabs.action.AddClassFolderAction;
import runjettyrun.tabs.action.AddExternalFolderAction;
import runjettyrun.tabs.action.AddExternalJarAction;
import runjettyrun.tabs.action.AddJarAction;
import runjettyrun.tabs.action.AddProjectAction;
import runjettyrun.tabs.action.RemoveAction;
import runjettyrun.tabs.action.RestoreDefaultEntriesAction;
import runjettyrun.tabs.action.RuntimeClasspathAction;
import runjettyrun.tabs.classpath.ClasspathEntry;
import runjettyrun.tabs.classpath.ClasspathLabelProvider;
import runjettyrun.tabs.classpath.IClasspathEntry;
import runjettyrun.tabs.classpath.IClasspathViewer;
import runjettyrun.tabs.classpath.IEntriesChangedListener;
import runjettyrun.tabs.classpath.RuntimeClasspathViewer;
import runjettyrun.tabs.classpath.UserClassesClasspathContentProvider;
import runjettyrun.tabs.classpath.UserClassesClasspathModel;

/**
 * A launch configuration tab that displays and edits the user and bootstrap
 * classes comprising the classpath launch configuration attribute.
 * <p>
 * This class may be instantiated.
 * </p>
 *
 * @since 2.0
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class AbstractClasspathTab extends JavaLaunchTab implements
		IEntriesChangedListener {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(AbstractClasspathTab.class.getName());

	protected JettyLaunchConfigurationClassPathProvider classpathProvider = new JettyLaunchConfigurationClassPathProvider();

	private String tabname;
	private String id;

	protected RuntimeClasspathViewer fClasspathViewer;
	private UserClassesClasspathModel fModel;

	private static final Set<String> empty = Collections
			.unmodifiableSet(new HashSet<String>());

	protected static final String DIALOG_SETTINGS_PREFIX = "JavaClasspathTab"; //$NON-NLS-1$

	/**
	 * The last launch config this tab was initialized from
	 */
	protected ILaunchConfiguration fLaunchConfiguration;

	public AbstractClasspathTab(String id, String tabname) {
		this.id = id;
		this.tabname = tabname;
	}

	abstract String getCustomAttributeName();

	abstract String getNonCheckedAttributeName();

	protected JettyLaunchConfigurationClassPathProvider getClasspathProvider() {
		return classpathProvider;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jdt.internal.debug.ui.launcher.IEntriesChangedListener#
	 * entriesChanged
	 * (org.eclipse.jdt.internal.debug.ui.launcher.IClasspathViewer)
	 */
	public void entriesChanged(IClasspathViewer viewer) {
		// setDirty(true);
		try {
			saveEntries(fLaunchConfiguration.getWorkingCopy());
		} catch (CoreException e) {
			throw new IllegalStateException(e);
		}
		// updateLaunchConfigurationDialog();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse
	 * .swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Font font = parent.getFont();

		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
		// IJavaDebugHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_CLASSPATH_TAB);
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 2;
		comp.setLayout(topLayout);
		GridData gd;

		Label label = new Label(comp, SWT.NONE);
		label.setText(tabname);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

		fClasspathViewer = new RuntimeClasspathViewer(comp);
		fClasspathViewer.setCheckStateProvider(new ICheckStateProvider() {
			public boolean isGrayed(Object element) {
				return false;
			}

			@SuppressWarnings("unchecked")
			public boolean isChecked(Object element) {
				Set<String> checked;
				try {
					checked = (Set<String>) fLaunchConfiguration.getAttribute(
							getNonCheckedAttributeName(),
							AbstractClasspathTab.empty);
				} catch (CoreException e) {
					return true;
				}

				if (checked == AbstractClasspathTab.empty)
					return true;

				boolean ret = !checked.contains(element.toString());
				return ret;
			}
		});
		fClasspathViewer.addCheckStateListener(new ICheckStateListener() {
			@SuppressWarnings("unchecked")
			public void checkStateChanged(CheckStateChangedEvent event) {
				Set<String> checked = null;
				try {
					checked = (Set<String>) fLaunchConfiguration
							.getAttribute(getNonCheckedAttributeName(),
									new HashSet<String>());
				} catch (CoreException e) {
				}

				if (event.getChecked()) {
					removeEntry(checked, (IClasspathEntry) event.getElement());
				} else {
					addEntry(checked, (IClasspathEntry) event.getElement());
				}

				fClasspathViewer.refresh();
				try {
					ILaunchConfigurationWorkingCopy workingcopy = fLaunchConfiguration
							.getWorkingCopy();
					workingcopy.setAttribute(getNonCheckedAttributeName(),
							checked);
					workingcopy.doSave();
				} catch (CoreException e) {
					logger.severe("CheckStateChangedEvent - exception: " + e);
				}
			}
		});
		fClasspathViewer.addEntriesChangedListener(this);
		fClasspathViewer.getControl().setFont(font);
		fClasspathViewer.setLabelProvider(new ClasspathLabelProvider());
		fClasspathViewer
				.setContentProvider(new UserClassesClasspathContentProvider(
						this));

		Composite pathButtonComp = new Composite(comp, SWT.NONE);
		GridLayout pathButtonLayout = new GridLayout();
		pathButtonLayout.marginHeight = 0;
		pathButtonLayout.marginWidth = 0;
		pathButtonComp.setLayout(pathButtonLayout);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.HORIZONTAL_ALIGN_FILL);
		pathButtonComp.setLayoutData(gd);
		pathButtonComp.setFont(font);

		createPathButtons(pathButtonComp);
	}

	private void removeEntry(Set<String> set, IClasspathEntry entry) {
		set.remove(entry.toString());
		if (logger.isLoggable(Level.CONFIG)) {
			logger.config("Set<String>, IClasspathEntry - removed:"
					+ entry.toString());
		}
		IClasspathEntry[] entrys = entry.getEntries();
		if (entrys != null && entrys.length > 0) {
			for (IClasspathEntry childentry : entrys) {
				removeEntry(set, childentry);
			}
		}

		if (entry instanceof ClasspathEntry) {
			ClasspathEntry ce = (ClasspathEntry) entry;
			ITreeContentProvider contentProvider = (ITreeContentProvider) fClasspathViewer
					.getContentProvider();
			IClasspathEntry[] childentrys = (IClasspathEntry[]) contentProvider
					.getChildren(ce);

			if (childentrys != null && childentrys.length > 0) {
				for (IClasspathEntry childentry : childentrys) {
					removeEntry(set, childentry);
				}
			}
		}
	}

	private void addEntry(Set<String> set, IClasspathEntry entry) {
		set.add(entry.toString());
		if (logger.isLoggable(Level.CONFIG)) {
			logger.config("Set<String>, IClasspathEntry - add:"
					+ entry.toString());
		}
		IClasspathEntry[] entrys = entry.getEntries();
		if (entrys != null && entrys.length > 0) {
			for (IClasspathEntry childentry : entrys) {
				addEntry(set, childentry);
			}
		}

		if (entry instanceof ClasspathEntry) {
			ClasspathEntry ce = (ClasspathEntry) entry;
			ITreeContentProvider contentProvider = (ITreeContentProvider) fClasspathViewer
					.getContentProvider();
			IClasspathEntry[] childentrys = (IClasspathEntry[]) contentProvider
					.getChildren(ce);

			if (childentrys != null && childentrys.length > 0) {
				for (IClasspathEntry childentry : childentrys) {
					addEntry(set, childentry);
				}
			}
		}
	}

	/**
	 * Creates the buttons to manipulate the classpath.
	 *
	 * @param pathButtonComp
	 *            composite buttons are contained in
	 * @since 3.0
	 */
	protected void createPathButtons(Composite pathButtonComp) {

		createButton(pathButtonComp, new RemoveAction(fClasspathViewer));
		createButton(pathButtonComp, new AddProjectAction(fClasspathViewer));
		createButton(pathButtonComp, new AddClassFolderAction(fClasspathViewer));
		createButton(pathButtonComp, new AddJarAction(fClasspathViewer));

		createButton(pathButtonComp, new AddExternalJarAction(fClasspathViewer,
				DIALOG_SETTINGS_PREFIX));
		createButton(pathButtonComp, new AddExternalFolderAction(
				fClasspathViewer, DIALOG_SETTINGS_PREFIX));

		RuntimeClasspathAction action = new RestoreDefaultEntriesAction(
				fClasspathViewer, this, this.getCustomAttributeName());

		createButton(pathButtonComp, action);
		action.setEnabled(true);
	}

	/**
	 * Creates a button for the given action.
	 *
	 * @param pathButtonComp
	 *            parent composite for the button
	 * @param action
	 *            the action triggered by the button
	 * @return the button that was created
	 */
	protected Button createButton(Composite pathButtonComp,
			RuntimeClasspathAction action) {
		Button button = createPushButton(pathButtonComp, action.getText(), null);
		action.setButton(button);
		return button;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.
	 * debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse
	 * .debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
		refresh(configuration);
		fClasspathViewer.expandToLevel(3);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#activated(org.eclipse.debug
	 * .core.ILaunchConfigurationWorkingCopy)
	 */
	public void activated(ILaunchConfigurationWorkingCopy workingCopy) {
		try {
			boolean useDefault = workingCopy.getAttribute(
					IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH,
					true);
			if (useDefault) {
				if (!isDefaultClasspath(getCurrentClasspath(), workingCopy)) {
					initializeFrom(workingCopy);
					return;
				}
			}
			fClasspathViewer.refresh();
		} catch (CoreException e) {
		}
	}

	/**
	 * Refreshes the classpath entries based on the current state of the given
	 * launch configuration.
	 */
	private void refresh(ILaunchConfiguration configuration) {
		// boolean useDefault = true;
		// setErrorMessage(null);
		// try {
		// useDefault =
		// configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH,
		// true);
		// } catch (CoreException e) {
		// Plugin.logError(e);
		// }
		//
		// if (configuration == getLaunchConfiguration()) {
		// // no need to update if an explicit path is being used and this
		// setting
		// // has not changed (and viewing the same config as last time)
		// if (!useDefault) {
		// setDirty(false);
		// return;
		// }
		// }

		setLaunchConfiguration(configuration);
		try {
			fModel = createClasspathModel(configuration);
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
		}

		fClasspathViewer.setLaunchConfiguration(configuration);
		fClasspathViewer.setInput(fModel);
		setDirty(false);
	}

	public abstract UserClassesClasspathModel createClasspathModel(
			ILaunchConfiguration configuration) throws Exception;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse
	 * .debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		if (isDirty()) {
			saveEntries(configuration);
		}
	}

	private void saveEntries(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, true);

		IRuntimeClasspathEntry[] customClasspath = getCurrentCustomClasspath();
		try {
			List<String> mementos = new ArrayList<String>(
					customClasspath.length);
			for (int i = 0; i < customClasspath.length; i++) {
				IRuntimeClasspathEntry entry = customClasspath[i];
				mementos.add(entry.getMemento());
			}
			configuration.setAttribute(getCustomAttributeName(), mementos);
			configuration.doSave();
		} catch (CoreException e) {
			Plugin.statusDialog(
					RunJettyRunMessages.JavaClasspathTab_Unable_to_save_classpath_1,
					e.getStatus());
		}
	}

	/**
	 * Returns the classpath entries currently specified by this tab.
	 *
	 * @return the classpath entries currently specified by this tab
	 */
	private IRuntimeClasspathEntry[] getCurrentClasspath() {
		IClasspathEntry[] user = fModel
				.getEntries(UserClassesClasspathModel.USER);
		List<IRuntimeClasspathEntry> entries = new ArrayList<IRuntimeClasspathEntry>(
				user.length);
		IRuntimeClasspathEntry entry;
		IClasspathEntry userEntry;
		for (int i = 0; i < user.length; i++) {
			userEntry = user[i];
			entry = null;
			if (userEntry instanceof ClasspathEntry) {
				entry = ((ClasspathEntry) userEntry).getDelegate();
			} else if (userEntry instanceof IRuntimeClasspathEntry) {
				entry = (IRuntimeClasspathEntry) user[i];
			}
			if (entry != null) {
				entry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
				entries.add(entry);
			}
		}
		return (IRuntimeClasspathEntry[]) entries
				.toArray(new IRuntimeClasspathEntry[entries.size()]);
	}

	private IRuntimeClasspathEntry[] getCurrentCustomClasspath() {
		IClasspathEntry[] user = fModel
				.getEntries(UserClassesClasspathModel.CUSTOM);
		List<IRuntimeClasspathEntry> entries = new ArrayList<IRuntimeClasspathEntry>(
				user.length);
		IRuntimeClasspathEntry entry;
		IClasspathEntry userEntry;
		for (int i = 0; i < user.length; i++) {
			userEntry = user[i];
			entry = null;
			if (userEntry instanceof ClasspathEntry) {
				entry = ((ClasspathEntry) userEntry).getDelegate();
			} else if (userEntry instanceof IRuntimeClasspathEntry) {
				entry = (IRuntimeClasspathEntry) user[i];
			}
			if (entry != null) {
				entry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
				entries.add(entry);
			}
		}
		return (IRuntimeClasspathEntry[]) entries
				.toArray(new IRuntimeClasspathEntry[entries.size()]);
	}

	/**
	 * Returns whether the specified classpath is equivalent to the default
	 * classpath for this configuration.
	 *
	 * @param classpath
	 *            classpath to compare to default
	 * @param configuration
	 *            original configuration
	 * @return whether the specified classpath is equivalent to the default
	 *         classpath for this configuration
	 */
	private boolean isDefaultClasspath(IRuntimeClasspathEntry[] classpath,
			ILaunchConfiguration configuration) {
		try {
			ILaunchConfigurationWorkingCopy wc = configuration.getWorkingCopy();
			wc.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH,
					true);
			IRuntimeClasspathEntry[] entries = JavaRuntime
					.computeUnresolvedRuntimeClasspath(wc);
			if (classpath.length == entries.length) {
				for (int i = 0; i < entries.length; i++) {
					IRuntimeClasspathEntry entry = entries[i];
					if (!entry.equals(classpath[i])) {
						return false;
					}
				}
				return true;
			}
			return false;
		} catch (CoreException e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return this.tabname;
	}

	/**
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getId()
	 *
	 * @since 3.3
	 */
	public String getId() {
		return "runjettyrun.tabs.classpath." + this.id; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getImage()
	 */
	public static Image getClasspathImage() {
		return JavaClasspathTab.getClasspathImage();
	}

	/**
	 * Sets the launch configuration for this classpath tab
	 */
	private void setLaunchConfiguration(ILaunchConfiguration config) {
		fLaunchConfiguration = config;
	}

	/**
	 * Returns the current launch configuration
	 */
	public ILaunchConfiguration getLaunchConfiguration() {
		return fLaunchConfiguration;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#dispose()
	 */
	public void dispose() {
		if (fClasspathViewer != null) {
			fClasspathViewer.removeEntriesChangedListener(this);
		}
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getImage()
	 */
	public Image getImage() {
		return getClasspathImage();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#isValid(org.eclipse.debug
	 * .core.ILaunchConfiguration)
	 */
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setMessage(null);
		String projectName = null;
		try {
			projectName = launchConfig.getAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); //$NON-NLS-1$
		} catch (CoreException e) {
			return false;
		}
		if (projectName.length() > 0) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IStatus status = workspace.validateName(projectName,
					IResource.PROJECT);
			if (status.isOK()) {
				IProject project = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(projectName);
				if (!project.exists()) {
					setErrorMessage(MessageFormat.format(
							RunJettyRunMessages.ClasspathTab_projectNotFound,
							new Object[] { projectName }));
					return false;
				}
				if (!project.isOpen()) {
					setErrorMessage(MessageFormat.format(
							RunJettyRunMessages.JavaMainTab_21,
							new Object[] { projectName }));
					return false;
				}
			} else {
				setErrorMessage(MessageFormat.format(
						RunJettyRunMessages.JavaMainTab_19,
						new Object[] { status.getMessage() }));
				return false;
			}
		}

		IRuntimeClasspathEntry[] entries = fModel.getAllEntries();
		int type = -1;
		for (int i = 0; i < entries.length; i++) {
			type = entries[i].getType();
			if (type == IRuntimeClasspathEntry.ARCHIVE) {
				if (!entries[i].getPath().isAbsolute()) {
					setErrorMessage(MessageFormat
							.format(RunJettyRunMessages.JavaClasspathTab_Invalid_runtime_classpath_1,
									new Object[] { entries[i].getPath()
											.toString() }));
					return false;
				}
			}
			if (type == IRuntimeClasspathEntry.PROJECT) {
				IResource res = entries[i].getResource();
				if (res != null && !res.isAccessible()) {
					setErrorMessage(MessageFormat.format(
							RunJettyRunMessages.JavaClasspathTab_1,
							new Object[] { res.getName() }));
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Returns whether the bootpath should be displayed.
	 *
	 * @return whether the bootpath should be displayed
	 * @since 3.0
	 */
	public boolean isShowBootpath() {
		return true;
	}

	/**
	 * @return Returns the classpath model.
	 */
	protected UserClassesClasspathModel getModel() {
		return fModel;
	}
}