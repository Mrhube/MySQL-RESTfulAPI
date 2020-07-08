import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class SubmitServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("POST endpoint hit!");
		BufferedReader reader = req.getReader();
		DBDriver.addEntries(reader.readLine());
		ServletOutputStream out = resp.getOutputStream();
		out.write("Success!".getBytes());
		out.flush();
		out.close();
	}
}