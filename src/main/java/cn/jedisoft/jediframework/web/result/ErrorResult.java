package cn.jedisoft.jediframework.web.result;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

/**
 * 返回错误的调用输出
 * 
 * @author azhi
 *
 */
public class ErrorResult extends DefaultResult {

	public ErrorResult(Exception e) {
		super(WebResult.TYPE_ERROR);

		StringWriter sw = new StringWriter();  
        PrintWriter w = new PrintWriter(sw); 

        if(e instanceof InvocationTargetException) {
        	((InvocationTargetException)e).getTargetException().printStackTrace(w);

        } else {
        	e.printStackTrace(w);
        }

		super.setBody(sw.toString());

		w.close();
	}

}
