package com.tuangh.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.ldap.UnsupportedAuthenticationMechanismException;
import org.apache.shiro.realm.ldap.DefaultLdapRealm;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.subject.PrincipalCollection;

import javax.naming.AuthenticationNotSupportedException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class CustomLdapAuthzRealm extends DefaultLdapRealm {
    private static final String USER_SEARCH_FILTER = "(&(objectClass=*)(sn={0}))";

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String msg;
        try {
            Object principal = this.getLdapPrincipal(token);
            if(principal == null){
                return null;
            }
            AuthenticationInfo info = this.queryForAuthenticationInfo(token, this.getContextFactory());
            return info;
        } catch (AuthenticationNotSupportedException var5) {
            msg = "Unsupported configured authentication mechanism";
            throw new UnsupportedAuthenticationMechanismException(msg, var5);
        } catch (javax.naming.AuthenticationException var6) {
            throw new AuthenticationException("LDAP authentication failed.", var6);
        } catch (NamingException var7) {
            msg = "LDAP naming error while attempting to authenticate user.";
            throw new AuthenticationException(msg, var7);
        }
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        try {
            AuthorizationInfo info = this.queryForAuthorizationInfo(principals, this.getContextFactory());
            return info;
        } catch (NamingException var5) {
            String msg = "LDAP naming error while attempting to retrieve authorization for user [" + principals + "].";
            throw new AuthorizationException(msg, var5);
        }
    }
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
    @Override
    protected AuthorizationInfo queryForAuthorizationInfo(PrincipalCollection principals, LdapContextFactory ldapContextFactory) throws NamingException {
//        String username = (String) getAvailablePrincipal(principals);
//        LdapContext ldapContext = ldapContextFactory.getSystemLdapContext();
//        Set<String> roleNames;
//        try {
//            roleNames = getRoleNamesForUser(username, ldapContext);
//        } finally {
//            LdapUtils.closeContext(ldapContext);
//        }
        return null;
    }
    protected Set<String> getRoleNamesForUser(String username, LdapContext ldapContext) throws NamingException {
        Set<String> roleNames;
        roleNames = new LinkedHashSet<String>();

        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //SHIRO-115 - prevent potential code injection:
        String searchFilter = "(&(objectClass=*)(CN={0}))";
        Object[] searchArguments = new Object[]{username};

        NamingEnumeration answer = ldapContext.search("tuannv@vietbank.com.vn", searchFilter, searchArguments, searchCtls);

        while (answer.hasMoreElements()) {
            SearchResult sr = (SearchResult) answer.next();
            Attributes attrs = sr.getAttributes();

            if (attrs != null) {
                NamingEnumeration ae = attrs.getAll();
                while (ae.hasMore()) {
                    Attribute attr = (Attribute) ae.next();

                    if (attr.getID().equals("memberOf")) {

                        Collection<String> groupNames = LdapUtils.getAllAttributeValues(attr);
                        //Collection<String> rolesForGroups = getRoleNamesForGroups(groupNames);
                        //roleNames.addAll(rolesForGroups);
                    }
                }
            }
        }
        return roleNames;
    }
}
