package com.tuangh.auth;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;


public class CustomRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String principalUsername = (String) getAvailablePrincipal(principalCollection);
        if(principalUsername.equals("tuangh")){
            info.addStringPermission("product:view");
            info.addRole("admin");
        }else{
            info.addStringPermission("user");
            info.addRole("user");
        }
        return info ;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Authorizer aut;
        JdbcRealm realm;
        WildcardPermission wilcard;
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String foundPassword = String.valueOf(upToken.getPassword());
        String foundUserName = upToken.getUsername();
//        if (!foundUserName.equals("tuangh") || !foundPassword.equals("admin")) {
//            throw new UnknownAccountException("No account found for username  " + upToken.getUsername());
//        }
        if( !foundUserName.equals("user") || !foundPassword.equals("user")){
            throw new UnknownAccountException("No account found for username  " + upToken.getUsername());
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(upToken.getPrincipal(), foundPassword, getName());

        return info;
    }
}
