
package rpc;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A helper class to handle rpc (remote parse control) related parsing logics.
 */
public class RpcHelper {
    // Parses a JSONObject from http request.
    public static JSONObject readJsonObject(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Writes a JSONObject to http response.
    public static void writeJsonObject(HttpServletResponse response, JSONObject obj) {
        try {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter(); // Create a PrintWriter from response such that we can add data to response.
            out.print(obj);
            out.flush(); // Flush the output stream and send the data to the client side.
            out.close(); // Close this response for good. 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Writes a JSONArray to http response.
    public static void writeJsonArray(HttpServletResponse response, JSONArray array) {
        try {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter(); // Create a PrintWriter from response such that we can add data to response.
            out.print(array);
            out.flush(); // Flush the output stream and send the data to the client side.
            out.close(); // Close this response for good. 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
