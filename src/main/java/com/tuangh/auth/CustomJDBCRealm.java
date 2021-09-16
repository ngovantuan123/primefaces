package com.tuangh.auth;

import com.tuangh.Entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.sql.*;

public class CustomJDBCRealm extends JdbcRealm {
    public CustomJDBCRealm()  {
    }
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "jdbcRealm";
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        SimpleAuthenticationInfo info = null;

        User user = getUserInfo(userName);
        System.out.println("User name"+user.getUserName()+"==============");
        info = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(),getName());

        return info;
    }
    private User getUserInfo(String username){
        User user = new User();
        String sql = "select USERNAME,PASSWORD from TUAN_USER where USERNAME=?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection= DriverManager.getConnection("jdbc:oracle:thin:@192.168.59.8:1521:tcbsweb","halong","halong");
            //connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            set = statement.executeQuery();

            if(set.next()) {
                String username1 = set.getString(1);
                String password = set.getString(2);
                user.setPassword(password);
                user.setUserName(username1);
            }
        } catch (SQLException | ClassNotFoundException |UnsupportedClassVersionError  e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            closeAll(connection, set, statement);
        }
        return user;
    }

    private void closeAll(Connection conn, ResultSet set, PreparedStatement statement) {
        try {
            if(set != null) {
                set.close();
            }
            if(statement != null) {
                statement.close();
            }
            if(conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
