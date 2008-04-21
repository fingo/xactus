/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsd.ui.internal.adt.editor;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SelectionManager;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.xsd.ui.internal.adt.actions.BaseSelectionAction;
import org.eclipse.wst.xsd.ui.internal.adt.actions.SetInputToGraphView;
import org.eclipse.wst.xsd.ui.internal.adt.design.ADTFloatingToolbar;
import org.eclipse.wst.xsd.ui.internal.adt.design.DesignViewGraphicalViewer;
import org.eclipse.wst.xsd.ui.internal.adt.design.editparts.ADTEditPartFactory;
import org.eclipse.wst.xsd.ui.internal.adt.facade.IModel;
import org.eclipse.wst.xsd.ui.internal.adt.outline.ADTContentOutlinePage;
import org.eclipse.wst.xsd.ui.internal.adt.outline.ADTLabelProvider;
import org.eclipse.wst.xsd.ui.internal.adt.outline.ExtensibleContentOutlinePage;
import org.eclipse.wst.xsd.ui.internal.editor.XSDEditorPlugin;

public abstract class ADTMultiPageEditor extends CommonMultiPageEditor
{
  protected IModel model;
  private int currentPage = -1;
  protected Button tableOfContentsButton;
  protected ADTFloatingToolbar floatingToolbar;
  
  /**
   * Creates a multi-page editor example.
   */
  public ADTMultiPageEditor()
  {
    super();
  }

  
  private class InternalLayout extends StackLayout
  {
    public InternalLayout()
    {
      super();  
    }
    
    protected void layout(Composite composite, boolean flushCache)
    {
      Control children[] = composite.getChildren();
      Rectangle rect = composite.getClientArea();
      rect.x += marginWidth;
      rect.y += marginHeight;
      rect.width -= 2 * marginWidth;
      rect.height -= 2 * marginHeight;
      
      for (int i = 0; i < children.length; i++) 
      {
        if (i == 0)  // For the back to schema button 
        { 
          org.eclipse.draw2d.geometry.Rectangle r = ((GraphicalEditPart)floatingToolbar.getContents()).getFigure().getBounds();
          children[i].setBounds(rect.x + 10, rect.y + 10, r.width, Math.max(24, r.height));
        }
        else if (i == 1 && modeCombo != null) // For the drop down toolbar
        {
          children[i].setBounds(rect.x + rect.width - 90 - maxLength, rect.y + 10, maxLength + 60, 26);
        }
        else // For the main graph viewer
        {
          children[i].setBounds(rect);          
        }
      }       
    }               
  }
  
  protected Composite createGraphPageComposite()
  {    
    Composite parent = new Composite(getContainer(), SWT.FLAT);
    parent.setBackground(ColorConstants.white);
    
    parent.setLayout(new InternalLayout());

    floatingToolbar = new ADTFloatingToolbar(getModel());
    floatingToolbar.createControl(parent);
    floatingToolbar.getControl().setVisible(true);
    EditPartFactory editPartFactory = getEditorModeManager().getCurrentMode().getEditPartFactory();
    floatingToolbar.setEditPartFactory(editPartFactory);
    
    createViewModeToolbar(parent);
    
    return parent;
  }
  
  protected void createGraphPage()
  {
    super.createGraphPage(); 
//    toolbarViewer.getControl().moveAbove(graphicalViewer.getControl());
//    graphicalViewer.getControl().moveBelow(toolbarViewer.getControl());
  }
  
  public String getContributorId()
  {
    return "org.eclipse.wst.xsd.ui.internal.editor"; //$NON-NLS-1$
  }
  
  public IContentOutlinePage getContentOutlinePage()
  {
    if (fOutlinePage == null || fOutlinePage.getControl() == null || fOutlinePage.getControl().isDisposed())
    {
      final ProductCustomizationProvider productCustomizationProvider = (ProductCustomizationProvider)getAdapter(ProductCustomizationProvider.class);
      ExtensibleContentOutlinePage outlinePage = null;
      if (productCustomizationProvider != null)
      {
        outlinePage = productCustomizationProvider.getProductContentOutlinePage();
      }
      
      if (outlinePage == null)
      {
        outlinePage = new ADTContentOutlinePage();
      }
      outlinePage.setEditor(this);
      ITreeContentProvider provider = (ITreeContentProvider)getEditorModeManager().getCurrentMode().getOutlineProvider();
      outlinePage.setContentProvider(provider);
      ADTLabelProvider adtLabelProvider = new ADTLabelProvider();
      outlinePage.setLabelProvider(adtLabelProvider);
      outlinePage.setModel(getModel());
      fOutlinePage = outlinePage;
    }
    return fOutlinePage;
  }

  /**
   * Creates the pages of the multi-page editor.
   */
  protected void createPages()
  {
    selectionProvider = getSelectionManager();
    
    createGraphPage();
    createSourcePage();
    
    getEditorSite().setSelectionProvider(selectionProvider);

    model = buildModel();  // (IFileEditorInput)getEditorInput());
    
    initializeGraphicalViewer();
    floatingToolbar.setModel(model);
    
    int pageIndexToShow = getDefaultPageTypeIndex();
    setActivePage(pageIndexToShow);
  }

  protected int getDefaultPageTypeIndex() {
    int pageIndex = SOURCE_PAGE_INDEX;
    if (XSDEditorPlugin.getPlugin().getDefaultPage().equals(XSDEditorPlugin.DESIGN_PAGE)) {
        pageIndex = DESIGN_PAGE_INDEX;
    }

    return pageIndex;
  }
  
  /**
   * @see org.eclipse.ui.IWorkbenchPart#setFocus()
   */
  public void setFocus()
  {
    if (getActivePage() == DESIGN_PAGE_INDEX)
    {
      if (graphicalViewer != null && graphicalViewer.getControl() != null)
      {
        List selected = graphicalViewer.getSelectedEditParts();
        if (selected.size() > 0)
        {
          graphicalViewer.setFocus((EditPart)selected.get(0));
        }
        graphicalViewer.getControl().setFocus();
        
      }
    }
    else
    {
      graphicalViewer.setFocus(null);
    }
  }

  protected void pageChange(int newPageIndex)
  {
    currentPage = newPageIndex;
    super.pageChange(newPageIndex);
    setFocus();
  }
  
  private boolean isTableOfContentsApplicable(Object graphViewInput)
  {
    return !(graphViewInput instanceof IModel);
  }

  protected ScrollingGraphicalViewer getGraphicalViewer()
  {
    DesignViewGraphicalViewer viewer = new DesignViewGraphicalViewer(this, getSelectionManager());
    viewer.addInputChangdListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {        
        IStructuredSelection input = (IStructuredSelection)event.getSelection();
        floatingToolbar.refresh(isTableOfContentsApplicable(input.getFirstElement()));
      }      
    });
    // Workaround bug 227687 An edit part's focus state is not updated properly
    // Once this is bug is fixed, we can remove custom selection manager
    viewer.setSelectionManager(new CustomSelectionManager());
    return viewer;
  }

  // Workaround bug 227687 An edit part's focus state is not updated properly
  // Once this is bug is fixed, we can remove this class
  private class CustomSelectionManager extends SelectionManager
  {
    public void appendSelection(EditPart editpart)
    {
      if (editpart != getFocus())
        getViewer().setFocus(editpart);
      super.appendSelection(editpart);
    }
  }
  
  abstract public IModel buildModel();  // (IFileEditorInput editorInput);
  
  protected void createActions()
  {
    ActionRegistry registry = getActionRegistry();
    
    BaseSelectionAction action = new SetInputToGraphView(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
  }


  public IModel getModel()
  {
    return model;
  }

  public Object getAdapter(Class type)
  {
    if (type == ZoomManager.class)
      return graphicalViewer.getProperty(ZoomManager.class.toString());
    
    if (type == GraphicalViewer.class)
      return graphicalViewer;
    if (type == EditPart.class && graphicalViewer != null)
      return graphicalViewer.getRootEditPart();
    if (type == IFigure.class && graphicalViewer != null)
      return ((GraphicalEditPart) graphicalViewer.getRootEditPart()).getFigure();

    if (type == IContentOutlinePage.class)
    {
      return getContentOutlinePage();
    }

    return super.getAdapter(type);
  }

  protected EditPartFactory getEditPartFactory() {
    return new ADTEditPartFactory();
  }

  protected void initializeGraphicalViewer()
  {
    graphicalViewer.setContents(model);
  }
  
  public void dispose()
  {
    if (currentPage == SOURCE_PAGE_INDEX)
    {
      XSDEditorPlugin.getPlugin().setSourcePageAsDefault();
    }
    else
    {
      XSDEditorPlugin.getPlugin().setDesignPageAsDefault();
    }
    floatingToolbar = null;
    super.dispose();
  }
}
