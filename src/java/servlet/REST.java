package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Dennis
 */
@WebServlet(name = "REST", urlPatterns = {"/api/quote/*"})
public class REST extends HttpServlet {

    private Map<Integer, String> quotes = new HashMap() {
        {
            put(1, "Friends are kisses blown to us by angels");
            put(2, "Do not take life too seriously. You will never get out of it alive");
            put(3, "Behind every great man, is a woman rolling her eyes");
        }
    };

    public String getParameter(HttpServletRequest request) {
        String[] parts = request.getRequestURI().split("/");
        String parameter = null;
        if (parts.length == 5) {
            parameter = parts[4];
        }
        return parameter;
    }

    protected void makeResponse(String responseString, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(responseString);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idString = getParameter(request);
        int id = Integer.parseInt(idString);
        String quote = quotes.get(id);
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("quote", quote);

        makeResponse(new Gson().toJson(jsonObj), response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Scanner jsonScanner = new Scanner(request.getInputStream());
        String json = "";
        while (jsonScanner.hasNext()) {
            json += jsonScanner.nextLine();
        }
        
//        Get the size of Map to ensure the id is correct
        int nextId = quotes.size();
        
//        Get the quote text from the provided Json
        JsonObject newQuote = new JsonParser().parse(json).getAsJsonObject();
        String quote = newQuote.get("quote").getAsString();
        quotes.put(nextId++, quote);
        
//        This is to be used to ensure that the quote is stored in the map
        String quoteString = quotes.get(nextId);
        
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", nextId);
        jsonObj.addProperty("quote", quote);

        makeResponse(new Gson().toJson(jsonObj), response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
