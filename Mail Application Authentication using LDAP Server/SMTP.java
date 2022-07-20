import java.util.Properties;
import javax.mail.Message;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.AuthenticationFailedException;
import javax.mail.Session;
import javax.mail.BodyPart;
import javax.mail.Part;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.event.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;

import java.util.regex.*;

//Import GUI,LDAP and IMAP classes
import guiimpl.GUI;                          
import ldapimpl.LDAP;                        
import imapimpl.IMAP;

//Creation of SMTP class
public class SMTP extends GUI{

    LDAP authObject;                     //LDAP object
    IMAP imapObject;                     //IMAP object
    IMAP tempImapObj;
    String file,imageFile;
    String username,password,newUsername,newPassword;
    String link_name,link_url;
    Multipart multipart_temp; 
    int bodyPart_index;
    int flag=0;

    public SMTP(){
        super();                 //initializing GUI,LDAP,IMAP objects
        authObject=new LDAP();
        imapObject=new IMAP();
        //tempImapObj=new IMAP();
        file="";
        imageFile="";
        password="";
        link_name="";
        link_url="";
        multipart_temp=new MimeMultipart();      //Creating MimeMultipart object that holds any attachments while composing the email
        bodyPart_index=0;
    }

    /*createSession function for creating a session with the smtp server with the given username(mail-id).The username and password
    is authenticated with the getPasswordAuthentication method. The created session object is returned back.*/

    public Session createSession(String mail_id,String pwd){
        Properties properties=new Properties();
        System.out.println("Created session with username : "+mail_id);
        String host="smtp.gmail.com";

        properties.put("mail.smtp.auth",true);
        properties.put("mail.smtp.starttls.enable",true);
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port","587");

        Authenticator authenticator=new  Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(mail_id,pwd);
            }
        };

        //Create the session object with specified properties and authenticator
        Session session=Session.getInstance(properties,authenticator);
        return session;
    }

//attachFile method is used to add the file attachments (if any) of the email to the MimeMultipart object
    public void attachFile(){
        try{
            if(!(file.equals(""))){
                BodyPart msgBody=new MimeBodyPart();
                DataSource src=new FileDataSource(file);
                msgBody.setDataHandler(new DataHandler(src));
                int index=file.lastIndexOf('\\');
                String fileName=file.substring(index+1);
                msgBody.setFileName(fileName);
                attach1t.append(fileName+"\n");
                multipart_temp.addBodyPart(msgBody,bodyPart_index++);
            }
        }catch(MessagingException me){
            System.out.println(me);
        }
    }

//attachLink method is used to add the URL(links) attachments (if any) of the email to the MimeMultipart object
    public void attachLink(){
        try{
            if(!(link_name.equals(""))){
                BodyPart msgBody=new MimeBodyPart();
                msgBody.setContent("<br><h4>"+link_name+" : "+"<a href=\""+link_url+"\">"+link_url+"</a></h4>","text/html");
                multipart_temp.addBodyPart(msgBody,bodyPart_index++);
                System.out.println("URL attached");
            }
        }catch(MessagingException me){
            System.out.println(me);
        }
    }

//attachImage method is used to add the image file attachments (if any) of the email to the MimeMultipart object
    public void attachImage(){
        try{
            BodyPart msgBody=new MimeBodyPart();
            String text="<h5>Image : </h5><img src=\"cid:image_source"+bodyPart_index+"\">";
            msgBody.setContent(text,"text/html");
            multipart_temp.addBodyPart(msgBody);
            msgBody=new MimeBodyPart();
            DataSource img_src=new FileDataSource(imageFile);
            msgBody.setDataHandler(new DataHandler(img_src));
            msgBody.addHeader("Content-ID","<image_source"+bodyPart_index+">");
            multipart_temp.addBodyPart(msgBody,bodyPart_index++);
            System.out.println("Image attached");
         }catch(MessagingException me){
             System.out.println(me);
         }
    }

/*sendMail function allows the user to send email by creating a new session with the SMTP server and creates a new message object.
Add the body of the mail as the first part of the message and then add the other attachments(if any) from temporary multipart object to the multipart object to be added to the message object*/
    public void sendMail(){
        
        try{
            MimeMessage message;
            String originalMail="",Body="";
            //If the flag value is not 2 it is a normal email
            if(flag!=2){
                Session session=createSession(username,password);
                message=new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(tot.getText()));
                message.setSubject(getSubject());
            }
            //If the flag value is 2 it is reply email
            else{
                Session session=imapObject.session;
                Message currentMail=imapObject.getCurrentMail();
                message=new MimeMessage(session);
                message=(MimeMessage)currentMail.reply(false);
                message.setReplyTo(currentMail.getReplyTo());
                message.addRecipient(Message.RecipientType.TO,currentMail.getFrom()[0]);
                message.setSubject("Re : "+getSubject());
                if(currentMail.getContent() instanceof Multipart){
                    Multipart mail_content=(Multipart)currentMail.getContent();
                    int nParts=mail_content.getCount();
                    for(int i=0;i<nParts;i++){
                        MimeBodyPart mail_part=(MimeBodyPart)mail_content.getBodyPart(i);
                        if(mail_part.isMimeType("text/plain")){
                            Body=(String)mail_part.getContent();
                        }
                    }
                }
                originalMail="\nOn "+currentMail.getSentDate()+",<"+username+"> wrote : \n"+Body;
                System.out.println("Inside else");
            }    
            if(!(cct.getText().equals(""))){
                message.addRecipients(Message.RecipientType.CC,InternetAddress.parse(cct.getText()));
            }

            if(!(bcct.getText().equals(""))){
                message.addRecipients(Message.RecipientType.BCC,InternetAddress.parse(bcct.getText()));
            }

            //Create Multipart object to store attachments of the email
            ///if(flag!=1){
            Multipart multipart=new MimeMultipart();
            BodyPart msgBody=new MimeBodyPart();
            msgBody.setText(getContent());
            if(flag==2){
                msgBody.setText(getContent()+"\n"+originalMail);
                ///flag=0;
            }
            multipart.addBodyPart(msgBody,0);
            if(flag!=1){
                for(int i=0;i<multipart_temp.getCount();i++){
                    msgBody=multipart_temp.getBodyPart(i);
                    multipart.addBodyPart(msgBody,i+1);
                    ///multipart_temp.removeBodyPart(i);
                }
            }else{
                Message fwdMail=imapObject.getCurrentMail();
                int j=1;
                if(fwdMail.getContent() instanceof Multipart){
                    Multipart mail_content=(Multipart)fwdMail.getContent();
                    int nParts=mail_content.getCount();
                    for(int i=0;i<nParts;i++){
                        MimeBodyPart mail_part=(MimeBodyPart)mail_content.getBodyPart(i);
                        if(mail_part.isMimeType("text/plain")){
                        }
                        if(Part.ATTACHMENT.equalsIgnoreCase(mail_part.getDisposition())){
                            multipart.addBodyPart(mail_part,j++);
                        }
                    }
                }
            }
        ///}
        ///else{}
            message.setContent(multipart);          //Add multipart object to the message object
            Transport.send(message);                //Send the message via Transport class send method

            if(flag==2){
                JOptionPane.showMessageDialog(operationFrame,"Reply Mail Sent Successfully !!", "Mail Sent",JOptionPane.PLAIN_MESSAGE);
                System.out.println("Reply Mail sent successfully");
            }
            else if(flag==1){
                JOptionPane.showMessageDialog(operationFrame,"Mail Forwarded Successfully !!", "Mail Sent",JOptionPane.PLAIN_MESSAGE);
                System.out.println("Mail Forwarded successfully");
            }
            else{
                JOptionPane.showMessageDialog(operationFrame,"Mail Sent Successfully !!", "Mail Sent",JOptionPane.PLAIN_MESSAGE);
                System.out.println("Mail Sent successfully");
            }
        }catch(AuthenticationFailedException ae){
            System.out.println("Username and Password not accepted!");
        }catch(MessagingException me){
            System.out.println("Exception : "+me);
        }catch(Exception e){
            System.out.println("Exception : "+e);
        }
        
    }

    public void actionPerformed(ActionEvent ae){

        /*Validate the username and password by calling authenticateUser method of LDAP class.After validation mail application can be used.*/
        if(ae.getSource()==sign_in){
             username=usernametf.getText();
             password=new String(passwordtf.getPassword());
                if(authObject.authenticateUser(username,password)){
                    signIn.dispose();
                    list.setSelectedIndex(0);
                    splitPane.setTopComponent(operations);
                    splitPane.setBottomComponent(mailApp);
                    operationFrame.setVisible(true);
                    operationFrame.setTitle("Mail Application (Signed In as - "+username+")");
                    int flag=imapObject.initializeImap(username,password);
                    if(flag==0){
                        imapInitialize();
                        msgLabel1.setText("");
                    }
                    else{
                        msgLabel1.setText("Enter valid email address and password!!");
                    }
                }
                else{
                    msgLabel1.setText("Enter valid email address and password!!");
                }
        }

        //Sign Up frame is opened when sign_up button is clicked
        if(ae.getSource()==sign_up){
            signIn.setVisible(false);
            signUp.setVisible(true);
        }

        /*Check the syntax of the email address and if valid call initializeImap(username,password) to check whether inbox is accessible.
        If accessible the address is valid, then add entry by calling addUser() method of IMAP class */
        if(ae.getSource()==sign_up2){
            msgLabel2.setText("");
            newUsername=usernametf2.getText();
            boolean valid=false;
            try{
                InternetAddress newid=new InternetAddress(newUsername);
                valid=true;    
            }catch(AddressException e){
                System.out.println("Invalid address");
            }
            newPassword=new String(passwordtf2.getPassword());

            ///int flag=imapObject.initializeImap(username,password);
            tempImapObj=new IMAP();
            int flag=tempImapObj.initializeImap2(newUsername,newPassword);
            //if the username is a valid mail address and the flag is 0 (inbox is accessible) mail Application is opened
            if(valid && flag==0){                                                 
                if(authObject.addUser(fnametf.getText(), lnametf.getText(),newUsername,newPassword)){
                    JOptionPane.showMessageDialog(signIn,"Sign Up Successful !!", "",JOptionPane.PLAIN_MESSAGE);
                    signUp.dispose();
                    if(flagSignUp==0){
                        signIn.setVisible(true);
                        usernametf.setText(newUsername);
                        passwordtf.setText(newPassword);
                    }
                    flagSignUp=0;
                    tempImapObj=null;
                    newUsername="";
                    newPassword="";
                }
                else{
                    System.out.println("Sign Up Unsuccessful!!");
                    signUp.dispose();
                }
            }
            else if(flag==-1){
                signUp.add(msgLabel2);
                msgLabel2.setText("This is not a valid mail address or password.Please enter details of a valid mail id.");
            }
            else{
                System.out.println("Not a valid email id.");
            }
        }
        //To send email
        if(ae.getSource()==send){
             sendMail();
             if(flag==1){
                 splitPane.setBottomComponent(imapObject.inboxPanel);
                 flag=0;
             }
         }

         //To add file attachments
         if(ae.getSource()==add_attach){
             jc1=new JFileChooser();
             jc1.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
             int option=jc1.showOpenDialog(signIn);
             if(option==JFileChooser.APPROVE_OPTION)
             {
                file=jc1.getSelectedFile().toString();
             }
             attachFile();
         }

         //To add URLs
         if(ae.getSource()==add_link){
            JTextField name = new JTextField();
            JTextField url = new JTextField();
            Object[] message = {
                "Name:", name,
                "URL:", url
            };
            int ok = JOptionPane.showConfirmDialog(null, message, "Attach Link", JOptionPane.OK_CANCEL_OPTION);
            if(ok==0){
                link_name=name.getText();
                link_url=url.getText();
            }
            System.out.println(ok);
            attachLink();
         }

         //To add image files as inline images 
         if(ae.getSource()==add_image){
             jc1=new JFileChooser();
             FileNameExtensionFilter imgFileFilter=new FileNameExtensionFilter("Images","jpg","jpeg","gif","png");
             jc1.setFileFilter(imgFileFilter);
             jc1.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
             int option_chose=jc1.showOpenDialog(signIn);
             if(option_chose==JFileChooser.APPROVE_OPTION){
                 imageFile=jc1.getSelectedFile().toString();
             }
             attachImage();
         }
         
        //Validate Recipient's email address if it is not correct warning is shown to user
        if(ae.getSource()==tot){
            if(Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-.]+$",tot.getText())){
                System.out.println("Correct format");
            }
            else{
                JOptionPane.showMessageDialog(signIn,
                                "Error: Please enter a valid email address", "Invalid Email Id",
                                JOptionPane.WARNING_MESSAGE);
            }
        }
        if(ae.getSource()==cancel4){
            splitPane.setBottomComponent(mailApp);
        }
        if(ae.getSource()==clear1){
            clear(signIn.getContentPane());
        }
        if(ae.getSource()==clear2){
            clear(signUp.getContentPane());
            msgLabel2.setText("");
        }
        if(ae.getSource()==clear3){
           clear(mailApp);
           fromt.setText(username);
        }
        if(ae.getSource()==clear4){
            clear(modifyDetails);
        }
        if(ae.getSource()==clear5){
            clear(changePassword.getContentPane());
        }

        //To change existing password.Call LDAP class modifyUserPwd method to change the password in LDAP server's database
        if(ae.getSource()==changePwd){
            if(usernametf.getText().isEmpty()){
                msgLabel1.setText("Please enter your username.");
            }else{
                if(authObject.searchUser(usernametf.getText())!=null){
                    username=usernametf.getText();
                    changePassword.setVisible(true);
                }
            }
        }

        /*Check whether the username exists or not.If exists allow user to change password after validating by accessing inbox */
        if(ae.getSource()==changePwd2){
            String old_pwd=new String(oldPwdtf.getPassword());
            String new_pwd=new String(newPwdtf.getPassword());
            if(new_pwd.equals(new String(confirmNewPwdtf.getPassword())) && imapObject.initializeImap(username,new_pwd)==0){
                authObject.modifyUserPwd(username,new_pwd);
                changePassword.setVisible(false);
                passwordtf.setText(new_pwd);
                msgLabel1.setText("Password changed Successfully!!");
            }
            else{
                msgLabel6.setText("This is not a valid password for your mail id.");
            }
        }

        //To modify user details in LDAP server database,invoke LDAP class modifyUserDetails method
        if(ae.getSource()==modify){
            String enteredPassword=new String(passwordtf3.getPassword());
            if(enteredPassword.equals(password)){
                authObject.modifyUserDetails(username,fnametf2.getText(),lnametf2.getText(),new String(passwordtf3.getPassword()));
                msgLabel4.setText("Modified Successfully!!");
            }else{
                msgLabel4.setText("Modify Unsucessful!! Password is incorrect.");
            }
        }

        //Logout from the current account and redirect to sign in frame
        if(ae.getSource()==logout){
            operationFrame.dispose();
            usernametf.setText("");
            passwordtf.setText("");
            signIn.remove(msgLabel1);
            signIn.setVisible(true);
        }

        //To delete user account from LDAP server's database,invoke LDAP class deleteUser method
        if(ae.getSource()==delete){
            authObject.deleteUser(username);
            operationFrame.dispose();
            usernametf.setText("");
            passwordtf.setText("");
            signIn.remove(msgLabel1);
            signIn.setVisible(true);
        }

        //Cancel button when clicked from sublist items will redirect to the compose mail page
        if(ae.getSource()==cancel1 || ae.getSource()==cancel2 || ae.getSource()==cancel3){
            operations.remove(sublist);
            operations.add(list);
            list.setSelectedIndex(0);
            operationFrame.setTitle("Mail Application(Signed In as - "+username+")");
            splitPane.setBottomComponent(mailApp);
        }

        if(ae.getSource()==cancel5){
            signUp.dispose();
            msgLabel2.setText(""); 
            if(flagSignUp==0){
               signIn.setVisible(true);
            }
        }

        //To display the details of the mail currently chosen by user
        if(ae.getSource()==imapObject.viewMail){
            imapObject.displayMail();
            splitPane.setBottomComponent(imapObject.mailPanel);
        }

        //To display next 5 emails in the inbox (only 5 emails are displayed at a time)
        if(ae.getSource()==imapObject.nextPage){
            imapObject.displayInbox();
            splitPane.setBottomComponent(imapObject.inboxPanel);
        }

        //To display previous 5 emails in the inbox (only 5 emails are displayed at a time)
        if(ae.getSource()==imapObject.prevPage){
            int n;
            if(imapObject.unseenMails!=0){
                n=10;
            }
            else{
                if(imapObject.no_of_mails%5!=0)
                {
                    n=(imapObject.no_of_mails%5)+5;
                }
                else{
                    n=10;
                }
            }
            imapObject.unseenMails+=n;
            imapObject.displayInbox();
            if(imapObject.nextPage.isEnabled()==false){
                imapObject.nextPage.setEnabled(true);
            }
        }

        //Switch from mailPanel to inboxPanel
        if(ae.getSource()==imapObject.back1){
            splitPane.setBottomComponent(imapObject.inboxPanel);
            clear(imapObject.mailPanel);
        }

        //To download attachments of the currently chosen mail
        if(ae.getSource()==imapObject.download){
            imapObject.downloadAttachments();
            splitPane.setBottomComponent(imapObject.mailPanel);
        }

        //To send a reply email for the currently chosen mail.Set the flag as 2 to indicate reply email.
        if(ae.getSource()==imapObject.reply){
            try{
           flag=2;
           Message currentMail=imapObject.getCurrentMail();
           clear(mailApp);
           fromt.setText(username);
           tot.setText(currentMail.getFrom()[0].toString());
           mailApp.add(back2);
           splitPane.setBottomComponent(mailApp);
            }catch(MessagingException me){
                System.out.println(me);
            }
        }

        //To forward the currently chosen mail.Set the flag as 1 to indicate forward mail.
        if(ae.getSource()==imapObject.forward){
            Message fwdMail=imapObject.getCurrentMail();
            Forward(fwdMail);
            mailApp.add(back2);
            splitPane.setBottomComponent(mailApp);
            flag=1;
        }

        //To switch back to mailPanel from forward/reply mail panel
        if(ae.getSource()==back2){
            splitPane.setBottomComponent(imapObject.mailPanel);
        }
        
        //To switch from inboxPanel to mailApp panel
        if(ae.getSource()==imapObject.back2){
            operations.remove(imapObject.mailTypes);
            operations.add(list);
            splitPane.setBottomComponent(mailApp);
            list.setSelectedIndex(0);
            mailApp.remove(back2);            //!!
            clear(mailApp);                   //!!
            fromt.setText(username);          //!!
        }
    }

    //valueChanged is overridden to perform corresponding operations when the selected list item is changed
    public void valueChanged(ListSelectionEvent le){
        if(list.getSelectedValue()!=null){
            if(list.getSelectedValue()=="Compose Mail"){
                operationFrame.setTitle("Mail Application (Signed In as - "+username+")");
                splitPane.setBottomComponent(mailApp);
                fromt.setText(username);
            }
            if(list.getSelectedValue()=="Check Mails"){
                if(imapObject.mailTypes.getSelectedValue()==null){
                imapObject.mailTypes.setSelectedIndex(0);
                }
                operations.remove(list);
                operations.add(imapObject.mailTypes);
                splitPane.setBottomComponent(imapObject.inboxPanel);
            }
            if(list.getSelectedValue()=="Manage your account"){
                operations.remove(list);
                operations.add(sublist);
                sublist.setSelectedIndex(0);
                operationFrame.setTitle("Manage your account (Signed In as - "+username+")");
                splitPane.setBottomComponent(modifyDetails);
            }
            if(list.getSelectedValue()=="Logout"){
                splitPane.setBottomComponent(logOut);
            }
        }
    }

    //clear method to clear the text fields, password fields and text areas of a container
    public void clear(Container container){
        Component compArr[]=container.getComponents();
        for(int i=0;i<compArr.length;i++){
            if(compArr[i] instanceof JTextField){
                JTextField temp=(JTextField)compArr[i];
                temp.setText("");
            }
            else if(compArr[i] instanceof JTextArea){
                JTextArea temp=(JTextArea)compArr[i];
                temp.setText("");
            }
            else if(compArr[i] instanceof JPasswordField){
                JPasswordField temp=(JPasswordField)compArr[i];
                temp.setText("");
            }
        }
    }

    //imapInitialize() to initialize the IMAP object
    public void imapInitialize(){
        imapObject.guiInbox();
        imapObject.guiMail();
        imapObject.prevPage.addActionListener(this);
        imapObject.viewMail.addActionListener(this);
        imapObject.download.addActionListener(this);
        imapObject.back1.addActionListener(this);
        imapObject.nextPage.addActionListener(this);
        imapObject.reply.addActionListener(this);
        imapObject.forward.addActionListener(this);
        imapObject.back2.addActionListener(this);
    }

    /*To forward current email, get the recipient mail address.All other details are filled with the current mail*/
    public void Forward(Message fwdMail){
        try{
            fromt.setText(username);
            tot.setText("");            //!!
            cct.setText("");
            bcct.setText("");
            subt.setText("Fwd : "+fwdMail.getSubject());   //!!
            bodyt.setText("----------Forwarded Message----------");
            bodyt.append("\nFrom : <"+fwdMail.getFrom()[0].toString()+">");
            bodyt.append("\nDate : "+fwdMail.getSentDate());
            bodyt.append("\nSubject : "+fwdMail.getSubject());               //!!
            bodyt.append("\nTo : <"+username+">");

            //!!
            /*if(fwdMail.getRecipients(Message.RecipientType.CC)!=null){
                Address[] ccRecipients=fwdMail.getRecipients(Message.RecipientType.CC);
                for(int i=0;i<ccRecipients.length;i++){
                    cct.setText(fwdMail.getRecipients(Message.RecipientType.CC)[i].toString()+" ");
                }
            }
            if(fwdMail.getRecipients(Message.RecipientType.BCC)!=null){
                Address[] bccRecipients=fwdMail.getRecipients(Message.RecipientType.BCC);
                for(int i=0;i<bccRecipients.length;i++){
                    bcct.setText(fwdMail.getRecipients(Message.RecipientType.BCC)[i].toString()+" ");
                }
            }*/
            String Body="";
            attach1t.setText("");          //!!
            if(fwdMail.getContent() instanceof Multipart){
                Multipart mail_content=(Multipart)fwdMail.getContent();
                int nParts=mail_content.getCount();
                for(int i=0;i<nParts;i++){
                    MimeBodyPart mail_part=(MimeBodyPart)mail_content.getBodyPart(i);
                    if(mail_part.isMimeType("text/plain") && i==0){
                        Body=(String)mail_part.getContent();
                        bodyt.append("\n"+Body);
                    }
                    if(Part.ATTACHMENT.equalsIgnoreCase(mail_part.getDisposition())){
                        String filePath=mail_part.getFileName();
                        int index=filePath.lastIndexOf('/');
                        String fileName=filePath.substring(index+1);
                        attach1t.append(fileName+"\n");
                    }
                }
            }
        }catch(MessagingException me){
            System.out.println("Forward Unsuccessful!!");
        }catch(Exception e){
            System.out.println("Exception : "+e);
        }
    }

    public static void main(String args[]){
        SMTP obj=new SMTP();                  //Creation of SMTP object
        System.out.println("SMTP object created : "+obj); 
    }
}