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
        principal=principal+"@vietbank.com.vn";
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

    protected String getUserDn(String principal) throws IllegalArgumentException, IllegalStateException {
        if (!StringUtils.hasText(principal)) {
            throw new IllegalArgumentException("User principal cannot be null or empty for User DN construction.");
        } else {
            String prefix = this.getUserDnPrefix();
            String suffix = this.getUserDnSuffix();
            if (prefix == null && suffix == null) {
                //log.debug("userDnTemplate property has not been configured, indicating the submitted AuthenticationToken's principal is the same as the User DN.  Returning the method argument as is.");
                return principal;
            } else {
                int prefixLength = prefix != null ? prefix.length() : 0;
                int suffixLength = suffix != null ? suffix.length() : 0;
                StringBuilder sb = new StringBuilder(prefixLength + principal.length() + suffixLength);
                if (prefixLength > 0) {
                    sb.append(prefix);
                }

                sb.append(principal);
                if (suffixLength > 0) {
                    sb.append(suffix);
                }

                return sb.toString();
            }
        }

    }
}
