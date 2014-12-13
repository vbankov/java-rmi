/*
 * The MIT License
 *
 * Copyright 2014 vasilis.
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
/**
 *
 * @author vasilis
 */
import java.rmi.*;
import java.net.*;
import java.sql.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ElectionClientUI extends javax.swing.JFrame {
    
    
    
    
    
    private static void printCandidates(Election election) throws SQLException, RemoteException{
        List<Election.Candidate> cands = null;
        cands = election.getCandidates();
        System.out.println("Got "+cands.size()+" candidates:");
        for (Election.Candidate cand : cands) {
            String t = "\t" + cand.id + "\t" + cand.name + "\t";
            System.out.println(t);
        }
    }
    public static Election initRmiElection() throws RemoteException, NotBoundException{
        System.setSecurityManager (new TheRMISecurityManager()); // Lookup a remote reference to a remote object of Calculator type 
        Registry registry = LocateRegistry.getRegistry("localhost", 18300);
        String name = "rmi://localhost/ElectionService";
        Election c = (Election) registry.lookup(name);
        return c;
    }
    private static int voterID;
    private final Election theElection;
    private boolean isSessionOpen = false;
    
    
    
    
    
    /**
     * Creates new form ElectionClientUI
     * @throws java.net.MalformedURLException
     * @throws java.rmi.NotBoundException
     * @throws java.rmi.RemoteException
     */
    public ElectionClientUI() throws MalformedURLException, NotBoundException, RemoteException {
        initComponents();
        // Hide all panels
        tabbedPane.setVisible(false);
        candidate1.setVisible(false);votes1.setVisible(false);
        candidate2.setVisible(false);votes2.setVisible(false);
        candidate3.setVisible(false);votes3.setVisible(false);
        candidate4.setVisible(false);votes4.setVisible(false);
        candidate5.setVisible(false);votes5.setVisible(false);
        // Initialize RMI Service
        this.theElection = initRmiElection();
        // Make a call-to-action
        statusLabel.setText("Status: Waiting for log-in.");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        userInput = new javax.swing.JTextField();
        logInOutBtn = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        passInput = new javax.swing.JPasswordField();
        tabbedPane = new javax.swing.JTabbedPane();
        resultsPanel = new javax.swing.JPanel();
        updateResultsBtn = new javax.swing.JButton();
        candidate1 = new javax.swing.JLabel();
        candidate2 = new javax.swing.JLabel();
        candidate3 = new javax.swing.JLabel();
        candidate4 = new javax.swing.JLabel();
        candidate5 = new javax.swing.JLabel();
        votes1 = new javax.swing.JLabel();
        votes2 = new javax.swing.JLabel();
        votes3 = new javax.swing.JLabel();
        votes4 = new javax.swing.JLabel();
        votes5 = new javax.swing.JLabel();
        votePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        voteComboBox = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 500, 100));

        userInput.setText("UserID");

        logInOutBtn.setText("Log-in");
        logInOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInOutBtnActionPerformed(evt);
            }
        });

        statusLabel.setText("Status: Connecting...");

        passInput.setText("password");

        updateResultsBtn.setText("Update");
        updateResultsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateResultsBtnActionPerformed(evt);
            }
        });

        candidate1.setText("candidate1");

        candidate2.setText("candidate2");

        candidate3.setText("candidate3");

        candidate4.setText("candidate4");

        candidate5.setText("candidate5");

        votes1.setText("votes1");

        votes2.setText("votes2");

        votes3.setText("votes3");

        votes4.setText("votes4");

        votes5.setText("votes5");

        javax.swing.GroupLayout resultsPanelLayout = new javax.swing.GroupLayout(resultsPanel);
        resultsPanel.setLayout(resultsPanelLayout);
        resultsPanelLayout.setHorizontalGroup(
            resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultsPanelLayout.createSequentialGroup()
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(updateResultsBtn))
                    .addGroup(resultsPanelLayout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(candidate2)
                            .addComponent(candidate1)
                            .addComponent(candidate3)
                            .addComponent(candidate4)
                            .addComponent(candidate5))
                        .addGap(130, 130, 130)
                        .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(votes2)
                            .addComponent(votes1)
                            .addComponent(votes3)
                            .addComponent(votes4)
                            .addComponent(votes5))))
                .addContainerGap(175, Short.MAX_VALUE))
        );
        resultsPanelLayout.setVerticalGroup(
            resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateResultsBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(candidate1)
                    .addComponent(votes1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(candidate2)
                    .addComponent(votes2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(candidate3)
                    .addComponent(votes3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(candidate4)
                    .addComponent(votes4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(candidate5)
                    .addComponent(votes5))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Results", resultsPanel);

        jLabel1.setText("Vote for Candidate #");

        jButton2.setText("Vote");

        javax.swing.GroupLayout votePanelLayout = new javax.swing.GroupLayout(votePanel);
        votePanel.setLayout(votePanelLayout);
        votePanelLayout.setHorizontalGroup(
            votePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(votePanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(voteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(205, Short.MAX_VALUE))
        );
        votePanelLayout.setVerticalGroup(
            votePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(votePanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(votePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(voteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(138, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Vote", votePanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusLabel)
                .addGap(44, 44, 44)
                .addComponent(userInput, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passInput, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logInOutBtn)
                .addContainerGap())
            .addComponent(tabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logInOutBtn)
                    .addComponent(userInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusLabel)
                    .addComponent(passInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPane)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logInOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logInOutBtnActionPerformed
        if(this.isSessionOpen){
            try {
                this.theElection.logout(ElectionClientUI.voterID);
                this.isSessionOpen = false;
                tabbedPane.setVisible(false);
                this.voterID = -1;
                userInput.setText("userID");
                userInput.setVisible(true);
                passInput.setVisible(true);
                logInOutBtn.setText("Log-in");
                statusLabel.setText("Status: Logged-out.");
            } catch (RemoteException ex) {
                Logger.getLogger(ElectionClientUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            statusLabel.setText("Status: Logging-in...");
            try {
                int userIn = Integer.parseInt(userInput.getText());
                String passIn = passInput.getText();
                boolean loggedIn = this.theElection.login(userIn,passIn);
                System.out.println("Log in with id "+userIn+" has status: "+loggedIn);
                if(loggedIn==true){
                    ElectionClientUI.voterID = userIn;
                    this.isSessionOpen = true;
                    tabbedPane.setVisible(true);
                    userInput.setVisible(false);
                    passInput.setVisible(false);
                    logInOutBtn.setText("Log-out");
                    statusLabel.setText("Status: Connected as user "+userIn);
                }else{
                    statusLabel.setText("Status: Login failed.");
                }
            } catch (RemoteException | SQLException ex) {
                statusLabel.setText("Something went wrong!");
                Logger.getLogger(ElectionClientUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException ex){
                statusLabel.setText("Invalid username");
            }
        }
        
    }//GEN-LAST:event_logInOutBtnActionPerformed

    private void updateResultsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateResultsBtnActionPerformed
        List<Election.ElectionResult> resultsList= new ArrayList<>();
        try {
            for(int i=1;i<6;i++){
                Election.ElectionResult er = null;
                er = this.theElection.getResults(i);
                resultsList.add(er);
                voteComboBox.addItem(er.name);
            } 
        // Print results
        candidate1.setText(resultsList.get(0).candidateId+". "+resultsList.get(0).name);
            votes1.setText(resultsList.get(0).votes+" Votes");
        candidate2.setText(resultsList.get(1).candidateId+". "+resultsList.get(1).name);
            votes2.setText(resultsList.get(1).votes+" Votes");
        candidate3.setText(resultsList.get(2).candidateId+". "+resultsList.get(2).name);
            votes3.setText(resultsList.get(2).votes+" Votes");        
        candidate4.setText(resultsList.get(3).candidateId+". "+resultsList.get(3).name);
            votes4.setText(resultsList.get(3).votes+" Votes");
        candidate5.setText(resultsList.get(4).candidateId+". "+resultsList.get(4).name);
            votes5.setText(resultsList.get(4).votes+" Votes");
        
        // Show labels
        candidate1.setVisible(true);votes1.setVisible(true);
        candidate2.setVisible(true);votes2.setVisible(true);
        candidate3.setVisible(true);votes3.setVisible(true);
        candidate4.setVisible(true);votes4.setVisible(true);
        candidate5.setVisible(true);votes5.setVisible(true);
        }catch (RemoteException | SQLException ex) {
            Logger.getLogger(ElectionClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_updateResultsBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc="Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ElectionClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ElectionClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ElectionClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ElectionClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ElectionClientUI().setVisible(true);
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    Logger.getLogger(ElectionClientUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel candidate1;
    private javax.swing.JLabel candidate2;
    private javax.swing.JLabel candidate3;
    private javax.swing.JLabel candidate4;
    private javax.swing.JLabel candidate5;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton logInOutBtn;
    private javax.swing.JPasswordField passInput;
    private javax.swing.JPanel resultsPanel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JButton updateResultsBtn;
    private javax.swing.JTextField userInput;
    private javax.swing.JComboBox voteComboBox;
    private javax.swing.JPanel votePanel;
    private javax.swing.JLabel votes1;
    private javax.swing.JLabel votes2;
    private javax.swing.JLabel votes3;
    private javax.swing.JLabel votes4;
    private javax.swing.JLabel votes5;
    // End of variables declaration//GEN-END:variables
}
