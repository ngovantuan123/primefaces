package com.tuangh.auth;

import com.tuangh.Entity.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class TestLdapConnection {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        ByteSource salt  = secureRandomNumberGenerator.nextBytes();
        String strSalt = salt.toString();
        String hashedPasswordBase64 = new Sha256Hash("123",strSalt.getBytes(StandardCharsets.UTF_8) , 1024).toBase64();
        System.out.println(hashedPasswordBase64);
        Connection connection=TestLdapConnection.getCon();
//        PreparedStatement statement = connection.prepareStatement("select USERNAME,PASSWORD from TUAN_USER where USERNAME=?");
        PreparedStatement statement = connection.prepareStatement("update tuan_user set salt=?,password=?");
        statement.setString(1,strSalt);
        statement.setString(2,hashedPasswordBase64);
statement.executeUpdate();

//        statement.setString(1, "tuangh");
//        ResultSet set = statement.executeQuery();
//        User user=new User();
//        if(set.next()) {
//            String username1 = set.getString(1);
//            String password = set.getString(2);
//            user.setPassword(password);
//            user.setUserName(username1);
//        }
statement.close();
        connection.close();
//        LdapContext ctx = null;
//        try{
//            Hashtable env = new Hashtable();
//            env.put(Context.INITIAL_CONTEXT_FACTORY,  "com.sun.jndi.ldap.LdapCtxFactory");
//            env.put(Context.SECURITY_AUTHENTICATION, "Simple");
//            //it can be <domain\\userid> something that you use for windows login
//            //it can also be
//            env.put(Context.SECURITY_PRINCIPAL, "tuannv@vietbank.com.vn");
//            env.put(Context.SECURITY_CREDENTIALS, "password");
//            //in following property we specify ldap protocol and connection url.
//            //generally the port is 389
//            env.put(Context.PROVIDER_URL, "ldap://192.168.25.1:389");
//            ctx = new InitialLdapContext(env, null);
//            System.out.println("Connection Successful.");
//        }catch(NamingException nex){
//            System.out.println("LDAP Connection: FAILED");
//            nex.printStackTrace();
//        }
    }
    public static Connection getCon() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con= DriverManager.getConnection(
                "jdbc:oracle:thin:@//192.168.59.8:1521/tcbsweb","halong","halong");
        return con;
    }

}
