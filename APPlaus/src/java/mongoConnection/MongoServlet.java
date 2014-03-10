package mongoConnection;

import com.mongodb.*;
import com.mongodb.util.JSON;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import org.bson.types.ObjectId;

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
    private IdeaManager ideaMan;
    
    @Override
    public void init() throws ServletException {
        homeMan = new HomeManager();
        authMan = new AuthenticationManager();
        assignMan = new AssignmentManager();
        contMan = new ContestManager();
        ideaMan = new IdeaManager();
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
                LOGGER.log(Level.INFO, "Bad request. POST action is NULL.");
                response.sendError(400);//bad request
            }
            
            //first comes methods accessible to anybody:
            
            //login
            else if(action.equals("login")) {
                String username = request.getParameter("usr");
                String password = request.getParameter("pwd");
                if(username == null || password == null) {
                    response.sendError(400);//bad request
                    return;
                }
                
                int role = authMan.login(db, username, password, request);
                if(role == -2 || role == -3 || role == -4) {
                    response.sendError(500);//internal error
                    return;
                } 

                //returning role id if okay, -1 if bad details
                out.println(JSON.serialize(new int[]{role}));

                if(role == 1 || role == 2 || role == 3) {
                    Cookie cookie = new Cookie("role", "" + role);
                    cookie.setMaxAge(24*60*60);
                    response.addCookie(cookie); 

                    request.getSession().setMaxInactiveInterval(604800);
                }
                response.setStatus(200);//success
                return;
            }
            
            // new password
            else if(action.equals("newPassword")) {
                String email = request.getParameter("email");
                if(email == null) {
                    response.sendError(400);//bad request
                    return;
                }
                
                int ok = authMan.newPassword(db, email);
                if(ok == 1) {
                    out.print(1);//ok email, all good
                    response.setStatus(200);//success
                    return;
                }
                else if(ok == 0) {
                    out.print(0);//wrong email
                    response.setStatus(200);//success
                    return;
                }
                else if(ok == -1 || ok == -2) {
                    response.sendError(500);//internal error
                    return;
                }
            }
            
            
            // register user
            else if(action.equals("registerUser")) {
                String username = request.getParameter("usr");
                String password = request.getParameter("pwd");
                String pwdRepeat = request.getParameter("pwdRepeat");
                String firstname = request.getParameter("fname");
                String lastname = request.getParameter("lname");
                String email = request.getParameter("email");
                
                if(username == null && password == null && pwdRepeat == null ||
                        firstname == null || lastname == null || email == null) 
                {
                    response.sendError(400);//bad request
                    return;
                }
                
                int res = authMan.registerUser(db, 
                        username, password, pwdRepeat, firstname, lastname,
                        email);
                if(res == 1 || res == -1 || res == -2 || res == -3) {
                    out.println(res);
                    response.setStatus(200);//success
                    return;
                }
                else {
                    response.sendError(500);//internal error
                    return;
                }
            }
            
            
            ////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////
            
            /*init username and role id before access restricted methods*/
            String username;
            int roleId;
            try {
                Object usernameObj = request.getSession().getAttribute("username");
                Object roleIdObj = request.getSession().getAttribute("role");
                if(usernameObj == null || roleIdObj == null) {
                    System.out.println("null, access denied");
                    response.sendError(401);//access denied
                    return;
                }
                username = (String)usernameObj;
                roleId = (Integer)roleIdObj;
            }
            catch(IllegalStateException e) {
                response.sendError(401);//access denied
                return;
            }
            catch(NumberFormatException e) {
                response.sendError(500);//internal error, roleId not parseable
                return;
            }
            /*end init*/
            
            
            ////////////////////////////////////////////////////////////////////
            
            //following method only accessible to super admin(roleid=1):
            
            ////////////////////////////////////////////////////////////////////

            
            // admin list
            if(action.equals("getAdminList")) {
                if(!isSuperAdmin(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String responseStr = authMan.getAdminList(db);
                if(responseStr == null) {
                    response.sendError(500);//internal error
                    return;
                }
                else {
                    out.print(responseStr);
                    response.setStatus(200);//success
                    return;
                }
            }

            // set role for users
            else if(action.equals("setRole")) {
                if(!isSuperAdmin(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String userToSet = request.getParameter("username");
                String roleToSetStr = request.getParameter("role");
                if(userToSet == null || roleToSetStr == null) {
                    LOGGER.warning("Some post variable is null.");
                    response.sendError(400);//bad request
                    return;
                }
                else try {
                    int roleToSet = Integer.parseInt(roleToSetStr);
                    String responseStr = authMan.setRole(db, userToSet, roleToSet);
                    if(responseStr == null) {
                        response.sendError(500);//internal error
                        return;
                    }
                    else {
                        out.print(responseStr);
                        response.setStatus(200);//success
                        return;
                    }
                }
                catch(NumberFormatException e) {
                    LOGGER.log(Level.INFO, "roleId not parseable to int", e);
                    response.sendError(400);//bad request
                    return;
                }
            }
            
            
            ////////////////////////////////////////////////////////////////////
            
            //admin features, not accessible if role id is not 1 or 2.
            
            ////////////////////////////////////////////////////////////////////


            //create assignment
            else if(action.equals("createAssignment")) {
                if(!isAdmin(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String title = request.getParameter("title");
                String desc = request.getParameter("desc");
                String pointsStr = request.getParameter("points");
                if(title == null || desc == null || pointsStr ==null) {
                    LOGGER.warning("Some post variable is null.");
                    response.sendError(400);//bad request
                    return;
                }

                try{
                    int points = Integer.parseInt(pointsStr);
                    String responseStr = assignMan.createAssignment(db, title, desc, points);
                    if(responseStr == null) {
                        response.sendError(500);//internal error
                        return;
                    }
                    else {
                        out.print(responseStr);
                        response.setStatus(200);//success
                        return;
                    }
                }
                catch(NumberFormatException e) {
                    LOGGER.log(Level.INFO, "points not parseable to integer.");
                    response.setStatus(400);//bad request
                    return;
                }
            }

            //create contest
            else if(action.equals("createContest")) {
                if(!isAdmin(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String title = request.getParameter("title");
                String desc = request.getParameter("desc");
                String prize = request.getParameter("prize");
                String dateEndStr = request.getParameter("dateEnd");
                String pointsStr = request.getParameter("points");
                if(title == null || desc == null || prize ==null || 
                        dateEndStr == null || pointsStr == null) {
                    LOGGER.warning("Some post variable is null.");
                    response.sendError(400);//bad request
                    return;
                }

                try {
                    Long dateEndLong = Long.parseLong(dateEndStr);
                    Date dateEnd = new Date(dateEndLong);
                    int points = Integer.parseInt(pointsStr);

                    ObjectId oid = contMan.createContest(db, title, desc, prize,
                            dateEnd, points, username);
                    if(oid != null) {
                        out.print(JSON.serialize(oid));
                        response.setStatus(200);//success
                        return;
                    }
                    else {
                        response.sendError(500);//internal error
                        return;
                    }
                }
                catch(NumberFormatException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.info("Could not parse points or date to integer or "
                            + "long. " + e);
                    response.sendError(400);//error
                    return;
                }
            }   

            //delete contest
            else if(action.equals("deleteContest")) {
                if(!isAdmin(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String contestId = request.getParameter("contestId");
                if(contestId == null) {
                    LOGGER.info("contestId post variable null.");
                    response.sendError(400);//bad request
                    return;
                }
                int deleted = contMan.deleteContest(db, contestId);
                if(deleted == 1) {
                    response.setStatus(200);//success
                    return;
                }
                else {
                    response.sendError(500);//internal error
                    return;
                }
            }

            //edit contest
            else if(action.equals("editContest")) {
                if(!isAdmin(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String id = request.getParameter("id");
                String title = request.getParameter("title");
                String desc = request.getParameter("desc");
                String prize = request.getParameter("prize");
                String dateEndStr = request.getParameter("dateEnd");
                String pointsStr = request.getParameter("points");
                if(id == null || title == null || desc == null || prize == null
                        || dateEndStr == null || pointsStr == null) {
                    LOGGER.warning("Some post variable is null.");
                    response.sendError(400);//bad request
                    return;
                }

                try {
                    Long dateEndLong = Long.parseLong(dateEndStr);
                    Date dateEnd = new Date(dateEndLong);
                    int points = Integer.parseInt(pointsStr);

                    boolean ok = contMan.editContest(db, id, title, desc,
                            prize, dateEnd, points);
                    if(ok) {
                        response.setStatus(200);//succes
                        return;
                    }
                    else {
                        response.sendError(500);//internal error
                        return;
                    }
                }
                catch(NumberFormatException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.info("Could not parse points or date to integer or "
                            + "long. " + e);
                    response.sendError(400);//error
                    return;
                }
            }

            //add news story
            else if(action.equals("addNewsAll")) {
                if(!isAdmin(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String title = request.getParameter("title");
                String text = request.getParameter("text");
                if(title == null || text == null) {
                    LOGGER.warning("Some post variable is null.");
                    response.sendError(400);//bad request
                    return;
                }
                String responseStr = homeMan.addNewsStoryForAll(db, title, text
                        , username);
                if(responseStr != null) {
                    out.print(responseStr);
                    response.setStatus(200);//success
                    return;
                }
                else {
                    response.sendError(500);//internal error
                    return;
                }
            }
            
            
            ////////////////////////////////////////////////////////////////////
            
            //following code can only be accessed by a logged in user
            
            ////////////////////////////////////////////////////////////////////

            
            //getActiveContests
            else if(action.equals("getActiveContests")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String responseStr = contMan.getActiveContests(db);
                if(responseStr == null) {
                    response.sendError(500);//internal error
                    return;
                }
                else {
                    out.println(responseStr);
                    response.setStatus(200);//success
                    return;
                }
            }

            //getInactiveContests
            else if(action.equals("getInactiveContests")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String skipStr = request.getParameter("skip");
                if(skipStr == null) {
                    LOGGER.log(Level.INFO, "skip value null");
                    response.sendError(400);//bad request
                    return;
                }
                try {
                    int skip = Integer.parseInt(skipStr);
                    String responseStr = contMan.getInactiveContests(db, skip);
                    if(responseStr == null) {
                        response.sendError(500);//internal error
                        return;
                    }
                    out.println(responseStr);
                    response.setStatus(200);//success
                    return;
                }
                catch(NumberFormatException e) {
                    LOGGER.log(Level.INFO, "skip not parseable to int", e);
                    response.sendError(400);//bad request
                    return;
                }
            }

            //participate
            else if(action.equals("participate")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String contestId = request.getParameter("contestId");
                System.out.println(contestId);
                if(contestId == null) {
                    LOGGER.log(Level.INFO, "contestId value null.");
                    response.sendError(400);//bad request
                    return;
                }
                int ok = contMan.participate(db, username,
                        contestId);
                if(ok == 1) {
                    response.setStatus(200);//success
                    return;
                }
                else {
                    response.setStatus(500);//internal error
                    return;
                }
            }

            //userActiveContList
            else if(action.equals("userActiveContList")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String responseStr = contMan.userActiveContList(db, username);
                if(responseStr != null) {
                    out.print(responseStr);
                    response.setStatus(200);//success
                    return;
                }
                else {
                    response.setStatus(500);//internal error
                    return;
                }
            }

            //dontParticipate
            else if(action.equals("dontParticipate")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String contestId = request.getParameter("contestId");
                if(contestId == null) {
                    LOGGER.log(Level.INFO, "contestId value null.");
                    response.sendError(400);//bad request
                    return;
                }
                int ok = contMan.dontParticipate(db, username,
                        contestId);
                if(ok == 1) {
                    response.setStatus(200);//success
                    return;
                }
                else {
                    response.setStatus(500);//internal error
                    return;
                }
            }

            // register assignment
            else if(action.equals("registerAssignment")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String dateStr = request.getParameter("date_done");
                String id = request.getParameter("id");
                String comment = request.getParameter("comment");

                if(dateStr == null || id == null || comment == null) {
                    LOGGER.log(Level.INFO, "One of the parameters are null.");
                    response.setStatus(400);//bad request
                    return;
                }
                    try {
                        long dateL = Long.parseLong(dateStr);//str to long
                        Date date = new Date(dateL);//long to Date

                        int ok = assignMan.registerAssignment(db, username, id, 
                                date, comment);
                        System.out.println(ok);
                        if(ok == 1 || ok == -1) {
                            response.setStatus(200);//success
                            out.print(ok);
                            return;
                        }
                        else {
                            response.setStatus(500);//internal error
                            return;
                        }
                    }
                    catch(NumberFormatException e) {
                        LOGGER.log(Level.INFO, "date not long.");
                        response.setStatus(400);//bad request
                        return;
                    }
            }

            //get all registered assignments from a user
            else if(action.equals("getAllAssignmentsUserSorted")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String skipStr = request.getParameter("skip");
                if(skipStr == null) {
                    LOGGER.log(Level.INFO, "Skip parameter is null.");
                    response.setStatus(400);//bad request
                    return;
                }
                try {
                    int skip = Integer.parseInt(skipStr);
                    String toReturn = assignMan.getAllAssignmentsUserSorted(db, username
                    , skip);
                    if(toReturn != null) {
                        out.println(toReturn);
                        response.setStatus(200);//success
                        return;
                    }
                    else {
                        response.sendError(500);//internal error
                        return;
                    }
                }
                catch(NumberFormatException e) {
                    LOGGER.log(Level.INFO, "Skip parameter not an integer.");
                    response.sendError(400);
                    return;
                }
            }

            //get assignments types
            else if(action.equals("getAssignmentsTypes")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String responseStr = assignMan.getAssignmentsTypes(db);
                if(responseStr == null) {
                    response.sendError(400);//bad request
                    return;
                }
                else {
                    out.println(responseStr);
                    response.setStatus(200);//success
                    return;
                }
            }

            // get week goal
            else if(action.equals("getWeekGoal")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                int[] goals = homeMan.getWeekGoals(db, username);
                if(goals == null) {
                    response.sendError(500);//internal error
                    return;
                }
                if(goals[0] <= -2 || goals[1] <= -2) {
                    response.sendError(500);//Internal error
                    return;
                }
                else {
                    out.print(JSON.serialize(goals));
                    response.setStatus(200);//success
                    return;
                }
            }

            //set week goal
            else if(action.equals("setGoal")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String goalStr = request.getParameter("points");
                if(goalStr == null) {
                    LOGGER.log(Level.INFO, "Points variable null.");
                    response.sendError(400);//bad request
                    return;
                }
                try {
                    int goal = Integer.parseInt(goalStr);
                    boolean ok = homeMan.setGoal(db, username, goal);
                    if(ok) {
                        response.setStatus(200);//success
                        return;
                    }
                    else {
                        response.sendError(500);//internal error
                        return;
                    }
                }
                catch(NumberFormatException e) {
                    LOGGER.log(Level.INFO, "Skip parameter not integer"
                            + " parseable.");
                    response.sendError(500);//internal error
                    return;
                }
            }

            else if(action.equals("getPointsHome")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                int[] points = homeMan.getHomePoints(db, username);
                if(points == null) {
                    response.sendError(500);//internal error
                    return;
                }
                else {
                    out.print(JSON.serialize(points));
                    response.setStatus(200);//success
                    return;
                }
            }

            // add idea
            else if(action.equals("addIdea")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String title = request.getParameter("title");
                String text = request.getParameter("text");
                if(title == null || text == null) {
                    LOGGER.log(Level.INFO, "One of the post variables are null"
                            + ".");
                    response.sendError(400);//bad request
                    return;
                }

                String responseStr = ideaMan.addIdea(db, title, text, username);
                if(responseStr != null) {
                    out.println(responseStr);
                    response.setStatus(200);//success
                    return;
                }
                else {
                    response.sendError(500);//internal error
                    return;
                }
            }           

            // get ideas
            else if(action.equals("getIdeas")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String skipStr = request.getParameter("skip");
                if(skipStr == null) {
                    LOGGER.log(Level.INFO, "Skip parameter is null.");
                    response.setStatus(400);//bad request
                    return;
                }
                try {
                    int skip = Integer.parseInt(skipStr);
                    String toReturn = ideaMan.getIdeas(db, skip);
                    if(toReturn != null) {
                        out.println(toReturn);
                        response.setStatus(200);//success
                        return;
                    }
                    else {
                        response.sendError(500);//internal error
                        return;
                    }
                }
                catch(NumberFormatException e) {
                    LOGGER.log(Level.INFO, "Skip parameter not an integer.");
                    response.sendError(400);
                    return;
                }
            }   

            // get news stories
            else if(action.equals("getNews")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String skipStr = request.getParameter("skip");
                if(skipStr == null) {
                    LOGGER.warning("skip post value null");
                    response.sendError(400);//bad request
                    return;
                }
                try {
                    int skip = Integer.parseInt(skipStr);
                    String returnStr = homeMan.getNews(db, username, skip);
                    if(returnStr != null) {
                        out.println(returnStr);
                        response.setStatus(200);//success
                        return;
                    }
                    else {
                        response.sendError(500);//internal error
                        return;
                    }
                }
                catch(NumberFormatException e) {
                    LOGGER.warning("skip post value not an integer");
                    response.sendError(400);//bad request
                    return;
                }
            }
            
            else {
                //no match, bad request
                LOGGER.warning("Bad request. POST action specified doesn't match"
                        + " any actions.");
                response.sendError(400);//bad request
                return;
            }
        }
        
        catch(UnsupportedEncodingException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("Character encoding returned by getCharacter"
                            + "Encoding cannot be used. " + sw.toString());
                    response.sendError(500);//internal error
        }
        catch(IllegalStateException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("getOutputStream method has already been call"
                            + "ed for this response object. " + sw.toString());
                    response.sendError(500);//internal error
        }
        catch(IOException e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    LOGGER.severe("In/out-put error occurred. "
                            + sw.toString());
                    response.sendError(500);//internal error
        }
    }
    
    /**
     * Checks if a role id is of a user(1, 2, 3)
     * @param role int id
     * @return true if role is of a user, false if not
     */
    private boolean isUser(int role) {
        return (role == 1 || role == 2 || role == 3);
    }
    
    /**
     * Checks if a role id is of an admin(1, 2)
     * @param role int id
     * @return true if role is of a user, false if not
     */
    private boolean isAdmin(int role) {
        return (role == 1 || role == 2);
    }

    /**
     * Checks if a role id is of a super admin(1)
     * @param role int id
     * @return true if role is of a user, false if not
     */
    private boolean isSuperAdmin(int role) {
        return (role == 1);
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