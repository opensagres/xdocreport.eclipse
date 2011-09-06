package fr.opensagres.xdocreport.eclipse.registry;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public abstract class AbstractRegistry implements IRegistryChangeListener {

	protected static final String ID_ATTR = "id";
	protected static final String NAME_ATTR = "name";
	protected static final String DESCRIPTION_ATTR = "description";
	protected static final String CLASS_ATTR = "class";

	private boolean registryListenerIntialized = false;

	public void initialize() {

	}

	public void destroy() {
		Platform.getExtensionRegistry().removeRegistryChangeListener(this);
	}

	private void addRegistryListenerIfNeeded() {
		if (registryListenerIntialized)
			return;

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			registry.addRegistryChangeListener(this, getPluginId());
		}
		registryListenerIntialized = true;
	}

	public void registryChanged(IRegistryChangeEvent event) {
		IExtensionDelta[] deltas = event.getExtensionDeltas(getPluginId(),
				getExtensionPoint());
		if (deltas != null) {
			for (IExtensionDelta delta : deltas)
				handleExtensionDelta(delta);
		}
	}

	protected void loadRegistryIfNedded() {
		if (!isRegistryIntialized()) {
			loadRegistry();
			addRegistryListenerIfNeeded();
		}
	}

	protected boolean isRegistryIntialized() {
		return registryListenerIntialized;
	}

	protected void updateIcon(IConfigurationElement cfig, IIconAware iconAware) {
		String strIcon = cfig.getAttribute("icon");//$NON-NLS-1$
		if (strIcon != null) {
			ImageDescriptor imageDescriptor = AbstractUIPlugin
					.imageDescriptorFromPlugin(cfig.getNamespace(), strIcon);
			if (imageDescriptor != null) {
				Image image = JFaceResources.getResources()
						.createImageWithDefault(imageDescriptor);
				iconAware.setIcon(image);
			}
		}
	}

	protected abstract void loadRegistry();

	protected abstract void handleExtensionDelta(IExtensionDelta delta);

	protected abstract String getPluginId();

	protected abstract String getExtensionPoint();
}
