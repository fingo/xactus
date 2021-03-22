/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jsp.core.internal.java;

/**
 * @author pavery
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.compiler.IProblem;

class JSPProblemRequestor implements IProblemRequestor {

    private boolean fIsActive = false;

    private boolean fIsRunning = false;

    private List fCollectedProblems;

    public void beginReporting() {

        fIsRunning = true;
        fCollectedProblems = new ArrayList();
    }

    public void acceptProblem(IProblem problem) {

        if (isActive()) {
        	synchronized (fCollectedProblems) {
        		fCollectedProblems.add(problem);
			}
        }
    }

    public void endReporting() {

        fIsRunning = false;
    }

    public boolean isActive() {

        return fIsActive && fCollectedProblems != null;
    }

    /**
     * Sets the active state of this problem requestor.
     * 
     * @param isActive
     *            the state of this problem requestor
     */
    public void setIsActive(boolean isActive) {

        if (fIsActive != isActive) {
            fIsActive = isActive;
            if (fIsActive)
                startCollectingProblems();
            else
                stopCollectingProblems();
        }
    }

    /**
     * Tells this annotation model to collect temporary problems from now on.
     */
    private void startCollectingProblems() {

        fCollectedProblems = new ArrayList();
    }

    /**
     * Tells this annotation model to no longer collect temporary problems.
     */
    private void stopCollectingProblems() {

        // do nothing
    }

    /**
     * @return the list of collected problems
     */
    public List getCollectedProblems() {

        return fCollectedProblems;
    }

    public boolean isRunning() {

        return fIsRunning;
    }
}
