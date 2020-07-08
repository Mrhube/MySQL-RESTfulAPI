import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QueryServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("GET endpoint hit!");
		ServletOutputStream out = resp.getOutputStream();
		String json = DBDriver.runQuery("SELECT TransactionDate, Amount, Payee, Category, Description FROM transactions");
		out.write(json.getBytes());
		out.flush();
		out.close();
	}
}