/*
 * The MIT License
 *
 * Copyright 2014 Vasilis Bankov, George Peppas, Maria Theodoraki.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package SecurityController;
import java.sql.*;
/**
 *
 * @author vasilis
 */
public class PasswordHandler {
    public static final void savePassword(int userID, String password) throws SQLException{
        String generatedSecuredPasswordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        try{
            String q = "UPDATE VOTER SET PASSWORD=? WHERE ID=?";
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
            PreparedStatement prepared = conn.prepareStatement(q);
            prepared.setString(1, generatedSecuredPasswordHash);
            prepared.setInt(2,userID);
            prepared.executeUpdate();
            prepared.close();
            conn.close();
        }catch (SQLException se){
            System.out.println("BOOM !! SQLException: Could not save hashed password.");
            se.printStackTrace();
        }
        
    }
    public static final boolean checkPassword(int userID, String providedPassword) throws SQLException{
        String userPassword = "";
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
        String q = "SELECT PASSWORD FROM VOTER WHERE ID = "+userID;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(q);
        while(rs.next()){
            userPassword = rs.getString("PASSWORD");
        }
        conn.close();
        stmt.close();
        rs.close();
        boolean matched = BCrypt.checkpw(providedPassword, userPassword);
        return matched;
    }
}
