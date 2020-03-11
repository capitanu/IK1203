package edu.hm.cs.applet;

///////////////////////////////////////////////////////////////////////////////
// Author: Ryan Gilbert (rmgilbe@asu.edu rgilbert@celeritysoft.com)
// 
// Desc  : Simple JApplet that helps visualize datagram fragmentation
//
// Notes : This applet is replaces an applet coded by Albert Huang in 1997
//       : as part of course work at the University of Pennsylvania. 
///////////////////////////////////////////////////////////////////////////////

// Imported Libraries
import javax.swing.tree.*;
import javax.swing.*;

import edu.hm.cs.applet.util.AppletDescription;

import java.awt.*;
import java.awt.event.*;

// Class  : IPFragApplet
// Purpose: Simple Java JApplet that illustrates datagram fragmenation
public class IPFragApplet
        extends JApplet
        implements ActionListener /* Used by Calculate Button click event */
{
    // Constants
    final String DG_BYTE_INFO   = " byte information field";
    final String DG_OFFSET      = "Offset: ";
    final String DG_ID          = "ID: ";
    final String DG_FLAG        = "Flag: ";
    final int INT_MAX            = 65535; // 2^16 - 1         
    
    
    // Display Labels
    JLabel lblDGSize;
    JLabel lblMTU;
    JLabel lblID;
    JLabel lblMsg;
    
    // Corresponding Text Fields
    JTextField txtDGSize;
    JTextField txtMTU;
    JTextField txtID;
    
    // Tree View with results
    JTree tvwFrags;
    DefaultMutableTreeNode nodRoot;
    
    // Button used to initiate calculations
    JButton btnCalc;
    
    // Panels to organize GUI
    JPanel pnlUpperBorder;
    JPanel pnlMain;
    JPanel pnlBottom;
    JPanel pnlTree;
            
    // Method : init
    // Purpose: Instantiate all of the controls and perform the setup
    @Override
    public void init()
    {
        // Create the labels and their corresponding text fields
        lblDGSize = new JLabel("Datagram Size: ");
        lblMTU = new JLabel("MTU: ");
        lblID = new JLabel("Datagram ID: ");
        
        txtDGSize = new JTextField();
        txtMTU = new JTextField();
        txtID = new JTextField();
        
        // Create tree view to display the resulting fragments
        // Root tree node
        nodRoot = new DefaultMutableTreeNode("Fragments");
        tvwFrags = new JTree(nodRoot);
        tvwFrags.setRootVisible(true);
        tvwFrags.setShowsRootHandles(true);
                
        
        // Create the button used to initiate the calculation and setup
        // the action listener
        btnCalc = new JButton("Calculate");
        btnCalc.addActionListener(this);
        
        // Create Error label for displaying errors. It is also used
        // to display the number of fragments.
        lblMsg = new JLabel("");
        lblMsg.setForeground(Color.red);
        
        // Create the panel for the bottom to hold the calculate button
        pnlBottom = new JPanel();
        pnlBottom.setLayout( new BoxLayout(pnlBottom, BoxLayout.X_AXIS));
        
        // This causes the button to spring to the right side. I hate
        // Java layout managers, but I am dealing with it.
        pnlBottom.add(Box.createRigidArea(new Dimension(10,10)));
        pnlBottom.add(lblMsg);
        pnlBottom.add(Box.createHorizontalGlue());
        pnlBottom.add(btnCalc);
        pnlBottom.add(Box.createRigidArea(new Dimension(10,10)));
        
        /* add applet description
        AppletDescription theDesc = new AppletDescription(this, 
				"Ryan Gilber, Arizona State University",
				"IP Fragmentation Visualization. Datagram size includes an IP header of 20 bytes. MTU and Datagram size must be greater than 30, and all values must be less than 2^16 - 1 (65535).");
		pnlBottom.add(theDesc);
		*/
		
		pnlBottom.add(Box.createRigidArea(new Dimension(10,10)));
        
        // Create the upper panel holding all the labels and text fields
        pnlUpperBorder = new JPanel();
        pnlUpperBorder.setLayout(
                new BoxLayout(pnlUpperBorder, BoxLayout.X_AXIS));
        
        // Add each control and then a ten pixel spacer
        pnlUpperBorder.add(Box.createRigidArea(new Dimension(10,10)));
        pnlUpperBorder.add(lblDGSize);
        pnlUpperBorder.add(Box.createRigidArea(new Dimension(10,10)));
        pnlUpperBorder.add(txtDGSize);
        pnlUpperBorder.add(Box.createRigidArea(new Dimension(10,10)));
        pnlUpperBorder.add(lblMTU);
        pnlUpperBorder.add(Box.createRigidArea(new Dimension(10,10)));
        pnlUpperBorder.add(txtMTU);
        pnlUpperBorder.add(Box.createRigidArea(new Dimension(10,10)));
        pnlUpperBorder.add(lblID);
        pnlUpperBorder.add(Box.createRigidArea(new Dimension(10,10)));
        pnlUpperBorder.add(txtID);
        pnlUpperBorder.add(Box.createRigidArea(new Dimension(10,10)));
        
        // Create tree panel
        pnlTree = new JPanel();
        pnlTree.setLayout(new BoxLayout(pnlTree, BoxLayout.X_AXIS));
        pnlTree.add(Box.createRigidArea(new Dimension(10,10)));
        
        // Use a scroll pane for the tree view in case the text
        // expands past the size of the applet        
        pnlTree.add(new JScrollPane(tvwFrags));
        pnlTree.add(Box.createRigidArea(new Dimension(10,10)));
        
        
        // Create main panel to hold everything
        pnlMain = new JPanel();
        pnlMain.setLayout(
                new BorderLayout());
        
        // Add child panels to our main root panel
        pnlMain.add(pnlUpperBorder, BorderLayout.NORTH);
        pnlMain.add(pnlTree, BorderLayout.CENTER);
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);
        
        this.setContentPane(pnlMain);
    }

    // Method : actionPerformed
    // Purpose: Called when calculate is clicked. This method will validate
    //        : the text fields and then generate the appropriate tree nodes
    public void actionPerformed(ActionEvent e)
    {
        // Locals used for calculations
        int iMTU = 0;
        int iSize = 0;
        int iID = 0;
        
        // Clear msg/error text
        lblMsg.setText("");
        
        try
        {
            // Never trust user input. Try to parse the text.
            iMTU = Integer.parseInt(txtMTU.getText());
            iSize = Integer.parseInt(txtDGSize.getText());
            iID = Integer.parseInt(txtID.getText());
            
            // Basically these parameters could be checked better, but
            // I currently require the datagram size to be over 30 bytes
            // and the MTU has to be over 30 bytes while they are all a valid
            // 16-bit integer.
            // If the data is valid...
            if(iMTU > 30 && iMTU <= INT_MAX && iSize > 30 && iSize <= INT_MAX
                    && iID > 0 && iID <= INT_MAX)
            {   
                // Used in calculations and text rendering
                int iInfoSizeFlag1 = (iMTU - 20) - ((iMTU - 20) % 8);  //intermediate fragments MUST be multiple of 8 bytes
                int iInfoSizeFlag0 = (iMTU - 20); // last fragment can use all bytes (even if not multiple of 8)
                String sInfoSizeFlag1 = new Integer(iInfoSizeFlag1).toString();
                String sInfoSizeFlag0 = new Integer(iInfoSizeFlag0).toString();
                int iRemainder = (iSize - 20) % iInfoSizeFlag1;
             
                // Get number of datagrams
                int iDGCount = (int) Math.ceil(
                        (double) (iSize - 20) / iInfoSizeFlag1);
                
                if (iRemainder <= (iInfoSizeFlag0-iInfoSizeFlag1)){
                	// can put remainder in last packet (must not be multiple of 8 bytes)
                	iDGCount--;
                	iRemainder = 0;
                }
                
                // Remove all the current nodes
                nodRoot.removeAllChildren();

                // For each datagram...
                for(int i = 0; i < iDGCount; i++)
                {
                    // Root for current datagram
                    DefaultMutableTreeNode nodNew =
                            new DefaultMutableTreeNode("Datagram " + (i + 1));
                    
                    // If we are not on the last datagram...
                    if (i + 1 < iDGCount)
                    {
                        DefaultMutableTreeNode nodInfo =
                                new DefaultMutableTreeNode(
                                		sInfoSizeFlag1 + DG_BYTE_INFO);
                        nodNew.add(nodInfo);
                    }
                    else
                    {
                        // Calculate the remaining bytes to be sent
                        String sTemp = "";
                        
                        sTemp = (iRemainder == 0) ?
                        		Integer.toString(((iSize - 20)-(i*iInfoSizeFlag1))) :
                            new Integer( iRemainder ).toString();
                        
                        DefaultMutableTreeNode nodInfo =
                                new DefaultMutableTreeNode(
                                sTemp + DG_BYTE_INFO);
                        
                        nodNew.add(nodInfo);                        
                    }
                    
                    // Create ID
                    DefaultMutableTreeNode nodID =
                        new DefaultMutableTreeNode(
                            DG_ID + iID);
                        
                    nodNew.add(nodID);  
                    
                    // Create Offset
                    DefaultMutableTreeNode nodOffset =
                        new DefaultMutableTreeNode(
                            DG_OFFSET + (i * iInfoSizeFlag1 / 8));
                        
                    nodNew.add(nodOffset);                     
                    
                    // Create Flag
                    DefaultMutableTreeNode nodFlag = null;
                    
                    // Flag is one or zero for the last
                    String sFlag = (i + 1 < iDGCount) ?
                        DG_FLAG + "1" : DG_FLAG + "0";
                    
                    nodFlag = new DefaultMutableTreeNode(sFlag);

                    nodNew.add(nodFlag);                                        
                    
                    nodRoot.add(nodNew);
                }
                
                // Label will be used to display number of datagrams instead
                // of an error message, since we did not get an error
                lblMsg.setForeground(Color.blue);
                lblMsg.setText("Number of Datagrams: " + iDGCount);
                
                // Needed to refresh the tree. Not sure why the control
                // doesn't do this automatically when you change the nodes
                ((DefaultTreeModel)tvwFrags.getModel()).reload();
            
            }
            else
                // Invalid parameters, let the handler deal with it.
                throw new NumberFormatException();
        }
        catch(NumberFormatException errBadInput)
        {
            lblMsg.setForeground(Color.red);
            lblMsg.setText("Verify parameters are valid integers.");
        }
        catch(Exception errGeneric)
        {
            lblMsg.setForeground(Color.red);
            lblMsg.setText("Error: " + errGeneric.getMessage());            
        }
    }
}
