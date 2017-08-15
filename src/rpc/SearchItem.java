package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
	private DBConnection conn = DBConnectionFactory.getDBConnection();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		
	    HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.setStatus(403);
            return;
        }

//		String userId = request.getParameter("user_id");
		String userId = session.getAttribute("user").toString();
		double latitude = Double.parseDouble(request.getParameter("lat"));
		double longitude = Double.parseDouble(request.getParameter("lon"));
		//Term can be empty or null
		String term = request.getParameter("term");
//      ExternalAPI externalAPI =ExternalAPIFactory.getExternalAPI();
		List<Item> items = conn.searchItems(userId, latitude, longitude, term);
//		List<Item> items = externalAPI.search(latitude, longitude, term);
		List<JSONObject> list = new ArrayList<>();
		try {
			for (Item item : items) {
				// Add a thin version of item object
				JSONObject obj = item.toJSONObject();
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray array = new JSONArray(list);
		RpcHelper.writeJsonArray(response, array);
		
//      JSONArray array = new JSONArray();
//      try {
//            array.put(new JSONObject().put("username", "abcd"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RpcHelper.writeJsonArray(response, array);
		
//		/*
//		 * TEST CONNECTION
//		 */
//		// Tells the browser that server is returning a response in a format of JSON
//		response.setContentType("application/json");
//		// Allow all viewers to view this response
//		response.addHeader("Access-Control-Allow-Origin", "*");
//		// Create a PrintWriter from response such  that we can add data to response
//		String username = "";
//		PrintWriter  out = response.getWriter();
//		if (request.getParameter("username") != null) {
//			username = request.getParameter("username"); // get the username sent from the client
//			out.print("Hello " + username);
//		}
//		out.flush();
//		out.close();
		
		/**
		 *  TEST return a HTML format
		 */
//		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//		out.println("<html><body>");
//		out.println("<h1>This is a HTML page</h1>");
//		out.println("</body></html>");
//		out.flush();
//		out.close();
		
		/**
		 *  TEST return a json as a response
		 */
//		response.setContentType("application/json");
//		response.addHeader("Access-Control-Allow-Origin", "*");
//		
//		String username = "";
//		if (request.getParameter("username") != null) {
//		    username = request.getParameter("username");
//		}
//		JSONObject obj = new JSONObject();
//		try {
//			obj.put("username", username);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		PrintWriter out = response.getWriter();
//		out.print(obj);
//		out.flush(); // Flush the output stream and send the data to the client side
//		out.close(); // Close this response
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
