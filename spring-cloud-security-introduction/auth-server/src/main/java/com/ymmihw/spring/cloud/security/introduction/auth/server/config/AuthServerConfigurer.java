package com.ymmihw.spring.cloud.security.introduction.auth.server.config;

import java.security.KeyPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfigurer extends AuthorizationServerConfigurerAdapter {

  @Value("${jwt.certificate.store.file}")
  private Resource keystore;

  @Value("${jwt.certificate.store.password}")
  private String keystorePassword;

  @Value("${jwt.certificate.key.alias}")
  private String keyAlias;

  @Value("${jwt.certificate.key.password}")
  private String keyPassword;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    clients.inMemory().withClient("authserver")
        .secret(passwordEncoder.encode("passwordforauthserver"))
        .redirectUris("http://localhost:8080/")
        .authorizedGrantTypes("authorization_code", "refresh_token").scopes("myscope")
        .autoApprove(true).accessTokenValiditySeconds(30).refreshTokenValiditySeconds(1800);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.accessTokenConverter(jwtAccessTokenConverter());
  }

  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    KeyStoreKeyFactory keyStoreKeyFactory =
        new KeyStoreKeyFactory(keystore, keystorePassword.toCharArray());
    KeyPair keyPair = keyStoreKeyFactory.getKeyPair(keyAlias, keyPassword.toCharArray());
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setKeyPair(keyPair);
    return converter;
  }
}
