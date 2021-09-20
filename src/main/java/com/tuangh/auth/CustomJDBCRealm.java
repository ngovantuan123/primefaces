package com.tuangh.auth;

import com.tuangh.Entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.sql.*;
import java.util.Base64;

public class CustomJDBCRealm extends JdbcRealm {
    private User user;
    public CustomJDBCRealm()  {
    }
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "jdbcRealm";
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(user.getRole());
        info.addStringPermissions(user.getPermisstions());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        SimpleAuthenticationInfo info = null;
        User user = getUserInfo(userName);
        info = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(),getName());
        info.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()));
        return info;
    }
    private User getUserInfo(String username){
        this.user = new User();
        String sql = "select t.USERNAME,t.PASSWORD,t.ROLE,p.permission_code,t.SALT from TUAN_USER t INNER JOIN TUAN_USER_PERMISSION p ON t.ROLE = p.USER_ROLE where t.USERNAME=?";
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

            while(set.next()) {
                String username1 = set.getString(1);
                String password = set.getString(2);
                if(user.getUserName() == null && user.getUserName()==null){
                    user.setPassword(password);
                    user.setUserName(username1);
                    user.setRole(set.getString(3));
                    user.setSalt(set.getString(5));
                }
                user.getPermisstions().add(set.getString(4));
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
