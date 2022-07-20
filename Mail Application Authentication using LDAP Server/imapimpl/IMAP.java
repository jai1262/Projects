package imapimpl;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.search.*;
import javax.mail.internet.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JFileChooser;

//Creation of IMAP class
public class IMAP implements ActionListener,ItemListener,ListSelectionListener{
    //Declaring inboxPanel, mailPanel and its components
    public JComponent inboxPanel,mailPanel;
    JLabel totalMsg,unreadMsg;
    JTextArea messageLabel[];
    JCheckBox chooseMail[];
    ButtonGroup checkboxGroup;
    public JButton viewMail,nextPage,prevPage,back2;

    JLabel from,to,sub,cc,bcc,body,attachment;
    JTextField fromt,tot,cct,bcct,subt;
    JTextArea bodyt,attacht;
    public JButton back1,download,reply,forward,delete,refresh;
    public  JList<String> mailTypes;
    JFileChooser jc1;

    //Declaring session, folder, store and Message Array to store inbox mails
    public int unseenMails,no_of_mails,selectedIndex=-1;
    public Session session;
    Folder inbox=null;
    Folder tempFolder=null;
    Store store=null;
    Store tempStore=null;
    Message[] inbox_messages;
    Message[] tempInbox;

    //constructor for IMAP class
    public IMAP(){
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { 
        }
        //Set fonts for components of inboxPanel and mailPanel using UIManager class
        UIManager.put("Label.font",new Font("Serif",Font.BOLD,17));
        UIManager.put("TextField.font",new Font("Serif",Font.PLAIN,16));
        UIManager.put("PasswordField.font",new Font("Serif",Font.PLAIN,16));
        UIManager.put("Button.font",new Font("Serif",Font.PLAIN,17));
        UIManager.put("List.font",new Font("Serif",Font.PLAIN,16));
        UIManager.put("TextArea.font",new Font("Serif",Font.PLAIN,16));
    }

    /*initializeImap(username,password) method creates a session with SMTP server and using store object, 
    mail id and password connect to IMAP server and access the user's inbox. If unable to access the user is notified that the mail address 
    and password might be wrong. */
    public int initializeImap(String username,String password){
        int flag=0;
        try{
            Properties properties=System.getProperties();
            properties.setProperty("mail.store.protocol","imaps");
            properties.put("mail.smtp.auth",true);
            properties.put("mail.smtp.starttls.enable",true);
            properties.setProperty("mail.smtp.host","smtp.gmail.com");
            properties.put("mail.smtp.port","587");
            Authenticator authenticator=new  Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(username,password);
                }
            };
            session=Session.getDefaultInstance(properties,authenticator);
            store=session.getStore("imaps");
            System.out.println("Username: "+username+" Password: "+password);
            store.connect("imap.gmail.com",username,password);
            inbox=store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
            System.out.println("Folder opened");
            inbox_messages=inbox.getMessages();
            System.out.println(inbox_messages.length);
            unseenMails=inbox_messages.length;
            no_of_mails=inbox_messages.length;
        }catch(AuthenticationFailedException e){
            flag=-1;
            System.out.println("Incorrect mail id or password.Please enter correct details.\n (Note : If the details are correct kindly check whether the Less Secure app access feature of your gmail account is 'ON' to use this application.)");
        }catch(Exception ae){
            flag=-1;
            System.out.println("Enter valid mail id"+ae);
        }
        return flag;
    }

    /*initializeImap2(username,password) creates a session to connect to IMAP server and access the inbox when the user tries to sign up. */
    public int initializeImap2(String nusername,String npassword){
        Session newSession;
        int flag=-2;
        try{
            Properties properties=System.getProperties();
            properties.setProperty("mail.store.protocol","imaps");
            properties.put("mail.smtp.auth",true);
            properties.put("mail.smtp.starttls.enable",true);
            properties.setProperty("mail.smtp.host","smtp.gmail.com");
            properties.put("mail.smtp.port","587");
            Authenticator authenticator=new  Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(nusername,npassword);
                }
            };
            newSession=Session.getDefaultInstance(properties,authenticator);
            tempStore=newSession.getStore("imaps");
            System.out.println("Username: "+nusername+" Password: "+npassword);
            tempStore.connect("imap.gmail.com",nusername,npassword);
            tempFolder=tempStore.getFolder("Inbox");
            tempFolder.open(Folder.READ_WRITE);
            tempInbox=tempFolder.getMessages();
            flag=0;
            tempFolder.close(true);
            tempStore.close();
        }catch(AuthenticationFailedException e){
            flag=-1;
            System.out.println("Incorrect mail id or password.Please enter correct details.\n (Note : If the details are correct kindly check whether the Less Secure app access feature of your gmail account is 'ON' to use this application.)");
        }catch(MessagingException me){
            flag=-1;
            System.out.println("Enter valid mail id"+me);
        }catch(Exception ae){
            flag=-1;
            System.out.println("Enter valid mail id"+ae);
        }
        return flag;
    }

    /*guiInbox() method initializes inboxPanel and its components */
    public void guiInbox(){
        inboxPanel =new JPanel();
        totalMsg=new JLabel();
        try{
        unreadMsg=new JLabel("Unread Messages in inbox : "+inbox.getUnreadMessageCount());
        }catch(MessagingException me){
            System.out.println("Exception : "+me);
        }
        totalMsg.setText("Total Messages in inbox : "+unseenMails);
        messageLabel=new JTextArea[5];
        chooseMail=new JCheckBox[5];
        checkboxGroup=new ButtonGroup();
        prevPage=new JButton("Prev");
        viewMail=new JButton("View Mail");
        nextPage=new JButton("Next");
        back2=new JButton("Back");
        refresh=new JButton("Refresh");
        inboxPanel.setSize(900,850);
        inboxPanel.setLayout(new FlowLayout(FlowLayout.CENTER,145,40));
        inboxPanel.add(totalMsg);
        inboxPanel.add(unreadMsg);
        for(int i=0;i<5;i++){
            messageLabel[i]=new JTextArea(2,50);
            chooseMail[i]=new JCheckBox(" ",false);
        }

        //Invoke the displayInbox() method to display the first 5 mails in the inbox
        displayInbox();

        for(int i=0;i<5;i++){
            chooseMail[i].addItemListener(this);
            checkboxGroup.add(chooseMail[i]);
            inboxPanel.add(chooseMail[i]);
            messageLabel[i].setBackground(Color.WHITE);
            messageLabel[i].setEditable(false);
            inboxPanel.add(messageLabel[i]);
        }
        prevPage.setEnabled(false);
        inboxPanel.add(prevPage);
        inboxPanel.add(viewMail);
        inboxPanel.add(nextPage);
        inboxPanel.add(back2);
        inboxPanel.add(refresh);
        refresh.addActionListener(this);

        //mailTypes list is added in the operation panel to switch between other folders of gmail
        String[] types={"Inbox","All Mails","Drafts","Sent Mails   ","Spam","Starred","Trash"};
        mailTypes=new JList<String>(types);
        mailTypes.setFixedCellHeight(30);
        mailTypes.addListSelectionListener(this);
        }
    
    /*guiMail() method initializes the mailPanel and its components. The panel is displayed when the user selects the corresponding 
    checkbox of a particular mail and clicks viewMail.*/
    public void guiMail(){
        mailPanel=new JPanel();
        from=new JLabel("         From : ");
        to=new JLabel("             To : ");
        cc=new JLabel("             CC : ");
        bcc=new JLabel("           BCC : ");
        sub=new JLabel("      Subject : ");
        body=new JLabel("      Body : ");
        attachment=new JLabel("Attachments :");

        fromt=new JTextField(40);
        tot=new JTextField(40);
        cct=new JTextField(40);
        bcct=new JTextField(40);
        subt=new JTextField(40);

        bodyt=new JTextArea(10,40);
        attacht=new JTextArea(5,40);

        back1=new JButton("Back");
        download=new JButton("Download Attachments");
        reply=new JButton("Reply");
        forward=new JButton("Forward");
        delete=new JButton("Delete");
        
        mailPanel.add(from);
        mailPanel.add(fromt);
        mailPanel.add(to);
        mailPanel.add(tot);
        mailPanel.add(cc);
        mailPanel.add(cct);
        mailPanel.add(bcc);
        mailPanel.add(bcct);
        mailPanel.add(sub);
        mailPanel.add(subt);
        mailPanel.add(body);
        mailPanel.add(bodyt);
        mailPanel.add(attachment);
        mailPanel.add(attacht);
        mailPanel.add(back1);
        mailPanel.add(download);
        mailPanel.add(reply);
        mailPanel.add(forward);
        mailPanel.add(delete);
       
        delete.addActionListener(this);

        Component compArr[]=mailPanel.getComponents();
        for(int i=0;i<compArr.length;i++){
            if(compArr[i] instanceof JTextField){
                JTextField temp=(JTextField)compArr[i];
                temp.setEditable(false);
            }
            else if(compArr[i] instanceof JTextArea){
                JTextArea temp=(JTextArea)compArr[i];
                temp.setEditable(false);
            }
        }
        mailPanel.setSize(900,850);
        mailPanel.setLayout(new FlowLayout(FlowLayout.CENTER,140,20));
    }

    //displayInbox() method gets the from address and subject of mails and displays it
    public void displayInbox(){
    int k=0;
    try{
        String fromAddress,subject;
        int j=0;
        for(int i=unseenMails-1;i>=unseenMails-5;i--){
            if(i>=0){
                Message msg=inbox_messages[i];
                fromAddress=msg.getFrom()[0].toString();
                subject=msg.getSubject();
                if(i<9){
                    chooseMail[k].setText("0"+Integer.toString(i+1));
                }
                else{
                chooseMail[k].setText(Integer.toString(i+1));
                }
                messageLabel[k].setText("From : "+fromAddress+"\nSubject : "+subject);
                j++;
                k++;
            }else{
                if(k<5){
                messageLabel[k].setText("");
                chooseMail[k].setText("    ");
                unseenMails=0;
                nextPage.setEnabled(false);
                k++;
                }
            }
        } 
        if(j==5){
        unseenMails=unseenMails-5;
        }
        if(unseenMails<inbox_messages.length-5){
            prevPage.setEnabled(true);
        }
        else{
            prevPage.setEnabled(false);
        }
        if(unseenMails>0){
            nextPage.setEnabled(true);
        }
        else{
            nextPage.setEnabled(false);
        }
        System.out.println("Remaining "+inbox.getName()+" Messages : "+unseenMails);
    }catch(MessagingException me){
        System.out.println("Exception : "+me+" k : "+k);
    }catch(Exception e){
        System.out.println("Exception : "+e+" k : "+k);
    }
    }

    /*displayMail() method gets details such as from address, subject, body, attachments (if any) of the selected mail and displays it in the mailPanel */
    public void displayMail(){
        try{
            Message mail=inbox_messages[unseenMails+(4-selectedIndex)];
            String fromAddress=mail.getFrom()[0].toString();
            String subject=mail.getSubject();
            fromt.setText(fromAddress);
            subt.setText(subject);
            tot.setText(mail.getRecipients(Message.RecipientType.TO)[0].toString());
            if(mail.getRecipients(Message.RecipientType.CC)!=null){
                Address[] ccRecipients=mail.getRecipients(Message.RecipientType.CC);
                for(int i=0;i<ccRecipients.length;i++){
                    cct.setText(mail.getRecipients(Message.RecipientType.CC)[i].toString()+" ");
                }
            }
            if(mail.getRecipients(Message.RecipientType.BCC)!=null){
                Address[] bccRecipients=mail.getRecipients(Message.RecipientType.BCC);
                for(int i=0;i<bccRecipients.length;i++){
                    bcct.setText(mail.getRecipients(Message.RecipientType.BCC)[i].toString()+" ");
                }
            }
            String Body="";
            attacht.setText("");              //!!
            if(mail.getContent() instanceof Multipart){
                Multipart mail_content=(Multipart)mail.getContent();
                int nParts=mail_content.getCount();
                for(int i=0;i<nParts;i++){
                    MimeBodyPart mail_part=(MimeBodyPart)mail_content.getBodyPart(i);
                    if(mail_part.isMimeType("text/plain")){
                        Body=(String)mail_part.getContent();
                        bodyt.setText(Body);
                    }
                    if(Part.ATTACHMENT.equalsIgnoreCase(mail_part.getDisposition())){
                        String filePath=mail_part.getFileName();
                        int index=filePath.lastIndexOf('/');
                        String fileName=filePath.substring(index+1);
                        attacht.append(fileName+"\n");
                    }
                }
            }
        }catch(Exception me){
            System.out.println("Exception in display mail : "+me);
        }
    }

    /*downloadAttachments() method gets the attachments of the selected mail, downloads it and saves it to the location specified by user*/
    public void downloadAttachments(){
        Message msg=inbox_messages[unseenMails+(4-selectedIndex)];
        int attachmentFlag=0;
        String fileLocation="";
        jc1=new JFileChooser();
        jc1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option=jc1.showOpenDialog(mailPanel);
        if(option==JFileChooser.APPROVE_OPTION)
        {
            fileLocation=jc1.getSelectedFile().toString();
        }
        try{
            if(msg.getContent() instanceof Multipart){
                Multipart mail_content=(Multipart)msg.getContent();
                int nParts=mail_content.getCount();
                for(int i=0;i<nParts;i++){
                    MimeBodyPart mail_part=(MimeBodyPart)mail_content.getBodyPart(i);
                    if(Part.ATTACHMENT.equalsIgnoreCase(mail_part.getDisposition())){
                        attachmentFlag=1;
                        String filePath=mail_part.getFileName();
                        int index=filePath.lastIndexOf('/');
                        String fileName=filePath.substring(index+1);
                        
                        File attachment=new File(fileLocation+"\\"+fileName);
                        if(attachment.createNewFile()){
                            mail_part.saveFile(fileLocation+"\\"+fileName);
                            System.out.println("file name : "+mail_part.getFileName());
                        }else{
                            System.out.println("File already exists.");
                        }
                        }
                }
                }
            else if(msg.getContent() instanceof String){
                System.out.println("\tBody : "+msg.getContent()+"\nfrom String");
            }else{}
        if(attachmentFlag==0){
            System.out.println("The mail does not have any attachments.");
        }  
                
        }catch(Exception e){
            System.out.println("Exception"+e);
        }
        finally{
            System.out.println("Finished successfully");
        }
    }

    //The currently selected mail is returned
    public Message getCurrentMail(){
        if(selectedIndex!=-1){
            Message msg=inbox_messages[unseenMails+(4-selectedIndex)];
            Message currentMessage=msg;
            return currentMessage;
        }else{
            return null;
        }
    }

    //override itemStateChanged to know which mail's checkbox has been selected
    public void itemStateChanged(ItemEvent ie){
        for(int i=0;i<5;i++){
            if(ie.getSource()==chooseMail[i]){
                if(ie.getStateChange()==1){
                    selectedIndex=i;
                    System.out.println("Selected Index : "+selectedIndex);
                }
            }
        }
    }

    //valueChanged() method is overrided to switch to the folder selected by the user in mailTypes list
    public void valueChanged(ListSelectionEvent le){
        try{
            if(mailTypes.getSelectedValue()=="Inbox"){
                inbox=store.getFolder("Inbox");                  
            }
            if(mailTypes.getSelectedValue()=="All Mails"){
                inbox=store.getFolder("[Gmail]/All Mail");
            }
            if(mailTypes.getSelectedValue()=="Drafts"){
                inbox=store.getFolder("[Gmail]/Drafts");
            }
            if(mailTypes.getSelectedValue()=="Sent Mails   "){
                inbox=store.getFolder("[Gmail]/Sent Mail");
            }
            if(mailTypes.getSelectedValue()=="Spam"){
                inbox=store.getFolder("[Gmail]/Spam");
            }
            if(mailTypes.getSelectedValue()=="Starred"){
                inbox=store.getFolder("[Gmail]/Starred");
            }
            if(mailTypes.getSelectedValue()=="Trash"){
                inbox=store.getFolder("[Gmail]/Bin");
            }
            if(!inbox.isOpen()){
            inbox.open(Folder.READ_WRITE);
            }
            inbox_messages=inbox.getMessages();
            unseenMails=inbox_messages.length;
            no_of_mails=inbox_messages.length;
            unreadMsg.setText("\t\t\tUnread Messages in "+inbox.getName()+"  :  "+inbox.getUnreadMessageCount());
            totalMsg.setText("Total Messages in "+inbox.getName()+"  :  "+inbox.getMessageCount());
            displayInbox();  
        }catch(Exception e){
        System.out.println("Exception : "+e);
        }
    }

    //actionPerformed is overridden to specify operations to be done when delete or refresh button is clicked
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==delete){
            try{
            Message currentMail=getCurrentMail();
            currentMail.setFlag(Flags.Flag.DELETED,true);
            System.out.println("Mail Deleted.");
            messageLabel[selectedIndex].setText("");
            }catch(MessagingException me){
                System.out.println("Exception : "+me);
            }
        }
        if(ae.getSource()==refresh){
            try{
                String folderName=inbox.getName();
                if(!(folderName.equals("Inbox"))){
                    folderName="[Gmail]/"+inbox.getName();
                }
                inbox.close(true);
                inbox=store.getFolder(folderName);
                inbox.open(Folder.READ_WRITE);
                inbox_messages=inbox.getMessages();
                unseenMails=inbox_messages.length;
                no_of_mails=inbox_messages.length;
                unreadMsg.setText("\t\tUnread Messages in "+inbox.getName()+" : "+inbox.getUnreadMessageCount());
                totalMsg.setText("Total Messages in "+inbox.getName()+" : "+inbox.getMessageCount());
                displayInbox();  
            }catch(Exception e){
                System.out.println("Exception : "+e);
            }
        }
    }
}