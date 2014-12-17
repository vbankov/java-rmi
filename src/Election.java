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
public interface Election extends Remote { // The Election interface. It has to extend the java.rmi.Remote package
    // Note: DTOs have to implement the Serializable interface
    /**
     *  Data Transfer Object (DTO) for Candidate. It contains the ID and name of a Candidate.
     */
        public class Candidate implements Serializable{     // DTOs need to be Serializable to pass through RMI
        public int id;
        public String name;
    }

    /**
     *  DTO for ElectionResult. It contains all available data of a Candidate
     */
    public class ElectionResult implements Serializable{
        public int candidateId;
        public String name;
        public int votes;
    }
    /**
     *  DTO for Voter. It contains the ID and the hasVoted status of a Voter
     */
    public class Voter implements Serializable{
        public int id;
        public boolean hasVoted;
    }

    /*
     *  RMI functions
     */
    
    /**
     * @description     Request a list with the ID's and names of the Candidates. 
     *                  The number of current votes is not returned here, it is part of the ElectionResult DTO's
     * @return          List of Candidates DTOs
     * @throws          RemoteException, SQLException
     */
    public List<Candidate> getCandidates() throws RemoteException, SQLException;
    
    /**
     * @desc        Request the full data from the `Candidate` table for a given ID.
     *              The difference with the previous function is that this DTO also has the number of votes.
     * @param id    Candidate ID
     * @return      ElectionResult DTO
     * @throws      RemoteException, SQLException
     */
    public ElectionResult getResults(int id) throws RemoteException, SQLException;
    
    /**
     * @desc    Log-in to server
     * @param   voterID
     * @param   providedPassword
     * @return  True on success, False on fail
     * @throws  RemoteException
     * @throws  SQLException
     */
    public boolean login(int voterID, String providedPassword) throws RemoteException, SQLException;

    /**
     * @desc    Log a user out 
     * @param   voterID
     * @throws  RemoteException
     */
    public void logout(int voterID) throws RemoteException;

    /**
     * @desc    Check if a user has a right to vote.
     * @param   voterID
     * @return  True if `VOTER.HASVOTED`=0, False otherwise
     * @throws  RemoteException
     * @throws  SQLException
     */
    public boolean canVote(int voterID) throws RemoteException,SQLException;

    /**
     * @desc    Cast a vote for a candidate.
     *          To ensure data integrity a synchronized query is executed and everything is rolled-back in case of any problem.
     * @param   voterID
     * @param   candidateID
     * @return  True if vote was casted
     * @throws  RemoteException
     * @throws  SQLException
     */
    public boolean vote(int voterID, int candidateID) throws RemoteException, SQLException;
} // Election