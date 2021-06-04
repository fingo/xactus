package org.eclipse.wst.xml.xpath2.processor.internal.function;

import java.util.Collection;

import org.eclipse.wst.xml.xpath2.api.ResultBuffer;
import org.eclipse.wst.xml.xpath2.api.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.DynamicError;
import org.eclipse.wst.xml.xpath2.processor.internal.types.QName;

public class XSQNameConstructor extends Function
{
    public static final String XS_Q_NAME = "QName";

    private final QName qname;

    public XSQNameConstructor( QName qname )
    {
        super( new QName( QName.XS_Q_NAME ), 1 );
        this.qname = qname;
    }

    @Override
	public ResultSequence evaluate( Collection args ) throws DynamicError
    {
		return ResultBuffer.wrap( this.qname );
    }

    public QName getQname()
    {
        return this.qname;
    }
}
