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
import java.rmi.*;
import java.sql.*;
import java.util.List;
import java.io.Serializable;
public interface Election extends Remote {
    public class Candidate implements Serializable{
        public int id;
        public String name;
    }
    public class ElectionResult implements Serializable{
        public int candidateId;
        public String name;
        public int votes;
    }
    public class Voter implements Serializable{
        public int id;
        public boolean hasVoted;
    }
    public List<Candidate> getCandidates() throws RemoteException, SQLException;
    public ElectionResult getResults(int id) throws RemoteException, SQLException;
    public boolean login(int voterID, String providedPassword) throws RemoteException, SQLException;
    public void logout(int voterID) throws RemoteException;
    public boolean canVote(int voterID) throws RemoteException,SQLException;
    public void vote(int voterID, int candidateID) throws RemoteException, SQLException;
} // Election