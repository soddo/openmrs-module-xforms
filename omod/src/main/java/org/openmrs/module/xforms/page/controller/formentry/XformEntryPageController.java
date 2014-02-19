package org.openmrs.module.xforms.page.controller.formentry;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Patient;
import org.openmrs.api.FormService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.emrapi.patient.PatientDomainWrapper;
import org.openmrs.module.xforms.XformConstants;
import org.openmrs.module.xforms.util.XformsUtil;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.InjectBeans;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class XformEntryPageController {
	
	public void controller(@RequestParam("patientId") Patient patient, UiUtils ui, UiSessionContext emrContext,
	                       PageModel model, @InjectBeans PatientDomainWrapper patientDomainWrapper,
	                       HttpServletRequest request) {
		
		patientDomainWrapper.setPatient(patient);
		
		SimpleObject appHomepageBreadcrumb = SimpleObject.create("label", ui.message("xforms.app.formentry.title"), "link",
		    ui.pageLink("coreapps", "findpatient/findPatient?app=xforms.formentry"));
		SimpleObject patientPageBreadcrumb = SimpleObject.create("label",
		    patient.getFamilyName() + ", " + patient.getGivenName(), "link", ui.thisUrlWithContextPath());
		
		model.addAttribute("patient", patientDomainWrapper);
		model.addAttribute("breadcrumbOverride", ui.toJson(Arrays.asList(appHomepageBreadcrumb, patientPageBreadcrumb)));
		
		addFormEntryValues(model, request);
	}
	
	private void addFormEntryValues(PageModel model, HttpServletRequest request) {
		
		if (request.getParameter("encounterId") == null) { //Must be new form
			Integer formId = Integer.parseInt(request.getParameter("formId"));
			model.addAttribute("formId", formId);
			model.addAttribute("patientId", Integer.parseInt(request.getParameter("patientId")));
			model.addAttribute("formName", ((FormService) Context.getService(FormService.class)).getForm(formId).getName());
			model.addAttribute("entityFormDefDownloadUrlSuffix",
			    "moduleServlet/xforms/xformDownload?target=xformentry&contentType=xml&");
			model.addAttribute("formDataUploadUrlSuffix", "module/xforms/xformDataUpload.form");
		} else { //editing existing form
			Integer encounterId = Integer.parseInt(request.getParameter("encounterId"));
			Encounter encounter = Context.getEncounterService().getEncounter(encounterId);
			Form form = encounter.getForm();
			model.addAttribute("formId", form.getFormId());
			model.addAttribute("patientId", encounter.getPatientId());
			model.addAttribute("formName", ((FormService) Context.getService(FormService.class)).getForm(form.getFormId())
			        .getName());
			model.addAttribute("entityFormDefDownloadUrlSuffix",
			    "moduleServlet/xforms/xformDownload?target=xformentry&contentType=xml&encounterId=" + encounterId + "&");
			model.addAttribute("formDataUploadUrlSuffix", "module/xforms/xformDataUpload.form?mode=edit");
		}
		
		String url = "patientDashboard.form?";
		if ("true".equals(request.getParameter("refappui"))) {
			url = "xforms/formentry/patient.page?";
		}
		model.addAttribute("afterSubmitUrlSuffix", url);
		model.addAttribute("afterCancelUrlSuffix", url);
		
		model.addAttribute(
		    XformConstants.FORM_DESIGNER_KEY_DATE_SUBMIT_FORMAT,
		    Context.getAdministrationService().getGlobalProperty(XformConstants.GLOBAL_PROP_KEY_DATE_SUBMIT_FORMAT,
		        XformConstants.DEFAULT_DATE_SUBMIT_FORMAT));
		model.addAttribute(
		    XformConstants.FORM_DESIGNER_KEY_DATE_DISPLAY_FORMAT,
		    Context.getAdministrationService().getGlobalProperty(XformConstants.GLOBAL_PROP_KEY_DATE_DISPLAY_FORMAT,
		        XformConstants.DEFAULT_DATE_DISPLAY_FORMAT));
		model.addAttribute(XformConstants.FORM_DESIGNER_KEY_DEFAULT_FONT_FAMILY, Context.getAdministrationService()
		        .getGlobalProperty(XformConstants.GLOBAL_PROP_KEY_DEFAULT_FONT_FAMILY, XformConstants.DEFAULT_FONT_FAMILY));
		model.addAttribute(XformConstants.FORM_DESIGNER_KEY_DEFAULT_FONT_SIZE, Context.getAdministrationService()
		        .getGlobalProperty(XformConstants.GLOBAL_PROP_KEY_DEFAULT_FONT_SIZE, XformConstants.DEFAULT_FONT_SIZE));
		
		String color = "#8FABC7";
		String theme = Context.getAdministrationService().getGlobalProperty("default_theme", "legacy");
		if ("orange".equals(theme))
			color = "#f48a52";
		else if ("purple".equals(theme))
			color = "#8c87c5";
		else if ("green".equals(theme))
			color = "#1aac9b";
		
		model.addAttribute(XformConstants.FORM_DESIGNER_KEY_DEFAULT_GROUPBOX_HEADER_BG_COLOR, color);
		
		model.addAttribute(
		    XformConstants.FORM_DESIGNER_KEY_DATE_TIME_SUBMIT_FORMAT,
		    Context.getAdministrationService().getGlobalProperty(XformConstants.GLOBAL_PROP_KEY_DATE_TIME_SUBMIT_FORMAT,
		        XformConstants.DEFAULT_DATE_TIME_SUBMIT_FORMAT));
		model.addAttribute(
		    XformConstants.FORM_DESIGNER_KEY_DATE_TIME_DISPLAY_FORMAT,
		    Context.getAdministrationService().getGlobalProperty(XformConstants.GLOBAL_PROP_KEY_DATE_TIME_DISPLAY_FORMAT,
		        XformConstants.DEFAULT_DATE_TIME_DISPLAY_FORMAT));
		model.addAttribute(
		    XformConstants.FORM_DESIGNER_KEY_TIME_SUBMIT_FORMAT,
		    Context.getAdministrationService().getGlobalProperty(XformConstants.GLOBAL_PROP_KEY_TIME_SUBMIT_FORMAT,
		        XformConstants.DEFAULT_TIME_SUBMIT_FORMAT));
		model.addAttribute(
		    XformConstants.FORM_DESIGNER_KEY_TIME_DISPLAY_FORMAT,
		    Context.getAdministrationService().getGlobalProperty(XformConstants.GLOBAL_PROP_KEY_TIME_DISPLAY_FORMAT,
		        XformConstants.DEFAULT_TIME_DISPLAY_FORMAT));
		
		model.addAttribute(
		    XformConstants.FORM_DESIGNER_KEY_SHOW_SUBMIT_SUCCESS_MSG,
		    Context.getAdministrationService().getGlobalProperty(XformConstants.GLOBAL_PROP_KEY_SHOW_SUBMIT_SUCCESS_MSG,
		        XformConstants.DEFAULT_SHOW_SUBMIT_SUCCESS_MSG));
		
		model.addAttribute(XformConstants.FORM_DESIGNER_KEY_LOCALE_KEY, Context.getAdministrationService()
		        .getGlobalProperty(XformConstants.GLOBAL_PROP_KEY_LOCALE, Context.getLocale().getLanguage()));
		model.addAttribute(
		    XformConstants.FORM_DESIGNER_KEY_DECIMAL_SEPARATORS,
		    Context.getAdministrationService().getGlobalProperty(XformConstants.GLOBAL_PROP_KEY_DECIMAL_SEPARATORS,
		        XformConstants.DEFAULT_DECIMAL_SEPARATORS));
		model.addAttribute("usingJQuery", XformsUtil.usesJquery());
		model.addAttribute("locations", Context.getLocationService().getAllLocations(false));
		model.addAttribute("formatXml", "false");
		model.addAttribute("useOpenmrsMessageTag", XformsUtil.isOnePointNineOneAndAbove());
	}
}
