package ldapimpl;

import java.util.*;

//Import JNDI Interface
import javax.naming.directory.*;
import javax.naming.Context;
import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;

//Creation of LDAP class
public class LDAP{
    DirContext connection;              //Declaring connection object to establish connection with LDAP server using admin credentials

    //LDAP class constructor
    public LDAP(){
        //Initializing environment variable with admin credentials for establishing connection
        Hashtable environment=new Hashtable();
    
        environment.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL,"ldap://localhost:10389");
        environment.put(Context.SECURITY_AUTHENTICATION,"simple");
        environment.put(Context.SECURITY_PRINCIPAL,"uid=admin,ou=system");
        environment.put(Context.SECURITY_CREDENTIALS,"secret");
    try{
        connection=new InitialDirContext(environment);       //Initializing connection with environment variable
        System.out.println("Connected to ldap server..");
    }catch(AuthenticationNotSupportedException ae){
        System.out.println("Authentication not supported by server");
    }
    catch(AuthenticationException ae){
        System.out.println("Incorrect username or password");
    }
    catch(NamingException ne){
        System.out.println("Error while creating context"+ne);
    }
    }

    /*searchUser(username) method searches for the entry using admin connection in the LDAP database by forming the DN with the mail id. If the 
    entry is found the DN is returned. Attribute values such as common name, mail id and password are extracted from the database.*/
    public String searchUser(String mail_id){
        String distName="";
        SearchControls search_cntrl=new SearchControls();
        search_cntrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String[] req_attrID={"cn","mail","userPassword"};
        search_cntrl.setReturningAttributes(req_attrID);

        String search_filter="(&(objectClass=inetOrgPerson)(mail="+mail_id+"))";
        
        try{
            NamingEnumeration search_results=connection.search("",search_filter,search_cntrl);
            if(search_results.hasMore()){
                System.out.println("User found");
                SearchResult found_result=(SearchResult)search_results.next();
                Attributes attr=found_result.getAttributes();
                distName=found_result.getNameInNamespace();
                String found_mail=attr.get("mail").toString();
                String found_pwd=attr.get("userPassword").toString();
                System.out.println("Search Successful!! User Found\n"+found_mail+"\n"+found_pwd+" (hashed) ");
            }
        }catch(NamingException ne){
            System.out.println(ne);
        }
        return distName;
    }

    /*authenticateUser(username,password) method authenticates the user by establishing connection using the credentials entered by the user.
    First whether the user exists or not has been checked using the searchUser(username) method.Then try to establish the connection. Connection is 
    established only if the details are correct. Result is returned indicating whether the authentication is successful or not.*/
    public boolean authenticateUser(String mail_id,String pwd){
        boolean flag=false;
        String distName=searchUser(mail_id);

        if(!(distName.equals(""))){
            try {  
                    DirContext connection1;
                    Hashtable environment1=new Hashtable();
                    environment1.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
                    environment1.put(Context.PROVIDER_URL,"ldap://localhost:10389");
                    environment1.put(Context.SECURITY_AUTHENTICATION,"simple");
                    environment1.put(Context.SECURITY_PRINCIPAL,distName);
                    environment1.put(Context.SECURITY_CREDENTIALS,pwd);

                    connection1=new InitialDirContext(environment1);
                    System.out.println("User Details are correct!!");
                    flag=true;
                    connection1.close();
                }catch(AuthenticationNotSupportedException ae){
                    System.out.println("Authentication not supported by server");
                }
                catch(AuthenticationException ae){
                    System.out.println("Incorrect username or password");
                }
                catch(NamingException ne){
                    System.out.println("Error while creating context");
                }
            }
        else{
            System.out.println("User not found");
        }
        return flag;
    }

    /*addUser(commonName, surname, username, password) method creates a new entry in the LDAP database using the admin connection. The DN
    is formed by using the mail id. Create objectClass attribute with value as inetOrgPerson. Other attibutes are created by putting 
    attribute names and values inyo BasicAttributes object.*/
    public boolean addUser(String c_name,String s_name,String mail_id,String pwd){
        boolean flag=false;
        Attributes attr=new BasicAttributes();
        Attribute objClass=new BasicAttribute("objectClass");
        objClass.add("inetOrgPerson");
        attr.put(objClass);
        attr.put("cn",c_name);
        attr.put("sn",s_name);
        attr.put("mail",mail_id);
        attr.put("userPassword",pwd);
        try{
            connection.createSubcontext("mail="+mail_id+",ou=users,ou=system",attr);
            flag=true;
            System.out.println("Entry added Successfully!!");
        }catch(NamingException ne){
            System.out.println("Failed !! "+ne);
        }
        return flag;
    }

    /*deleteUser(username) deletes the existing entry from the database using the admin connection. First check whether the entry
    exists using the searchUser(username) method. Entry is deleted using the DN returned from searchUser method. */
    public void deleteUser(String mail_id){
        String distName=searchUser(mail_id);
        if(!(distName.equals(""))){
            try{
                connection.destroySubcontext(distName);
                System.out.println("Account removed");
            }catch(NamingException ne){
                System.out.println(ne);
            }
        }
    }

    /*modifyUserPwd(username,password) method changes the value of userPassword attribute of the existing entry in the database. */
    public void modifyUserPwd(String mail_id,String new_pwd){
        String distName=searchUser(mail_id);
        if(!(distName.equals(""))){
            try{
                ModificationItem[] modification_item=new ModificationItem[1];
                Attribute pwdAttr=new BasicAttribute("userPassword",new_pwd);
                modification_item[0]=new ModificationItem(DirContext.REPLACE_ATTRIBUTE,pwdAttr);
                connection.modifyAttributes(distName, modification_item);
                System.out.println("Password changed successfully!!");
            }catch(Exception e){
                System.out.println("Request Failed !!");
            }
        }else{
            System.out.println("User not found.");
        }
    }   

    /*modifyUserDetails(username,newCommonName,newSurname,password) method changes the values of common name and surname
     attributes of the existing entry in the database. */
    public void modifyUserDetails(String mail_id,String cname,String sname,String pwd){
        String distName=searchUser(mail_id);
        if(!(distName.equals(""))){
            ModificationItem[] mod_items=new ModificationItem[2];
            mod_items[0]=new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("cn",cname));
            mod_items[1]=new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("sn",sname));
            //mod_items[2]=new ModificationItem(DirContext.REPLACE_ATTRIBUTE,new BasicAttribute("userPassword",pwd));
            try{
                connection.modifyAttributes(distName, mod_items);
                System.out.println("Details updated successfully!!");
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }
}
