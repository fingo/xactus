/*******************************************************************************
 * Copyright (c) 2008, 2009 Standards for Technology in Automotive Retail and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     David Carver - STAR - bug 213849 - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.wst.xml.xpath.ui.internal.util;

import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.xml.xpath.ui.internal.XPathUIPlugin;

/**
 * Helper class to handle images provided by this plug-in.
 * 
 * NOTE: For internal use only. For images used externally, please use the
 * Shared***ImageHelper class instead.
 * 
 * @author david carver based on work by amywu
 */
public class XPathPluginImageHelper {
	private static XPathPluginImageHelper instance = null;

	/**
	 * Gets the instance.
	 * 
	 * @return Returns a XMLEditorPluginImageHelper
	 */
	public synchronized static XPathPluginImageHelper getInstance() {
		if (instance == null) {
			instance = new XPathPluginImageHelper();
		}
		return instance;
	}

	// save a descriptor for each image
	@SuppressWarnings("unchecked")
	private HashMap fImageDescRegistry = null;

	// private final String PLUGINID = XPathUIPlugin.PLUGIN_ID;

	/**
	 * Creates an image from the given resource and adds the image to the image
	 * registry.
	 * 
	 * @param resource
	 * @return Image
	 */
	private Image createImage(String resource) {
		ImageDescriptor desc = getImageDescriptor(resource);
		Image image = null;

		if (desc != null) {
			image = desc.createImage();
			// dont add the missing image descriptor image to the image
			// registry
			if (!desc.equals(ImageDescriptor.getMissingImageDescriptor())) {
				getImageRegistry().put(resource, image);
			}
		}
		return image;
	}

	/**
	 * Creates an image descriptor from the given imageFilePath and adds the
	 * image descriptor to the image descriptor registry. If an image descriptor
	 * could not be created, the default "missing" image descriptor is returned
	 * but not added to the image descriptor registry.
	 * 
	 * @param imageFilePath
	 * @return ImageDescriptor image descriptor for imageFilePath or default
	 *         "missing" image descriptor if resource could not be found
	 */
	@SuppressWarnings("unchecked")
	private ImageDescriptor createImageDescriptor(String imageFilePath) {
		ImageDescriptor imageDescriptor = AbstractUIPlugin
				.imageDescriptorFromPlugin(XPathUIPlugin.PLUGIN_ID,
						imageFilePath);
		if (imageDescriptor != null) {
			getImageDescriptorRegistry().put(imageFilePath, imageDescriptor);
		} else {
			imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
		}

		return imageDescriptor;
	}

	/**
	 * Retrieves the image associated with resource from the image registry. If
	 * the image cannot be retrieved, attempt to find and load the image at the
	 * location specified in resource.
	 * 
	 * @param resource
	 *            the image to retrieve
	 * @return Image the image associated with resource or null if one could not
	 *         be found
	 */
	public Image getImage(String resource) {
		Image image = getImageRegistry().get(resource);
		if (image == null) {
			// create an image
			image = createImage(resource);
		}
		return image;
	}

	/**
	 * Retrieves the image descriptor associated with resource from the image
	 * descriptor registry. If the image descriptor cannot be retrieved, attempt
	 * to find and load the image descriptor at the location specified in
	 * resource.
	 * 
	 * @param resource
	 *            the image descriptor to retrieve
	 * @return ImageDescriptor the image descriptor assocated with resource or
	 *         the default "missing" image descriptor if one could not be found
	 */
	public ImageDescriptor getImageDescriptor(String resource) {
		ImageDescriptor imageDescriptor = null;
		Object o = getImageDescriptorRegistry().get(resource);
		if (o == null) {
			// create a descriptor
			imageDescriptor = createImageDescriptor(resource);
		} else {
			imageDescriptor = (ImageDescriptor) o;
		}
		return imageDescriptor;
	}

	/**
	 * Returns the image descriptor registry for this plugin.
	 * 
	 * @return HashMap - image descriptor registry for this plugin
	 */
	@SuppressWarnings("unchecked")
	private HashMap getImageDescriptorRegistry() {
		if (fImageDescRegistry == null) {
			fImageDescRegistry = new HashMap();
		}
		return fImageDescRegistry;
	}

	/**
	 * Returns the image registry for this plugin.
	 * 
	 * @return ImageRegistry - image registry for this plugin
	 */
	private ImageRegistry getImageRegistry() {
		return JFaceResources.getImageRegistry();
	}
}
