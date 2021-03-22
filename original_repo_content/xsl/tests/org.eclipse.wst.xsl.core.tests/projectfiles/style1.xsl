<?xml version="1.0" encoding="UTF-8"?>
<!-- ******************************************************************************
 * Copyright (c) 2008 Chase Technology Ltd - http://www.chasetechnology.co.uk
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     Doug Satchwell (Chase Technology Ltd) - initial API and implementation
 *******************************************************************************  -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:import href="lib2.xsl" />
	<xsl:include href="lib1.xsl" />
	
	<xsl:template name="existsInStyle1Only">
		<xsl:param name="p1"></xsl:param>
		<literal>Hello World</literal>
	</xsl:template>

	<xsl:template name="existsInStyle1AndLib1"> <!-- ERROR: template existsInStyle1AndLib1 is included from lib1.xsl-->
		<xsl:param name="p1"></xsl:param>
		<literal>Hello World</literal>
	</xsl:template>
	
</xsl:stylesheet>