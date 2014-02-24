package mongoConnection;

import com.mongodb.*;
import com.mongodb.util.JSON;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author eirikstadheim
 */
@WebServlet(name = "MongoConnection", urlPatterns = {"/MongoConnection"})
public class MongoServlet extends HttpServlet {
    private MongoClient mongo;
    private DB db;
    private final static Logger LOGGER = Logger.getLogger
        (MongoServlet.class.getName());
    
    @Override
    public void init() throws ServletException {
        try {
            mongo = new MongoClient( "localhost" , 27017 );
             db = mongo.getDB("applaus");
        }
        catch(java.net.UnknownHostException e) {
            LOGGER.severe("Database host cannot be resolved. + e");
        }
        catch(MongoException e) {
            LOGGER.severe(e.toString());
        }
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        
        //printwriter
        
        
        try (PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");
            if(action == null) {
                LOGGER.severe("Bad request. POST action is NULL.");
                response.sendError(400);
                return;
            }
            
            //getActiveContests
            if(action.equals("getActiveContests")) {
                out.println(ContestManager.getActiveContests(db));
                response.setStatus(200);//success
            }
            
            //getInactiveContests
            else if(action.equals("getInactiveContests")) {
                try {
                    int skip = Integer.parseInt(request.getParameter("skip"));
                    out.println(ContestManager.getInactiveContests(db, skip));
                    response.setStatus(200);//success
                }
                catch(NumberFormatException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("Could not parse skip to an integer. " + sw.toString());
                    response.sendError(400);//error
                }
            }
            
            //participate
            else if(action.equals("participate")) {
                try {
                    ContestManager.participate(db, request.getSession().
                            getAttribute("username").toString(),
                            request.getParameter("contestId"));
                    response.setStatus(200);//success
                }
                catch(RuntimeException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("Exception mongo. " + sw.toString());
                    response.sendError(500);
                }
            }
            
            //dontParticipate
            else if(action.equals("dontParticipate")) {
                    ContestManager.dontParticipate(db, request.getSession().
                            getAttribute("username").toString(),
                            request.getParameter("contestId"));
                    response.setStatus(200);//success
            }
            
            //userActiveContList
            else if(action.equals("userActiveContList")) {
                try {
                    out.println(ContestManager.userActiveContList(db, request.
                            getSession().getAttribute("username").toString()));
                    response.setStatus(200);//success
                }
                catch(RuntimeException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("Exception mongodb. " + sw.toString());
                    response.sendError(500);//error
                }
            }
            
            //assignment
            else if(request.getParameter("action").equals("getAllAssignments")) {
                out.println(AssignmentManager.getAllAssignmentsUser(db, "audunsto")); //!! Skift til username !!//
            }
            else if(request.getParameter("action").equals("getAssignments")) {
                out.println(AssignmentManager.getAssignments(db));
            }  
            
            else if(action.equals("getWeekGoal")) {
                try {
                    int[] goals = HomeManager.getWeekGoals(db, request
                            .getSession().getAttribute("username").toString());
                    if(goals[0] < 0 || goals[1] < 0) {
                        response.sendError(500);
                    }
                    else {
                        out.print(JSON.serialize(goals));
                    }
                }
                catch(NumberFormatException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("Exception parsing double. " + sw.toString());
                    response.sendError(500);//error
                }
                catch(RuntimeException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("Exception mongodb. " + sw.toString());
                    response.sendError(500);//error
                }
            }
            else if(action.equals("getPointsHome")) {
                int[] points = HomeManager.getHomePoints(db, (String)request.getSession().getAttribute("username"));
                out.print(JSON.serialize(points));
            }
            else if(action.equals("setGoal")) {
                String username = (String)request.getSession().getAttribute("username");
                try {
                    int goal = Integer.parseInt(request.getParameter("points"));
                    HomeManager.setGoal(db, username, goal);
                }
                catch(NumberFormatException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.info("Could not parse goal points to integer. "
                            + sw.toString());
                    response.sendError(500);//error
                }
            }
            
            //login
            else if(action.equals("login")) {
                try {
                    //returning role id, -1 if bad details
                    out.println(JSON.serialize(new int[]{AuthenticationManager.
                            login(db, request)}));
                }
                //Does not throw com.mongodb.MongoException as the doc says 
                //it should
                catch(NumberFormatException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("Role could not be parsed as integer. " + sw.toString());
                    response.sendError(500);
                }
                catch(NullPointerException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("Role null. " + sw.toString());
                    response.sendError(500);
                }
                catch(RuntimeException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("Exception mongodb. " + sw.toString());
                    response.sendError(500);
                }
            }
            
            //no match, bad request
            else {
                LOGGER.warning("Bad request. POST action specified doesn't match"
                        + " any actions.");
                response.sendError(400);
            }
        }
        catch(UnsupportedEncodingException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("Character encoding returned by getCharacter"
                            + "Encoding cannot be used. " + sw.toString());
        }
        catch(IllegalStateException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("getOutputStream method has already been call"
                            + "ed for this response object. " + sw.toString());
        }
        catch(IOException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("In/out-put error occurred. "
                            + sw.toString());
        }
    }

    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for connecting to the mongodb of APPlaus and handling"
                + "session data.";
    }// </editor-fold>
 
    /**
     * Closes mongo client destroy
     */
   @Override
    public void destroy() {
        mongo.close();
    }
}
