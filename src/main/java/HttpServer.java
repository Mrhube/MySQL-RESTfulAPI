import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.http.HttpServlet;


public class HttpServer {

	public static void launch() {
		String contextPath = "";
		String docBase = new File(".").getAbsolutePath();

		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);

		try {
			Context ctx = tomcat.addContext(contextPath, docBase);

			// Add get servlet
			String servletName = "Servlet1";
			String urlPattern = "/go";
			HttpServlet servlet = new QueryServlet();
			tomcat.addServlet(contextPath, servletName, servlet);
			ctx.addServletMappingDecoded(urlPattern, servletName);

			// Add post servlet
			servletName = "Servlet2";
			urlPattern = "/submit";
			servlet = new SubmitServlet();
			tomcat.addServlet(contextPath, servletName, servlet);
			ctx.addServletMappingDecoded(urlPattern, servletName);

			// Start the server
			tomcat.start();
			tomcat.getServer().await();

		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
}