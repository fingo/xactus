package org.eclipse.wst.jsdt.web.ui.internal.hyperlink;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.jsdt.web.ui.internal.JSPUIPlugin;
import org.eclipse.wst.jsdt.web.ui.internal.Logger;

/**
 * Hyperlink for taglib files in jars.
 */
class TaglibJarHyperlink implements IHyperlink {
	static class ZipStorage implements IStorage {
		File fFile = null;
		String fEntryName = null;

		ZipStorage(File file, String entryName) {
			fFile = file;
			fEntryName = entryName;
		}

		public InputStream getContents() throws CoreException {
			InputStream stream = null;
			try {
				ZipFile file = new ZipFile(fFile);
				ZipEntry entry = file.getEntry(fEntryName);
				stream = file.getInputStream(entry);
			} catch (Exception e) {
				throw new CoreException(new Status(IStatus.ERROR, JSPUIPlugin
						.getDefault().getBundle().getSymbolicName(),
						IStatus.ERROR, getFullPath().toString(), e));
			}
			return stream;
		}

		public IPath getFullPath() {
			return new Path(fFile.getAbsolutePath() + IPath.SEPARATOR
					+ fEntryName);
		}

		public String getName() {
			return fEntryName;
		}

		public boolean isReadOnly() {
			return true;
		}

		public Object getAdapter(Class adapter) {
			return null;
		}
	}

	private IRegion fRegion;
	private IPath fZipFilePath;

	public TaglibJarHyperlink(IRegion region, IPath zipFilePath) {
		fRegion = region;
		fZipFilePath = zipFilePath;
	}

	public IRegion getHyperlinkRegion() {
		return fRegion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.hyperlink.IHyperlink#getTypeLabel()
	 */
	public String getTypeLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.hyperlink.IHyperlink#getHyperlinkText()
	 */
	public String getHyperlinkText() {
		// TODO Auto-generated method stub
		return null;
	}

	public void open() {
		IEditorInput input = new URLFileHyperlink.StorageEditorInput(
				new ZipStorage(fZipFilePath.toFile(), "META-INF/taglib.tld")); //$NON-NLS-1$
		IEditorDescriptor descriptor;
		try {
			descriptor = IDE.getEditorDescriptor(input.getName());
			if (descriptor != null) {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IDE.openEditor(page, input, descriptor.getId(), true);
			}
		} catch (PartInitException e) {
			Logger.log(Logger.WARNING_DEBUG, e.getMessage(), e);
		}
	}
}
