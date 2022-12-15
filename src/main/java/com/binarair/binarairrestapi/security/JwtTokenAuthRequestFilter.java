package com.binarair.binarairrestapi.security;

import com.binarair.binarairrestapi.config.JwtTokenConfiguration;
import com.binarair.binarairrestapi.service.impl.JwtTokenAuthService;
import com.binarair.binarairrestapi.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenAuthRequestFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(JwtTokenAuthRequestFilter.class);

    private final JwtTokenConfiguration jwtTokenConfiguration;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtTokenAuthService jwtTokenAuthService;

    @Autowired
    public JwtTokenAuthRequestFilter(JwtTokenConfiguration jwtTokenConfiguration, JwtTokenUtil jwtTokenUtil, JwtTokenAuthService jwtTokenAuthService) {
        this.jwtTokenConfiguration = jwtTokenConfiguration;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtTokenAuthService = jwtTokenAuthService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addIntHeader("Access-Control-Max-Age", 10);

        log.info("Calling do filter internal");
        String authorization = request.getHeader(jwtTokenConfiguration.getAuthorizationHttpHeader());
        log.info("Authorization {}", authorization );
        String token = null;
        String userName = null;

        if(authorization != null && authorization.startsWith(jwtTokenConfiguration.getTokenPrefix())) {
            token = authorization.substring(7);
            log.info("token {} ", token);
            userName = jwtTokenUtil.getUsernameFromToken(token);
            log.info("email {} ", userName);
        }

        if (null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails
                    = jwtTokenAuthService.loadUserByUsername(userName);

            log.info("User details {} with token {} ", userDetails.getUsername(), token);
            if(jwtTokenUtil.validateToken(token,userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }
        filterChain.doFilter(request, response);
    }

}
