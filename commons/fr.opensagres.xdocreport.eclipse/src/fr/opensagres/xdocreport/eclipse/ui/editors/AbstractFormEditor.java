package fr.opensagres.xdocreport.eclipse.ui.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import fr.opensagres.xdocreport.eclipse.extensions.IModelProvider;
import fr.opensagres.xdocreport.eclipse.extensions.modules.IReportModule;
import fr.opensagres.xdocreport.eclipse.extensions.modules.IReportModuleEntry;
import fr.opensagres.xdocreport.eclipse.extensions.modules.IReportModuleEntryProvider;
import fr.opensagres.xdocreport.eclipse.extensions.reporting.IReportFormat;
import fr.opensagres.xdocreport.eclipse.extensions.reporting.IReportProcessorType;
import fr.opensagres.xdocreport.eclipse.extensions.reporting.IReportProcessors;
import fr.opensagres.xdocreport.eclipse.ui.actions.ActionMenu;
import fr.opensagres.xdocreport.eclipse.ui.actions.GenerationReportAction;

public abstract class AbstractFormEditor<Model> extends FormEditor implements
		IReportModuleEntryProvider, IModelProvider<Model> {

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		if (!(input instanceof ModelAndEntryEditorInput<?>)) {
			throw new PartInitException("UserInput");
		}
		super.init(site, input);
	}

	public Model getModel() {
		return ((ModelAndEntryEditorInput<Model>) getEditorInput()).getModel();
	}

	public IReportModuleEntry getEntry() {
		return ((ModelAndEntryEditorInput<?>) getEditorInput()).getEntry();
	}

	public IReportModule getModule() {
		IReportModuleEntry entry = getEntry();
		if (entry == null) {
			return null;
		}
		return entry.getModule();
	}

	public IReportProcessors getReportProcessors() {
		IReportModule module = getModule();
		if (module == null) {
			return null;
		}
		return module.getProcessors();
	}

	public void contributeToToolbar(IToolBarManager manager) {
		contributeReportProcessorsToToolbar(manager);
	}

	protected void contributeReportProcessorsToToolbar(IToolBarManager manager) {

		IReportProcessors processors = getReportProcessors();
		if (processors == null || processors.getProcessorTypes().size() < 1) {
			return;
		}

		Map<IReportFormat, List<IReportProcessorType>> supportedFormats = processors
				.getSupportedFormats();
		for (Map.Entry<IReportFormat, List<IReportProcessorType>> supportedFormat : supportedFormats
				.entrySet()) {
			Action runAction = new ActionMenu(getReportProcessorsAction(
					supportedFormat.getValue(), supportedFormat.getKey()));
			manager.add(runAction);
		}
	}

	private List<Action> getReportProcessorsAction(
			List<IReportProcessorType> processorTypes, IReportFormat format) {
		List<Action> actions = new ArrayList<Action>();
		for (IReportProcessorType processorType : processorTypes) {
			actions.add(new GenerationReportAction(this, this, processorType,
					format, this.getSite()));
		}
		return actions;
	}
}
