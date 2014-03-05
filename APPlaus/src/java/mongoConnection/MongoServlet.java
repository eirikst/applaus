package mongoConnection;

import com.mongodb.*;
import com.mongodb.util.JSON;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import static java.lang.Integer.parseInt;
import java.util.Date;
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
    private final static Logger LOGGER = Logger.getLogger
        (MongoServlet.class.getName());
    private static MongoClient mongo;
    private static DB db;
    private HomeManager homeMan;
    private AuthenticationManager authMan;
    private AssignmentManager assignMan;
    private ContestManager contMan;
    private UserManager userMan;
    private AdminManager adminMan;
    
    @Override
    public void init() throws ServletException {
        homeMan = new HomeManager();
        authMan = new AuthenticationManager();
        assignMan = new AssignmentManager();
        contMan = new ContestManager();
        userMan = new UserManager();
        adminMan = new AdminManager();
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
        request.getSession().setMaxInactiveInterval(604800);
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
                out.println(contMan.getActiveContests(db));
                response.setStatus(200);//success
            }
            
            //getInactiveContests
            else if(action.equals("getInactiveContests")) {
                try {
                    int skip = Integer.parseInt(request.getParameter("skip"));
                    out.println(contMan.getInactiveContests(db, skip));
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
                    contMan.participate(db, request.getSession().
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
                    contMan.dontParticipate(db, request.getSession().
                            getAttribute("username").toString(),
                            request.getParameter("contestId"));
                    response.setStatus(200);//success
            }
            
            //userActiveContList
            else if(action.equals("userActiveContList")) {
                try {
                    out.println(contMan.userActiveContList(db, request.
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
            
            //create assignment
            else if(request.getParameter("action").equals("createAssignment")) {
                try{
                    out.println(assignMan.createAssignment(db, request.
                            getParameter("title").toString(), request.getParameter("desc").toString(),
                            parseInt(request.getParameter("points"))));
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
            
            
            // register assignment
            else if(request.getParameter("action").equals("registerAssignment")) {
                long date = Long.parseLong(request.getParameter("date_done"));
                try{
                    out.println(assignMan.registerAssignment(db, request.getSession().
                            getAttribute("username").toString(), request.
                            getParameter("id").toString(), new java.util.Date(date), request.getParameter("comment").toString()));
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
            
            //get all registered assignments from a user
            else if(request.getParameter("action").equals("getAllAssignmentsUser")) {
                try{
                    out.println(assignMan.getAllAssignmentsUser(db, 
                            request.getSession().getAttribute("username").toString()));
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
            
            //get assignments types
            else if(request.getParameter("action").equals("getAssignmentsTypes")) {
                try{
                    out.println(assignMan.getAssignmentsTypes(db));
                    response.setStatus(200);//success
                }catch(RuntimeException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("Exception mongodb. " + sw.toString());
                    response.sendError(500);//error
                }
            }
            
            else if(action.equals("getWeekGoal")) {
                try {
                    int[] goals = homeMan.getWeekGoals(db, request
                            .getSession().getAttribute("username").toString());
                    if(goals[0] <= -2 || goals[1] <= -2) {
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
                int[] points = homeMan.getHomePoints(db, (String)request.getSession().getAttribute("username"));
                out.print(JSON.serialize(points));
            }
            else if(action.equals("setGoal")) {
                String username = (String)request.getSession().getAttribute("username");
                try {
                    int goal = Integer.parseInt(request.getParameter("points"));
                    homeMan.setGoal(db, username, goal);
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
            
            else if(action.equals("createContest")) {
                String title = (String)request.getParameter("title");
                String desc = (String)request.getParameter("desc");
                String prize = (String)request.getParameter("prize");
                String username = (String)request.getSession().
                        getAttribute("username");
                try {
                    Long dateEndLong = Long.parseLong(request.
                            getParameter("dateEnd"));
                    Date dateEnd = new Date(dateEndLong);
                    int points = Integer.parseInt((String)request.
                            getParameter("points"));
                    
                    if(!contMan.createContest(db, title, desc, prize, dateEnd, 
                            points, username)) {
                        response.sendError(500);
                    }
                }
                catch(NumberFormatException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.info("Could not parse points or date to integer or "
                            + "long. "
                            + sw.toString());
                    response.sendError(500);//error
                }
            }
            
            // admin list
            else if(action.equals("getAdminList")) {
                out.println(userMan.getAdminList(mongo.getDB("applaus")));
            }
            
            // register user
            else if(action.equals("registerUser")) {
                out.println(userMan.registerUser(mongo.getDB("applaus"), request));
            }
            
            // register new password
            else if(action.equals("newPassword")) {
                out.println(userMan.newPassword(mongo.getDB("applaus"), request));
            }
            
            // set role for users
            else if(action.equals("setRole")) {
                out.println(userMan.setRole(mongo.getDB("applaus"), request));
            }
            
            // add idea
            else if(action.equals("addIdea")) {
                out.println(homeMan.addIdea(mongo.getDB("applaus"), request));
            }           
            
            // get ideas
            else if(action.equals("getIdeas")) {
                out.println(homeMan.getIdeas(mongo.getDB("applaus")));
            }   
            
            //login
            else if(action.equals("login")) {
                try {
                    //returning role id, -1 if bad details
                    out.println(JSON.serialize(new int[]{authMan.
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
            
            else if(action.equals("deleteContest")) {
                int deleted = contMan.deleteContest(db, request
                        .getParameter("contestId"));
                if(deleted == 1) {
                    response.setStatus(200);
                }
                else {
                    response.sendError(500);
                }
            }
            
            else if(action.equals("editContest")) {
                String contestId = (String)request.getParameter("contestId");
                String title = (String)request.getParameter("title");
                String desc = (String)request.getParameter("desc");
                String prize = (String)request.getParameter("prize");
                try {
                    Long dateEndLong = Long.parseLong(request.
                            getParameter("dateEnd"));
                    Date dateEnd = new Date(dateEndLong);
                    int points = Integer.parseInt((String)request.
                            getParameter("points"));
                    
                    boolean edit = contMan.editContest(db, contestId, title, desc, prize, 
                            dateEnd, points);
                    if(!edit) {
                        response.sendError(500);//error
                    }
                }
                catch(NumberFormatException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.info("Could not parse points or date to integer or "
                            + "long. "
                            + sw.toString());
                    response.sendError(500);//error
                }
            }
            
            // get news stories
            else if(action.equals("getNews")) {
                String username = request
                            .getSession().getAttribute("username").toString();
                int skip = 0;
                try {
                    skip = Integer.parseInt(request.getParameter("skip"));
                }
                catch(NumberFormatException e) {
                    LOGGER.warning("skip post value not an integer");
                }
                String returnStr = homeMan.getNews(db, username, skip);
                if(returnStr != null) {
                    out.println(returnStr);
                }
                else {
                    response.sendError(500);
                }
            }

            //add news story
            else if(action.equals("addNewsAll")) {
                String writer = request
                            .getSession().getAttribute("username").toString();
                String title = request.getParameter("title");
                String text = request.getParameter("text");
                if(adminMan.addNewsStoryForAll(db, title, text, writer)) {
                    response.setStatus(200);
                }
                else {
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
