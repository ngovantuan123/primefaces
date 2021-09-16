package com.tuangh.auth;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.ldap.DefaultLdapRealm;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.util.StringUtils;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

public class CustomLdapAuthzRealm extends DefaultLdapRealm {
   @Override
    protected AuthenticationInfo queryForAuthenticationInfo(AuthenticationToken token, LdapContextFactory ldapContextFactory) throws NamingException {
        Object principal = token.getPrincipal();
        Object credentials = token.getCredentials();
        //log.debug("Authenticating user '{}' through LDAP", principal);
        principal = this.getLdapPrincipal(token);
        LdapContext ctx = null;

        AuthenticationInfo var6;
        try {
            ctx = ldapContextFactory.getLdapContext(principal, credentials);
            var6 = this.createAuthenticationInfo(token, principal, credentials, ctx);
        } finally {
            LdapUtils.closeContext(ctx);
        }

        return var6;
    }
}
