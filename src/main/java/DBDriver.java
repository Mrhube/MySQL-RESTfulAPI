import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

class DBDriver {

		private static String url = "jdbc:mysql://localhost:3306/database";
		private static String user = "admin";
		private static String password = "admin";

		static String runQuery(String sql) {
			Connection conn = null;
			String result = "";
			try {
				conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				result = resultSetToJSON(rs).toString();
			} catch(SQLException e) {
				System.err.println(e.getMessage());
			} finally {
				try{
					if(conn != null)
						conn.close();
				}catch(SQLException ex){
					System.err.println(ex.getMessage());
				}
			}
			return result;
		}

		static void runUpdate(String sql) {
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(sql);
			} catch(SQLException e) {
				System.err.println(e.getMessage());
			} finally {
				try{
					if(conn != null)
						conn.close();
				}catch(SQLException ex){
					System.err.println(ex.getMessage());
				}
			}
		}

		private static JSONArray resultSetToJSON(ResultSet rs) {
			try {
				ResultSetMetaData rsmd  = rs.getMetaData();
				int colCount = rsmd.getColumnCount();
				List<Map<String,String>> lst = new ArrayList();
				while (rs.next()) {
					Map<String,String> dict = new HashMap();
					for (int i = 1; i <= colCount; i++) {
						String key = rsmd.getColumnName(i).toLowerCase();
						String val;
						if (rs.getObject(i) == null) {
							val = "";
						} else {
							val = rs.getObject(i).toString();
						}
						dict.put(key,val);
					}
					lst.add(dict);
				}
				return listToJSON(lst);
			} catch(SQLException e) {
				System.err.println(e.getMessage());
				return null;
			}
		}

		private static JSONArray listToJSON(List<Map<String,String>> lst) {
			List<JSONObject> jsonList = new ArrayList();
			for(Map<String, String> data : lst) {
				JSONObject obj = new JSONObject(data);
				jsonList.add(obj);
			}
			return new JSONArray(jsonList);
		}

		// TODO: sanitize input strings
		static void addEntries(String json) {
			JSONArray jsonArray = new JSONArray(json);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				String date = obj.get("transactiondate").toString();
				String amount = obj.get("amount").toString();
				String category = obj.get("category").toString();
				String description = obj.get("description").toString();
				String payee = obj.get("payee").toString();
				String sql = "INSERT INTO transactions (TransactionDate, Amount, Payee, Category, Description) VALUES (";
				sql += "'" + date + "', ";
				sql += "'" + amount + "', ";
				sql += "'" + payee + "', ";
				sql += "'" + category + "', ";
				sql += "'" + description + "');";
				runUpdate(sql);
			}
		}
	}



