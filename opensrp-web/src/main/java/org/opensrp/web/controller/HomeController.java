package org.opensrp.web.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleCsvReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

import org.opensrp.core.entity.Role;
import org.opensrp.core.entity.User;
import org.opensrp.core.service.RoleService;
import org.opensrp.core.service.UserService;
import org.opensrp.web.util.OpensrpProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends OpensrpProperties {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleServiceImpl;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request, HttpSession session, Model model, Locale locale) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getPrincipal();
		List<String> roleName = new ArrayList<String>();
		Set<Role> roles = (Set<Role>) user.getRoles();
		for (Role role : roles) {
			roleName.add(role.getName());
		}
		System.err.println("start home: "+user.getUsername()+":  "+System.currentTimeMillis() );

		String targetUrl = dashboardUrl;
		if (roleName.contains("admin")) {
			targetUrl = dashboardUrl;
		} else if (roleName.contains("AM")) {
			targetUrl = "/user/sk-list.html";
		}

		System.err.println("end home: "+user.getUsername()+":  "+System.currentTimeMillis() );
		model.addAttribute("locale", locale);
		return new ModelAndView("redirect:" + targetUrl);
		
	}
	
	@RequestMapping(value = "helloReport1", method = RequestMethod.GET)
	  @ResponseBody
	  public void getRpt1(HttpServletResponse response) throws JarException, IOException, JRException {
	    InputStream jasperStream = this.getClass().getResourceAsStream("/jsreport/report.jrxml");
	    //BufferedInputStream jasperStream = new BufferedInputStream( new FileInputStream("/opt/multimedia/jasper/report.jrxml"));
	    Map<String,Object> params = new HashMap<>();
	    params.put("noy", new Integer(2020));
	    
	    params.put("Title", "Employees working more than "+ 2020 + " Years");
	    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

	    response.setContentType("application/x-pdf");
	    response.setHeader("Content-disposition", "inline; filename=helloWorldReport.pdf");

	    final OutputStream outStream = response.getOutputStream();
	    JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	  }
	
	@RequestMapping(value = "report", method = RequestMethod.GET)
	public void report(HttpServletResponse response) throws Exception {
		List<Role> roles = roleServiceImpl.findAll("Role");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		for (Role role : roles) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", role.getName());
			item.put("id", role.getId());
			result.add(item);
			
		}
		//InputStream logo = this.getClass().getResourceAsStream("logo-surjerhashi.png");
		BufferedImage logo = ImageIO.read(getClass().getResource("/logo-surjerhashi.png"));
		
		Map<String,Object> params = new HashMap<>();
	    params.put("logo", logo);
		response.setContentType("text/html");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);
		InputStream inputStream = this.getClass().getResourceAsStream("/jsreport/report.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
		exporter.exportReport();
	}
	
	@RequestMapping(value = "pdf", method = RequestMethod.GET)
	public void pdf(HttpServletResponse response) throws Exception {
		List<Role> roles = roleServiceImpl.findAll("Role");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		for (Role role : roles) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", role.getName());
			item.put("id", role.getId());
			result.add(item);
			
		}
		
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);
		InputStream inputStream = this.getClass().getResourceAsStream("/jsreport/report.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
		response.setContentType("application/x-pdf");
	    response.setHeader("Content-disposition", "inline; filename=helloWorldReport.pdf");

	    final OutputStream outStream = response.getOutputStream();
	    JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	   
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "csv", method = RequestMethod.GET)
	public void xml(HttpServletResponse response) throws Exception {
		List<Role> roles = roleServiceImpl.findAll("Role");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		for (Role role : roles) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", role.getName());
			item.put("id", role.getId());
			result.add(item);
			
		}
		
		String filename = "role";
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);
		InputStream inputStream = this.getClass().getResourceAsStream("/jsreport/report.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
		 response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		    response.setHeader("Content-Disposition", "attachment;filename=Agregaty.xlsx");
		    response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

		    JRCsvExporter exporter = new JRCsvExporter();
		    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

		    exporter.setExporterOutput(new SimpleWriterExporterOutput(response.getOutputStream()));

		    SimpleCsvReportConfiguration configuration = new SimpleCsvReportConfiguration();
		    exporter.setConfiguration(configuration);
		    exporter.exportReport();
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "xlx", method = RequestMethod.GET)
	public void excel(HttpServletResponse response) throws Exception {
		List<Role> roles = roleServiceImpl.findAll("Role");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		for (Role role : roles) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", role.getName());
			item.put("id", role.getId());
			result.add(item);
			
		}
		
		String filename = "role";
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);
		InputStream inputStream = this.getClass().getResourceAsStream("/jsreport/report.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
		response.setHeader("Content-Disposition", "attachment;filename"+filename+".xlsx");
        response.setContentType("application/octet-stream");
      
        final OutputStream outStream = response.getOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filename+".xlsx");
        exporter.exportReport();
	}
}
