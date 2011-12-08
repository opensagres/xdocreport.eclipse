package org.eclipse.nebula.widgets.pagination.banner;

import org.eclipse.nebula.widgets.pagination.PageControllerChangedListener;
import org.eclipse.nebula.widgets.pagination.PaginationController;
import org.eclipse.nebula.widgets.pagination.PaginationHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.forms.events.HyperlinkEvent;

public class ResultAndPageLinksBannerWidget extends Composite implements
		SelectionListener, PageControllerChangedListener {

	private static final String END_HREF = "</a>";
	private static final String OPEN_START_HREF = "<a href=\"";
	private static final String OPEN_END_HREF = "\" >";

	private final PaginationController controller;
	private Link firstLink;
	private Link lastLink;
	private Link previousLink;
	private Link nextLink;
	private Label resultsLabel;
	private Link pageLinks;

	public ResultAndPageLinksBannerWidget(PaginationController controller,
			Composite parent, int style) {
		super(parent, style);
		this.controller = controller;
		createUI();
		controller.addPageSelectionListener(this);
		refreshEnabled(controller);
	}

	protected void createUI() {
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		this.setLayout(layout);

		createLeftContainer(this);
		createRightContainer(this);
	}

	private void addHyperlinkListener(Link link,
			ResultAndPageLinksBannerWidget paginationBannerWidget2) {
		link.addSelectionListener(this);
	}

	private void createLeftContainer(Composite parent) {
		Composite left = createComposite(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		left.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		left.setLayout(layout);

		resultsLabel = new Label(left, SWT.NONE);
		resultsLabel.setText("Results");
		resultsLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private void createRightContainer(Composite parent) {
		Composite right = createComposite(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalAlignment = SWT.END;
		right.setLayoutData(data);

		RowLayout layout = new RowLayout();
		layout.marginHeight = 0;
		layout.wrap = false;
		// layout.fill=true;
		right.setLayout(layout);

		// First link
		firstLink = createHyperlink(right, SWT.WRAP);
		setLinkText(firstLink, "[First");
		firstLink.setLayoutData(new RowData());
		addHyperlinkListener(firstLink, this);

		Label l = new Label(right, SWT.WRAP);
		l.setText("/");
		l.setLayoutData(new RowData());

		// Previous link
		previousLink = createHyperlink(right, SWT.WRAP);
		setLinkText(previousLink, "Prev]");
		previousLink.setToolTipText("Previous");
		previousLink.setLayoutData(new RowData());
		addHyperlinkListener(previousLink, this);

		pageLinks = createHyperlink(right, SWT.NONE);
		//setLinkText(pageLinks, "");
		// pageLinks.setToolTipText("Next");
		RowData r = new RowData();
		// r.width=50;
		pageLinks.setLayoutData(r);
		addHyperlinkListener(pageLinks, this);

		// Next link
		nextLink = createHyperlink(right, SWT.WRAP);
		setLinkText(nextLink, "[Next");
		nextLink.setToolTipText("Next");
		nextLink.setLayoutData(new RowData());
		addHyperlinkListener(nextLink, this);

		l = new Label(right, SWT.WRAP);
		l.setText("/");
		l.setLayoutData(new RowData());

		// Last link
		lastLink = createHyperlink(right, SWT.WRAP);
		setLinkText(lastLink, "Last]");
		lastLink.setLayoutData(new RowData());
		addHyperlinkListener(lastLink, this);

	}

	public void widgetDefaultSelected(SelectionEvent e) {

	}

	public void widgetSelected(SelectionEvent e) {
		Link hyperlink = (Link) e.getSource();
		int newCurrentPage = 0;
		if (hyperlink == firstLink) {
			newCurrentPage = 0;
		} else if (hyperlink == lastLink) {
			newCurrentPage = controller.getTotalPages() - 1;
		} else if (hyperlink == previousLink) {
			newCurrentPage = controller.getCurrentPage() - 1;
		} else if (hyperlink == nextLink) {
			newCurrentPage = controller.getCurrentPage() + 1;
		} else if (hyperlink == pageLinks) {
			newCurrentPage = Integer.parseInt(e.text);
		}
		controller.setCurrentPage(newCurrentPage);
	}

	public void linkEntered(HyperlinkEvent e) {

	}

	public void linkExited(HyperlinkEvent e) {

	}

	public void pageSelected(int oldPageNumber, int newPageNumber,
			PaginationController controller) {
		refreshEnabled(controller);

		int n = controller.getPageOffset() > 0 ? controller.getPageOffset() + 1
				: 9;
		StringBuilder s = new StringBuilder();
		
		//PaginationHelper.getPageIndex(nbMax)
		
		for (int i = 0; i < n; i++) {
			if (i > 0) {
				s.append(" ");
			}
			if (i == newPageNumber)
				s.append("" + (i + 1) + "");
			else
				s.append("<a href=\"" + i + "\">" + (i + 1) + "</a>");

		}
		pageLinks.setText(s.toString());
		// pageLinks.redraw();
		// pageLinks.getParent().layout();
	}

	public void totalElementsChanged(long oldTotalElements,
			long newTotalElements, PaginationController controller) {
		refreshEnabled(controller);
	}

	private void refreshEnabled(PaginationController controller) {
		resultsLabel.setText(getResultsText(controller));
		nextLink.setEnabled(controller.hasNextPage());
		previousLink.setEnabled(controller.hasPreviousPage());
	}

	protected void displayResults(PaginationController controller) {

	}

	protected String getResultsText(PaginationController controller) {
		int start = controller.getPageOffset() + 1;
		int end = start + controller.getPageSize() - 1;
		long total = controller.getTotalElements();
		if (end > total) {
			end = (int) total;
		}
		return getResultsText(start, end, total, controller);
	}

	protected String getResultsText(int start, int end, long total,
			PaginationController controller2) {
		return "Results " + start + "-" + end + " of " + total;
	}

	protected Composite createComposite(Composite parent, int style) {
		return new Composite(parent, style);
	}

	protected Link createHyperlink(Composite parent, int style) {
		return new Link(parent, style);
	}

	protected void setLinkText(Link link, String text) {
		StringBuilder href = new StringBuilder();
		href.append(OPEN_START_HREF);
		href.append(text);
		href.append(OPEN_END_HREF);
		href.append(text);
		href.append(END_HREF);
		link.setText(href.toString());
	}

}
