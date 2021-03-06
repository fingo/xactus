package info.fingo.xactus.processor.internal.function;

import java.util.Collection;
import java.util.Iterator;

import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.QName;

public class OpConcatenate extends Function {

    public OpConcatenate() {
        super( new QName( "concatenate" ), 2 );
    }

    public ResultSequence evaluate(Collection args, info.fingo.xactus.api.EvaluationContext ec) throws DynamicError {
        return concatenate(args);
    }

    public static ResultSequence concatenate(Collection args) throws DynamicError {
        assert args.size() == 2;

        // get args
        Iterator citer = args.iterator();
        ResultSequence arg1 = (ResultSequence) citer.next();
        ResultSequence arg2 = (ResultSequence) citer.next();

        if (arg1.size() <= 1)
            return arg2;

        if (arg2.size() <= 1)
            return arg1;

        ResultBuffer rs = new ResultBuffer();
        rs.concat( arg1 );
        rs.concat( arg2 );
        return rs.getSequence();
    }
}
