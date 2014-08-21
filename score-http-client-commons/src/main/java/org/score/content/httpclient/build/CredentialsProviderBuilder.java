package org.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.impl.client.BasicCredentialsProvider;

import java.util.Locale;

public class CredentialsProviderBuilder {
    private String authType;
    private String username;
    private String password;
    private String host;
    private String port;
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;

    public CredentialsProviderBuilder setAuthType(String authType) {
        this.authType = authType;
        return this;
    }

    public CredentialsProviderBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public CredentialsProviderBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public CredentialsProviderBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public CredentialsProviderBuilder setPort(String port) {
        this.port = port;
        return this;
    }

    public CredentialsProviderBuilder setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    public CredentialsProviderBuilder setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    public CredentialsProviderBuilder setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
        return this;
    }

    public CredentialsProviderBuilder setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
        return this;
    }

    public CredentialsProvider buildCredentialsProvider() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        if (StringUtils.isEmpty(authType)) {
            authType = "basic";
        }

        if (!StringUtils.isEmpty(username)) {
            Credentials credentials;
            if (authType.equalsIgnoreCase(AuthSchemes.NTLM)) {
                String[] domainAndUsername = getDomainUsername(username);
                credentials = new NTCredentials(domainAndUsername[1], password, host, domainAndUsername[0]);
            } else {
                credentials = new UsernamePasswordCredentials(username, password);
            }
            credentialsProvider.setCredentials(new AuthScope(host, Integer.parseInt(port)), credentials);
        }

        if (!StringUtils.isEmpty(proxyUsername)) {
            int intProxyPort = 8080;
            if (!StringUtils.isEmpty(proxyPort)) {
                intProxyPort = Integer.parseInt(proxyPort);
            }
            credentialsProvider.setCredentials(new AuthScope(proxyHost, intProxyPort),
                    new UsernamePasswordCredentials(proxyUsername, proxyPassword));
        }

        return credentialsProvider;
    }

    private static String[] getDomainUsername(String username) {
        int atSlash = username.indexOf('/');
        if (atSlash == -1) {
            atSlash = username.indexOf('\\');
        }
        String usernameWithoutDomain = username.substring(atSlash + 1);
        String domain = ".";
        if (atSlash >= 0) {
            domain = username.substring(0, atSlash).toUpperCase(Locale.ENGLISH);
        }

        return new String[]{domain, usernameWithoutDomain};
    }
}