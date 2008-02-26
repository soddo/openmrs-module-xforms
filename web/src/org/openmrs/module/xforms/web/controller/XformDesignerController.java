package org.openmrs.module.xforms.web.controller;

//baseRelativePath: "scripts/dojo/",

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * This controller backs and saves XForm designs.
 * 
 * As per the current version, we deal with the following:
 * 
 * For the XForm:
 * 1 - Changing question properties like display text, readonly, required, disabled, visibility etc.
 * 2 - Order of questions.
 * 3 - Branching or Skipping logic.
 * 4 - 
 * 
 * For the XHTML as XSL transformed from the XForm:
 * 1 - Grouping of questions into various tabs. This avoids long scrollings between questions on a form.
 * 2 - 
 * 
 */
public class XformDesignerController extends SimpleFormController {
	
    /** Logger for this class and subclasses */
    protected final Log log = LogFactory.getLog(getClass());
        	    
    @Override
	protected Map referenceData(HttpServletRequest request, Object obj, Errors err) throws Exception {    	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("formId", request.getParameter("formId"));
        return map;
	}


	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException exceptions) throws Exception {						
    	return new ModelAndView(new RedirectView(getSuccessView()));
    }


    @Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception { 
    	return "Not Yet";
    }    
}
