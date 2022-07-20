package guiimpl;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JFileChooser;

//Creation of GUI class
public class GUI implements ActionListener,ListSelectionListener{
    //Declaring frames - signIn, signUp, changePassword and operationFrame (mail application)
    protected JFrame signIn,signUp,operationFrame,changePassword;
    //Declaring panels - operations, modifyDetails, mailApp, deleteAcc and logOut
    protected JComponent operations,modifyDetails,logOut,mailApp,deleteAcc;

    //Declaring components for signIn frame
    protected JLabel usernameLabel,passwordLabel,msgLabel,msgLabel1;
    protected JTextField usernametf;
    protected JPasswordField passwordtf;
    protected JButton sign_in,sign_up,clear1;

    //Declaring components for changePassword frame
    protected JLabel oldPwd,newPwd,confirmNewPwd,msgLabel6;
    protected JPasswordField oldPwdtf,newPwdtf,confirmNewPwdtf;
    protected JButton changePwd,changePwd2,clear5,cancel3;

    //Declaring components for signUp frame
    protected JLabel fname,lname;
    protected JTextField fnametf,lnametf;
    protected JLabel usernameLabel2,passwordLabel2,msgLabel2;
    protected JTextField usernametf2;
    protected JPasswordField passwordtf2;
    protected JButton sign_up2,clear2,cancel5;
    protected int flagSignUp=0; 

    //Declaring components for operationFrame
    protected JList<String> list,sublist;
    protected JSplitPane splitPane;

    //Declaring components for modifyDetails panel and logOut panel
    protected JLabel fname2,lname2,passwordLabel3,msgLabel3,msgLabel4;
    protected JTextField fnametf2,lnametf2;
    protected JPasswordField passwordtf3;
    protected JButton logout,cancel1,cancel4,modify,clear4;

    //Declaring components for mailApp panel
    protected JLabel from,to,sub,cc,bcc,body,attach1;
    protected JTextField fromt,tot,cct,bcct,subt;
    protected JTextArea bodyt,attach1t;
    protected JButton send,add_attach,add_link,add_image,clear3,back2;
    protected JFileChooser jc1;

    //Declaring components for deleteAcc panel
    protected JLabel msgLabel5;
    protected JButton delete,cancel2;

    //constructor for GUI class
    public GUI(){
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { 
        }

        //Set fonts for components of frame and panel using UIManager
        UIManager.put("Label.font",new Font("Serif",Font.BOLD,17));
        UIManager.put("TextField.font",new Font("Serif",Font.PLAIN,16));
        UIManager.put("TextArea.font",new Font("Serif",Font.PLAIN,16));
        UIManager.put("PasswordField.font",new Font("Serif",Font.PLAIN,16));
        UIManager.put("Button.font",new Font("Serif",Font.PLAIN,17));
        UIManager.put("List.font",new Font("Serif",Font.PLAIN,16));

        //Initializing signIn frame and its components
        signIn=new JFrame("Sign In");
        usernameLabel=new JLabel("Username");
        passwordLabel=new JLabel("Password");
        usernametf=new JTextField(40);
        passwordtf=new JPasswordField(40);
        msgLabel=new JLabel("       ( If you have changed password for your gmail account please change it here )        ");
        msgLabel1=new JLabel("");
        changePwd=new JButton("Change Password");
        changePwd.setToolTipText("If you have changed password for your gmail account please change it here before signing in");
        sign_in=new JButton("Sign In");
        sign_up=new JButton("Sign Up");
        clear1=new JButton("Clear");

        signIn.add(usernameLabel);
        signIn.add(usernametf);
        signIn.add(passwordLabel);
        signIn.add(passwordtf);
        signIn.add(sign_in);
        signIn.add(sign_up);
        signIn.add(clear1);
        signIn.add(changePwd);
        signIn.add(msgLabel);
        signIn.add(msgLabel1);
        sign_in.setToolTipText("Existing User : Sign In");
        sign_up.setToolTipText("New User : Sign Up");
        msgLabel.setFont(new Font("Times New Roman",Font.PLAIN,14));
        msgLabel.setBackground(Color.WHITE);
        sign_in.addActionListener(this);
        sign_up.addActionListener(this);
        clear1.addActionListener(this);

        //Set frame size, icon for signIn frame
        Image icon1=Toolkit.getDefaultToolkit().getImage("D:\\cnproj\\mail\\try1\\guiimpl\\signin.jfif");
        signIn.setIconImage(icon1);
        signIn.setSize(750,600);
        signIn.setVisible(true);
        signIn.setResizable(false);
        signIn.setLayout(new FlowLayout(FlowLayout.CENTER,120,30));
        signIn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Initializing changePassword frame and its components
        changePassword=new JFrame("Change Password");
        Image icon4=Toolkit.getDefaultToolkit().getImage("D:\\cnproj\\mail\\try1\\guiimpl\\key2.PNG");
        changePassword.setIconImage(icon4);
        oldPwd=new JLabel("          Old Password :  ");
        oldPwdtf=new JPasswordField(30);
        newPwd=new JLabel("          New Password :  ");
        newPwdtf=new JPasswordField(30);
        confirmNewPwd=new JLabel("Confirm new password :");
        confirmNewPwdtf=new JPasswordField(30);
        changePwd2=new JButton("Change Password");
        clear5=new JButton("Clear");
        cancel3=new JButton("Cancel");
        msgLabel6=new JLabel("");
        changePassword.add(oldPwd);
        changePassword.add(oldPwdtf);
        changePassword.add(newPwd);
        changePassword.add(newPwdtf);
        changePassword.add(confirmNewPwd);
        changePassword.add(confirmNewPwdtf);
        changePassword.add(changePwd2);
        changePassword.add(clear5);
        changePassword.add(cancel3);
        changePassword.add(msgLabel6);
        changePwd.addActionListener(this);
        changePwd2.addActionListener(this);
        cancel3.addActionListener(this);
        changePassword.setSize(850, 700);
        changePassword.setVisible(false);
        changePassword.setLayout(new FlowLayout(FlowLayout.CENTER,120,75));
        changePassword.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Initializing signUp frame and its components
        signUp=new JFrame("Sign Up");
        fname=new JLabel("First Name : ");
        lname=new JLabel("Last Name : ");
        fnametf=new JTextField(30);
        lnametf=new JTextField(30);
        usernameLabel2=new JLabel("Username : ");
        passwordLabel2=new JLabel("Password : ");
        usernametf2=new JTextField(30);
        passwordtf2=new JPasswordField(30);
        sign_up2=new JButton("Sign Up");
        clear2=new JButton("Clear");
        cancel5=new JButton("Cancel");
        msgLabel2=new JLabel("");

        signUp.add(fname);
        signUp.add(fnametf);
        signUp.add(lname);
        signUp.add(lnametf);
        signUp.add(usernameLabel2);
        signUp.add(usernametf2);
        signUp.add(passwordLabel2);
        signUp.add(passwordtf2);
        signUp.add(sign_up2);
        signUp.add(clear2);
        signUp.add(cancel5);
        signUp.add(msgLabel2);

        sign_up2.addActionListener(this);
        clear2.addActionListener(this);
        cancel5.addActionListener(this);
        Image icon2=Toolkit.getDefaultToolkit().getImage("D:\\cnproj\\mail\\try1\\guiimpl\\signin.jfif");
        signUp.setIconImage(icon2);
        signUp.setSize(700,500);
        signUp.setVisible(false);
        signUp.setResizable(false);
        signUp.setLayout(new FlowLayout(FlowLayout.CENTER,100,35));
        signUp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Initializing operations panel and its components
        operations=new JPanel();
        String oprns[]={"Compose Mail","Check Mails","Manage your account","Logout"};
        String accountOperations[]={"Modify Details","Add another account","Delete account"};
        list=new JList<String>(oprns);
        sublist=new JList<String>(accountOperations);
        list.addListSelectionListener(this);
        list.setFixedCellHeight(30);
        sublist.setFixedCellHeight(30);
        operations.add(list);

        //Initializing logOut panel and its components
        logOut=new JPanel();
        msgLabel3=new JLabel("  Are you sure ? Do you want to Logout ?");
        logout=new JButton("Logout [->");
        cancel4=new JButton("Cancel");
        logOut.add(msgLabel3);
        msgLabel3.setBounds(150,100,400,100);
        msgLabel3.setFont(new Font("Serif",Font.BOLD,20));
        logOut.add(logout);
        logout.addActionListener(this);
        logout.setBounds(150,250,150,50);
        logout.setFont(new Font("Serif",Font.PLAIN,16));
        logOut.add(cancel4);
        cancel4.addActionListener(this);
        cancel4.setBounds(350,250,150,50);
        cancel4.setFont(new Font("Serif",Font.PLAIN,16));
        logOut.setLayout(null);
        logOut.setSize(900, 850);

        //Initializing splitPane to be set for operationFrame
        splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        //Initializing operationFrame and set size,icon for frame
        operationFrame=new JFrame("Operations");
        operationFrame.add(splitPane,BorderLayout.CENTER);
        operationFrame.setSize(1000,800);
        operationFrame.setVisible(false);
        operationFrame.setResizable(false);
        operationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon3=Toolkit.getDefaultToolkit().getImage("D:\\cnproj\\mail\\try1\\guiimpl\\mail3.png");
        operationFrame.setIconImage(icon3);
   
        //Initializing modifyDetails panel and its components
        modifyDetails=new JPanel();
        fname2=new JLabel("First Name : ");
        lname2=new JLabel("Last Name : ");
        fnametf2=new JTextField(30);
        lnametf2=new JTextField(30);
        passwordLabel3=new JLabel("Password : ");
        passwordtf3=new JPasswordField(30);
        msgLabel4=new JLabel("");
        clear4=new JButton("Clear");
        modify=new JButton("Modify");
        cancel1=new JButton("Cancel");
        modify.addActionListener(this);
        cancel1.addActionListener(this);
        clear4.addActionListener(this);
        modifyDetails.add(fname2);
        modifyDetails.add(fnametf2);
        modifyDetails.add(lname2);
        modifyDetails.add(lnametf2);
        modifyDetails.add(passwordLabel3);
        modifyDetails.add(passwordtf3);
        modifyDetails.add(modify);      
        modifyDetails.add(clear4);
        modifyDetails.add(cancel1);
        modifyDetails.add(msgLabel4);
        modifyDetails.setSize(900, 850);
        modifyDetails.setLayout(new FlowLayout(FlowLayout.CENTER,120,75));

        //Initializing deleteAcc panel and its components
        deleteAcc=new JPanel();
        msgLabel5=new JLabel("Delete this account ? ");
        delete=new JButton("Delete");
        cancel2=new JButton("Cancel");
        msgLabel5.setBounds(150,100,400,100);
        delete.setBounds(150,250,150,50);
        cancel2.setBounds(350,250,150,50);
        msgLabel5.setFont(new Font("Serif",Font.BOLD,20));
        delete.addActionListener(this);
        cancel2.addActionListener(this);
        deleteAcc.add(msgLabel5);
        deleteAcc.add(delete);
        deleteAcc.add(cancel2);
        deleteAcc.setSize(900,850);
        deleteAcc.setLayout(null);
   
        //Initializing mailApp panel and its components
        mailApp=new JPanel();
        from=new JLabel("       From :   ");
        to=new JLabel("           To :   ");
        cc=new JLabel("           CC :   ");
        bcc=new JLabel("         BCC :   ");
        sub=new JLabel("    Subject :   ");
        body=new JLabel("        Body :   ");
        attach1=new JLabel("Attachments :");

        fromt=new JTextField(40);
        tot=new JTextField(40);
        cct=new JTextField(40);
        bcct=new JTextField(40);
        subt=new JTextField(40);

        bodyt=new JTextArea(10,40);
        attach1t=new JTextArea(5,40);

        send=new JButton("Send");
        add_attach=new JButton("Attach Files");
        add_link=new JButton("Attach Link");
        add_image=new JButton("Attach Inline Image");
        add_image.setInheritsPopupMenu(true);
        clear3=new JButton("Clear");
        back2=new JButton("Back");

        send.addActionListener(this);
        add_attach.addActionListener(this);
        add_link.addActionListener(this);
        add_image.addActionListener(this);
        fromt.addActionListener(this);
        tot.addActionListener(this);
        clear3.addActionListener(this);
        back2.addActionListener(this);

        mailApp.add(from);
        mailApp.add(fromt);
        mailApp.add(to);
        mailApp.add(tot);
        mailApp.add(cc);
        mailApp.add(cct);
        mailApp.add(bcc);
        mailApp.add(bcct);
        mailApp.add(sub);
        mailApp.add(subt);
        mailApp.add(body);
        mailApp.add(bodyt);
        mailApp.add(attach1);
        mailApp.add(attach1t);
        mailApp.add(add_attach);
        mailApp.add(add_link);
        mailApp.add(add_image);       
        mailApp.add(send);
        mailApp.add(clear3);
        mailApp.setSize(900,850);
        mailApp.setLayout(new FlowLayout(FlowLayout.CENTER,100,20));
        
        //Add listSelectionListener for sublist and specify corresponding events
        sublist.addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent le){
                    if(sublist.getSelectedValue()=="Modify Details"){
                        splitPane.setBottomComponent(modifyDetails);
                        fnametf2.setText("");
                        lnametf2.setText("");
                        passwordtf3.setText("");
                    }
                    if(sublist.getSelectedValue()=="Add another account"){
                        signUp.setVisible(true);
                        fnametf.setText("");
                        lnametf.setText("");
                        usernametf2.setText("");
                        passwordtf2.setText("");
                        flagSignUp=1;
                    }
                    if(sublist.getSelectedValue()=="Delete account"){
                        splitPane.setBottomComponent(deleteAcc);
                    }
                }
            }
        );

    }

    public void actionPerformed(ActionEvent ae){

    }
    public void valueChanged(ListSelectionEvent le){

    }
    public String getSubject(){
        return subt.getText();
    }
    public String getContent(){
        return bodyt.getText();
    }
}
