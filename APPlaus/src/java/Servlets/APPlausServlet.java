package Servlets;

import APPlausException.InputException;
import DAO.DatabaseConnection;
import DAO.MongoDbImpl.AssignmentQueriesImpl;
import DAO.MongoDbImpl.ContestQueriesImpl;
import DAO.MongoDbImpl.IdeaQueriesImpl;
import DAO.MongoDbImpl.MongoConnectionImpl;
import DAO.MongoDbImpl.NewsQueriesImpl;
import DAO.MongoDbImpl.SectionQueriesImpl;
import DAO.MongoDbImpl.UserQueriesImpl;
import DbManager.AssignmentManager;
import DbManager.AuthenticationManager;
import DbManager.ContestManager;
import DbManager.HomeManager;
import DbManager.IdeaManager;
import DbManager.MongoDbImpl.AssignmentManagerImpl;
import DbManager.MongoDbImpl.AuthenticationManagerImpl;
import DbManager.MongoDbImpl.ContestManagerImpl;
import DbManager.MongoDbImpl.HomeManagerImpl;
import DbManager.MongoDbImpl.IdeaManagerImpl;
import DbManager.MongoDbImpl.StatisticsManagerImpl;
import DbManager.StatisticsManager;
import Tools.DateTools;
import com.mongodb.*;
import com.mongodb.util.JSON;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author eirikstadheim
 */
@WebServlet(name = "APPlausServlet", urlPatterns = {"/APPlausServlet"})
public class APPlausServlet extends HttpServlet {
    private final static Logger LOGGER = Logger.getLogger
        (APPlausServlet.class.getName());
    private HomeManager homeMan;
    private AuthenticationManager authMan;
    private AssignmentManager assignMan;
    private ContestManager contMan;
    private IdeaManager ideaMan;
    private StatisticsManager statsMan;
    
    @Override
    public void init() throws ServletException {
        try {
            homeMan = new HomeManagerImpl(UserQueriesImpl.getInstance(), 
                    AssignmentQueriesImpl.getInstance(), 
                    NewsQueriesImpl.getInstance(), 
                    IdeaQueriesImpl.getInstance(),
                    ContestQueriesImpl.getInstance());
            authMan = new AuthenticationManagerImpl(UserQueriesImpl.
                    getInstance(), SectionQueriesImpl.
                    getInstance());
            assignMan = new AssignmentManagerImpl(AssignmentQueriesImpl.
                    getInstance(), UserQueriesImpl.getInstance());
            contMan = new ContestManagerImpl(ContestQueriesImpl.getInstance(), 
                    UserQueriesImpl.getInstance());
            ideaMan = new IdeaManagerImpl(IdeaQueriesImpl.getInstance());
            statsMan = new StatisticsManagerImpl(SectionQueriesImpl.
                    getInstance(), UserQueriesImpl.getInstance(), ContestQueriesImpl.
                    getInstance(), IdeaQueriesImpl.getInstance(), homeMan);
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
        request.setCharacterEncoding("UTF-8");
        //printwriter
        
        
        try (PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");
            System.out.println(action);
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
                
                int role = authMan.login(username, password);
                if(role == -2 || role == -3 || role == -4) {
                    response.sendError(500);//internal error
                    return;
                } 

                //returning role id if okay, -1 if bad details
                out.println(role);

                if(role == 1 || role == 2 || role == 3) {
                    setCookie(response, role, username);
                    setSession(request, username, role);
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
                
                int ok = authMan.newPassword(email);
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
                String sectionId = request.getParameter("sectionId");
                
                if(username == null && password == null && pwdRepeat == null ||
                        firstname == null || lastname == null || email == null 
                        || sectionId == null) 
                {
                    response.sendError(400);//bad request
                    return;
                }
                
                int res = authMan.registerUser(username, password, pwdRepeat, 
                        firstname, lastname, email, sectionId, null);//null 
                                                    //because no facebook user
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
            
            
            // get sections
            else if(action.equals("getSections")) {
                String retString = authMan.getSections();
                if(retString == null) {
                    response.sendError(500);//internal error
                    return;
                }
                else {
                    out.print(retString);
                    response.setStatus(200);
                    return;
                }
            }
            
            else if(action.equals("fbReg")) {
                String username = request.getParameter("usr");
                String sectionId = request.getParameter("sectionId");
                System.out.println("sectionid: " + sectionId);
                System.out.println("username: " + username);
                
                
                
                System.out.println("i fb-reg servlet");
                String code = request.getParameter("code");
                System.out.println(code);
                if (code == null || code.equals("")) {
                    // an error occurred, handle this
                    System.out.println("code er " + code);
                }

                String token = null;
                try {
                    String g = "https://graph.facebook.com/oauth/access_token?client_id=294235794072909&redirect_uri=" + URLEncoder.encode("http://localhost:8080/APPlaus/APPlausServlet?action=" + action + "&usr=" + username + "&sectionId=" + sectionId, "UTF-8") + "&client_secret=fce2ac5c6e666c307b6dd5095b40097f&code=" + code;
                    URL u = new URL(g);
                    URLConnection c = u.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    String inputLine;
                    StringBuffer b = new StringBuffer();
                    while ((inputLine = in.readLine()) != null)
                        b.append(inputLine + "\n");            
                    in.close();
                    token = b.toString();
                    if (token.startsWith("{"))
                        throw new Exception("error on requesting token: " + token + " with code: " + code);
                } catch (Exception e) {
                        // an error occurred, handle this
                    System.out.println("Exception l46");
                    System.out.println(e);
                    response.sendError(500);
                    return;
                }

                String graph = null;
                try {
                    String g = "https://graph.facebook.com/me?" + token;
                    URL u = new URL(g);
                    URLConnection c = u.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    String inputLine;
                    StringBuffer b = new StringBuffer();
                    while ((inputLine = in.readLine()) != null)
                        b.append(inputLine + "\n");            
                    in.close();
                    graph = b.toString();
                } catch (Exception e) {
                        // an error occurred, handle this
                    System.out.println("Exception l65");
                    response.sendError(500);
                    return;
                }

                String facebookId;
                String firstName;
                String lastName;
                String email;
                try {
                    JSONObject json = new JSONObject(graph);
                    facebookId = json.getString("id");
                    firstName = json.getString("first_name");
                    lastName = json.getString("last_name");
                    email = json.getString("email");
                } catch (JSONException e) {
                    // an error occurred, handle this
                    System.out.println("Exception l87");
                    response.sendError(500);
                    return;
                }
                System.out.println("okupd: " + authMan.registerUser(username, 
                        null, null, firstName, lastName, email, sectionId, facebookId));
                response.sendRedirect("index.jsp");
                return;
            }
            
            // register user
            else if(action.equals("registerUser")) {
                String username = request.getParameter("usr");
                String password = request.getParameter("pwd");
                String pwdRepeat = request.getParameter("pwdRepeat");
                String firstname = request.getParameter("fname");
                String lastname = request.getParameter("lname");
                String email = request.getParameter("email");
                String sectionId = request.getParameter("sectionId");
                
                if(username == null && password == null && pwdRepeat == null ||
                        firstname == null || lastname == null || email == null 
                        || sectionId == null) 
                {
                    response.sendError(400);//bad request
                    return;
                }
                
                int res = authMan.registerUser(username, password, pwdRepeat, 
                        firstname, lastname, email, sectionId, null);//null 
                                                    //because no facebook user
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
            
            
            // get sections
            else if(action.equals("getSections")) {
                String retString = authMan.getSections();
                if(retString == null) {
                    response.sendError(500);//internal error
                    return;
                }
                else {
                    out.print(retString);
                    response.setStatus(200);
                    return;
                }
            }
            
            else if(action.equals("fbLogin")) {
                request.getSession().invalidate();
                String code = request.getParameter("code");
                System.out.println(code);
                if (code == null || code.equals("")) {
                    // an error occurred, handle this
                    System.out.println("code er " + code);
                }

                String token = null;
                try {
                    String g = "https://graph.facebook.com/oauth/access_token?client_id=294235794072909&redirect_uri=" + URLEncoder.encode("http://localhost:8080/APPlaus/APPlausServlet?action=" + action, "UTF-8") + "&client_secret=fce2ac5c6e666c307b6dd5095b40097f&code=" + code;
                    URL u = new URL(g);
                    URLConnection c = u.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    String inputLine;
                    StringBuffer b = new StringBuffer();
                    while ((inputLine = in.readLine()) != null)
                        b.append(inputLine + "\n");            
                    in.close();
                    token = b.toString();
                    if (token.startsWith("{"))
                        throw new Exception("error on requesting token: " + token + " with code: " + code);
                } catch (Exception e) {
                        // an error occurred, handle this
                    System.out.println("Exception l46");
                    System.out.println(e);
                    response.sendError(500);
                    return;
                }

                String graph = null;
                try {
                    String g = "https://graph.facebook.com/me?" + token;
                    URL u = new URL(g);
                    URLConnection c = u.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    String inputLine;
                    StringBuffer b = new StringBuffer();
                    while ((inputLine = in.readLine()) != null)
                        b.append(inputLine + "\n");            
                    in.close();
                    graph = b.toString();
                } catch (Exception e) {
                        // an error occurred, handle this
                    System.out.println("Exception l65");
                    response.sendError(500);
                    return;
                }

                String facebookId;
                String firstName;
                String lastName;
                String email;
                try {
                    JSONObject json = new JSONObject(graph);
                    facebookId = json.getString("id");
                    firstName = json.getString("first_name");
                    lastName = json.getString("last_name");
                    email = json.getString("email");
                } catch (JSONException e) {
                    // an error occurred, handle this
                    System.out.println("Exception l87");
                    response.sendError(500);
                    return;
                }
                Map userInfo = authMan.getFbUserInfo(facebookId);
                System.out.println(userInfo);
                if(userInfo != null) {
                    setSession(request, (String)userInfo.get("username"), (Integer)userInfo.get("role_id"));
                    setCookie(response, (Integer)userInfo.get("role_id"), (String)userInfo.get("username"));
                    response.sendRedirect("index.jsp");
                }
                else {
                    response.sendRedirect("login.jsp");
                }
                return;
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
                String responseStr = authMan.getAdminList();
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
                    String responseStr = authMan.setRole(userToSet, roleToSet);
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
            
            else if(action.equals("editAssignmentType")) {
                String assignId = (String)request.getParameter("assignId");
                String title = (String)request.getParameter("title");
                int points = parseInt(request.getParameter("points"));
                String desc = (String)request.getParameter("desc");
                boolean active = parseBoolean(request.getParameter("active"));
                try {
                    
                    boolean edit = assignMan.editAssignmentType(assignId, title, points, desc, active);
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
            
            //delete assignment
            else if(action.equals("deleteAssignmentType")) {
                int deleted = assignMan.deleteAssignmentType(request
                        .getParameter("assignId"));
                if(deleted == 1) {
                    response.setStatus(200);
                }
                else {
                    response.sendError(500);
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
                    String responseStr = assignMan.createAssignment(title, desc, points);
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

                    String returnStr = contMan.createContest(title, desc, prize,
                            dateEnd, points, username);
                    if(returnStr != null) {
                        out.print(returnStr);
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
                    response.sendError(400);//bad request
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
                int deleted = contMan.deleteContest(contestId);
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
                    response.sendError(401);//no access
                    return;
                }
                String id = request.getParameter("contestId");
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

                    boolean ok = contMan.editContest(id, title, desc,
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
                    response.sendError(400);//bad request
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
                String responseStr = homeMan.addNewsStoryForAll(title, text
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
            
            // declare a winner of a contest
            else if(action.equals("declareWinner")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String contestId = request.getParameter("contestId");
                String winner = request.getParameter("username");
                if(contestId == null) {
                    LOGGER.log(Level.INFO, "contestId parameter is null.");
                    response.setStatus(400);//bad request
                    return;
                }
                boolean toReturn = contMan.declareWinner(contestId, winner);
                try{
                    boolean achievement = statsMan.contestWinnerAchievement(username);
                } catch (InputException e) {
                    LOGGER.info("Could not read username " + e);
                }
                if(toReturn) {
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
                String responseStr = contMan.getActiveContests();
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
                    String responseStr = contMan.getInactiveContests(skip);
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
                int ok = contMan.participate(username,
                        contestId);
                try{
                    boolean achievement = statsMan.participateAchievement(username);
                }catch(InputException e) {
                    LOGGER.log(Level.INFO, "Input bad.");
                    response.setStatus(400);//bad request
                    return;
                }
                if(ok == 1) {
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
                int ok = contMan.dontParticipate(username,
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

                        int ok = assignMan.registerAssignment(username, id, 
                                date, comment);
                        System.out.println(ok);
                        boolean achievement = statsMan.regAssignmentAchievement(username);
                        
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
                    catch(InputException e) {
                        LOGGER.log(Level.INFO, "Input bad.");
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
                    String toReturn = assignMan.getAllAssignmentsUserSorted(username
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
                String responseStr = assignMan.getAssignmentsTypes();
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
            
            //delete assignment
            else if(action.equals("deleteAssignment")) {
                int deleted = assignMan.deleteAssignment(request
                        .getParameter("assignId"), username);
                if(deleted == 1) {
                    response.setStatus(200);
                }
                else {
                    response.sendError(500);
                }
            }
            
            // edit assignment
            else if(action.equals("editAssignment")) {
                String assignId = (String)request.getParameter("assignId");
                String comment = (String)request.getParameter("comment");
                try {
                    Long dateDoneLong = Long.parseLong(request.
                            getParameter("date_done"));
                    Date date_done = new Date(dateDoneLong);
                    
                    boolean edit = assignMan.editAssignment(assignId, comment, date_done, username);
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

            // get week goal
            else if(action.equals("getWeekGoal")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                int[] goals = homeMan.getWeekGoals(username);
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
                    boolean ok = homeMan.setGoal(username, goal);
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
            
            //get points home
            else if(action.equals("getPointsHome")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                int[] points = homeMan.getHomePoints(username);
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
            
            // get stats home
            else if(action.equals("getHomeStats")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String periodString = request.getParameter("period");
                if(periodString == null) {
                    LOGGER.log(Level.INFO, "period value null");
                    response.sendError(400);//bad request
                    return;
                }
                int period = -1;
                try {
                    period = Integer.parseInt(periodString);
                    String returnObj = statsMan.getPointsStats(username, period);
                    if(returnObj == null) {//period not 
                        response.sendError(400);//bad request
                        return;
                    }
                    else {
                        out.print(returnObj);
                        response.setStatus(200);//success
                        return;
                    }
                }
                catch(NumberFormatException e) {
                    LOGGER.log(Level.INFO, "Period parameter not an integer.");
                    response.sendError(400);
                    return;
                }
            }
            
            // get total points separated
            else if(action.equals("getTotalPointsSeparated")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String pointsSeparated = homeMan.getPointsSeperate(username, 
                        DateTools.FOREVER);
                if(pointsSeparated == null) {
                    response.sendError(500);//internal error
                    return;
                }
                else {
                    out.print(pointsSeparated);
                    response.setStatus(200);//success
                    return;
                }
            }
            
            // get info about comment and likes
            else if(action.equals("getCommentLikeStats")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String info = ideaMan.getCommentsAndLikeInfo(username);
                if(info == null) {
                    response.sendError(500);//internal error
                    return;
                }
                else {
                    out.print(info);
                    response.setStatus(200);//success
                    return;
                }
            }
            
            // get stats about sections
            else if(action.equals("getSectionStats")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//unauthorized
                    return;
                }
                String periodStr = request.getParameter("period");
                if(periodStr == null) {
                    response.sendError(400);//bad request
                    return;
                }
                try {
                    int period = Integer.parseInt(periodStr);
                    String sectionInfo = statsMan.getSectionStats(period);
                    if(sectionInfo == null) {
                        response.sendError(500);//internal error
                        return;
                    }
                    out.print(sectionInfo);
                    return;
                }
                catch(NumberFormatException e) {
                    LOGGER.log(Level.INFO, "Period parameter not an integer.");
                    response.sendError(400);
                    return;
                }
            }
            
            // get top five
            else if(action.equals("getTopFive")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String periodString = request.getParameter("period");
                if(periodString == null) {
                    LOGGER.log(Level.INFO, "period value null");
                    response.sendError(400);//bad request
                    return;
                }
                int period = -1;
                try {
                    period = Integer.parseInt(periodString);
                    String returnObj = statsMan.getTopFive(period);
                    if(returnObj == null) {//period not 
                        response.sendError(400);//bad request
                        return;
                    }
                    else {
                        out.print(returnObj);
                        response.setStatus(200);//success
                        return;
                    }
                }
                catch(NumberFormatException e) {
                    LOGGER.log(Level.INFO, "Period parameter not an integer.");
                    response.sendError(400);
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

                String responseStr = ideaMan.addIdea(title, text, username);
                
                try {
                    boolean achievement = statsMan.writeIdeaAchievement(username);
                }catch(InputException e) {
                    LOGGER.log(Level.INFO, "Skip parameter not an integer.");
                    response.sendError(400);
                    return;
                }   
                
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
                    String toReturn = ideaMan.getIdeas(skip);
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

            // add comment on idea
            else if(action.equals("addIdeaComment")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String ideaId = request.getParameter("ideaId");
                String text = request.getParameter("text");
                if(ideaId == null || text == null) {
                    LOGGER.log(Level.INFO, "One of the post variables are null"
                            + ".");
                    response.sendError(400);//bad request
                    return;
                }

                String responseStr = ideaMan.addComment(ideaId, username, text);
                
                try{
                    boolean achievement = statsMan.commentIdeaAchievement(request.getParameter("writer"));
                }catch (InputException e){
                    LOGGER.info("Username is null.");
                }
                
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

            
            //delete idea
            else if(action.equals("deleteIdea")) {
                String ideaId = request.getParameter("ideaId");
                if(ideaId == null) {
                    LOGGER.info("ideaId post variable null.");
                    response.sendError(400);//bad request
                    return;
                }
                int deleted = ideaMan.deleteIdea(ideaId, username);
                if(deleted == 1) {
                    response.setStatus(200);//success
                    return;
                }
                else {
                    response.sendError(500);//internal error
                    return;
                }
            }
            
            //like/not like idea
            else if(action.equals("likeIdea")) {
                String ideaId = request.getParameter("ideaId");
                String like = request.getParameter("like");
                if(ideaId == null || like == null) {
                    LOGGER.info("ideaId and/or like post variable null.");
                    response.sendError(400);//bad request
                    return;
                }
                boolean likeBool;
                if(like.equals("1")) {
                    likeBool = true;
                }
                else if(like.equals("0")) {
                    likeBool = false;
                }
                else {
                    LOGGER.info("like post variable not 0 or 1.");
                    response.sendError(400);//bad request
                    return;
                }
                
                try{
                    boolean achievement = statsMan.likeIdeaAchievement(request.getParameter("writer"));
                }catch (InputException e){
                    LOGGER.info("Username is null.");
                }
                
                if(ideaMan.likeIdea(ideaId, username, likeBool)) {
                    response.setStatus(200);//success
                    return;
                }
                else {
                    response.sendError(500);//internal error
                    return;
                }
            }
            
            //like/not like comment
            else if(action.equals("likeComment")) {
                String commentId = request.getParameter("commentId");
                String like = request.getParameter("like");
                if(commentId == null || like == null) {
                    LOGGER.info("commentId and/or like post variable null.");
                    response.sendError(400);//bad request
                    return;
                }
                boolean likeBool;
                if(like.equals("1")) {
                    likeBool = true;
                }
                else if(like.equals("0")) {
                    likeBool = false;
                }
                else {
                    LOGGER.info("like post variable not 0 or 1.");
                    response.sendError(400);//bad request
                    return;
                }
                
                if(ideaMan.likeComment(commentId, username, likeBool)) {
                    response.setStatus(200);//success
                    return;
                }
                else {
                    response.sendError(500);//internal error
                    return;
                }
            }
            
            //delete comment
            else if(action.equals("deleteComment")) {
                String commentId = request.getParameter("commentId");
                String ideaId = request.getParameter("ideaId");
                if(commentId == null || ideaId == null) {
                    LOGGER.info("commentId and/or ideaId post variable null.");
                    response.sendError(400);//bad request
                    return;
                }
                if(ideaMan.deleteComment(ideaId, commentId, username)) {
                    response.setStatus(200);//success
                    return;
                }
                else {
                    response.sendError(500);//internal error
                    return;
                }
            }
   
           else if(action.equals("deleteNews")) {
               String newsId = request.getParameter("newsId");
               if(newsId == null) {
                   LOGGER.info("contestId post variable null.");
                   response.sendError(400);//bad request
                   return;
               }
               int deleted = homeMan.deleteNews(newsId);
               if(deleted == 1) {
                   response.setStatus(200);//success
                   return;
               }
               else {
                   response.sendError(500);//internal error
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
                    String returnStr = homeMan.getNews(username, skip);
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
            
            //get achievements
            else if(action.equals("getAchievements")) {
                if(!isUser(roleId)) {
                    response.sendError(401);//internal error
                    return;
                }
                String responseStr = statsMan.getAchievements(username);
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
    
    
    /**
     * Takes a HttpServletRequest, username and role and sets the request 
     * session attributes username and role to the given values.
     * @param request HttpServletRequest to add session to
     * @param username session username
     * @param role session role id
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    private void setSession(HttpServletRequest request, String username,
            int role) {
        try {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("role", role);
            session.setMaxInactiveInterval(604800);
        }
        catch(IllegalStateException e) {
            LOGGER.log(Level.INFO, "Trying to set attributes on invalidated"
                    + " session", e);
        }
    }
    
    /**
     * Sets a cookie with the role id of the user
     * @param response HttpServletResponse to set cookie on
     * @param role role id of user
     */
    private void setCookie(HttpServletResponse response, int role, String username) {
        Cookie cookieRole = new Cookie("role", "" + role);
        cookieRole.setMaxAge(24*60*60);
        response.addCookie(cookieRole);
        
        Cookie cookieUsr = new Cookie("username", "" + username);
        cookieUsr.setMaxAge(24*60*60);
        response.addCookie(cookieUsr);
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
        return "Servlet for connecting to the database of APPlaus and handling"
                + "session data.";
    }// </editor-fold>
 
    /**
     * Closes database connection on destroy
     */
   @Override
    public void destroy() {
        try {
            DatabaseConnection connection = MongoConnectionImpl.getInstance();
            connection.close();
        }
        catch(UnknownHostException e) {
            LOGGER.log(Level.SEVERE, "Error closing database connection.", e);
        }
    }
}