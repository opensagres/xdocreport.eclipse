package org.dynaresume.eclipse.ui.editors;

import java.util.HashSet;
import java.util.Set;

import org.dynaresume.domain.hr.Education;
import org.dynaresume.domain.hr.Resume;
import org.dynaresume.eclipse.ui.internal.Messages;
import org.dynaresume.eclipse.ui.viewers.EducationContentProvider;
import org.dynaresume.eclipse.ui.viewers.EducationLabelProvider;
import org.dynaresume.eclipse.ui.viewers.EducationViewerComparator;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.rap.singlesourcing.SingleSourcingUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import fr.opensagres.eclipse.forms.ModelMasterDetailsBlock;

public class EducationsMasterDetailsBlock extends
		ModelMasterDetailsBlock<Resume> {

	private static final Integer ADD_BUTTON_INDEX = 1;
	private static final Integer REMOVE_BUTTON_INDEX = 2;

	private EducationDetailsPage educationDetailsPage;
	private TableViewer viewer;
	private Button removeButton;

	public EducationsMasterDetailsBlock(EducationsPage educationsPage) {
		super(educationsPage);
		this.educationDetailsPage = new EducationDetailsPage();
	}

	@Override
	protected void onCreateUI(final IManagedForm managedForm, Composite parent) {

		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION
				| Section.TITLE_BAR);
		section.setText(Messages.ResumeFormEditor_EducationsPage_EducationsMasterDetailsBlock_title); //$NON-NLS-1$
		section.setDescription(Messages.ResumeFormEditor_EducationsPage_EducationsMasterDetailsBlock_desc); //$NON-NLS-1$
		section.marginWidth = 10;
		section.marginHeight = 5;

		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);

		Table educationsTable = toolkit.createTable(client, SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 20;
		gd.widthHint = 100;
		educationsTable.setLayoutData(gd);
		SingleSourcingUtils.FormToolkit_paintBordersFor(toolkit, client);

		createButtons(toolkit, client);

		section.setClient(client);

		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		viewer = new TableViewer(educationsTable);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				if (selection.size() == 1) {
					managedForm.fireSelectionChanged(spart,
							event.getSelection());
				}
				removeButton.setEnabled(true);
			}
		});
		viewer.setContentProvider(EducationContentProvider.getInstance());
		viewer.setLabelProvider(EducationLabelProvider.getInstance());
		viewer.setComparator(EducationViewerComparator.getInstance());
	}

	private void createButtons(FormToolkit toolkit, Composite parent) {
		GridData gd;
		Composite buttonsContainer = toolkit.createComposite(parent);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonsContainer.setLayoutData(gd);
		buttonsContainer.setLayout(createButtonsLayout());

		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.widget.getData() == ADD_BUTTON_INDEX) {
					handleAddButton();
				} else if (e.widget.getData() == REMOVE_BUTTON_INDEX) {
					handleRemoveButton();
				}
			}
		};

		Button addButton = toolkit.createButton(buttonsContainer,
				Messages.addButton_label, SWT.PUSH); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING);
		addButton.setData(ADD_BUTTON_INDEX);
		addButton.setLayoutData(gd);
		addButton.setEnabled(true);
		addButton.addSelectionListener(listener);

		removeButton = toolkit.createButton(buttonsContainer,
				Messages.removeButton_label, SWT.PUSH); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING);
		removeButton.setData(REMOVE_BUTTON_INDEX);
		removeButton.setLayoutData(gd);
		removeButton.setEnabled(false);
		removeButton.addSelectionListener(listener);

		SingleSourcingUtils.FormToolkit_paintBordersFor(toolkit,
				buttonsContainer);
	}

	protected void handleAddButton() {
		Education education = new Education();
		education.setLabel("New education");
		getEducations().add(education);
		viewer.add(education);
		viewer.setSelection(new StructuredSelection(education));
	}

	protected void handleRemoveButton() {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		if (!selection.isEmpty()) {
			Education education = null;
			Object[] educations = selection.toArray();
			for (int i = 0; i < educations.length; i++) {
				education = (Education) educations[i];
				getEducations().remove(education);
				viewer.remove(education);
			}
			viewer.refresh();
		}
	}

	protected GridLayout createButtonsLayout() {
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		return layout;
	}

	@Override
	public void onBind(DataBindingContext dataBindingContext) {
		Set<Education> educations = getEducations();
		viewer.setInput(educations);
	}

	private Set<Education> getEducations() {
		Set<Education> educations = getModelObject().getEducations();
		if (educations == null) {
			educations = new HashSet<Education>();
			getModelObject().setEducations(educations);
		}
		return educations;
	}

	public IDetailsPage getPage(Object key) {
		return educationDetailsPage;
	}

	public Object getPageKey(Object object) {
		return null;
	}

}
