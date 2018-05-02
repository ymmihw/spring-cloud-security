package com.ymmihw.spring.cloud.security.introduction.auth.client.filters;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Configuration
public class SimpleFilter extends ZuulFilter {


  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    RequestContext context = RequestContext.getCurrentContext();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
    String tokenValue = details.getTokenValue();
    context.addZuulRequestHeader("Authorization", "bearer " + tokenValue);

    return null;
  }

}
