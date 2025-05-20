// jwt/JwtAuthenticationFilter.java
package com.capstone7.ptufestival.auth.jwt;

import com.capstone7.ptufestival.auth.model.User;
import com.capstone7.ptufestival.auth.repository.UserRepository;
import com.capstone7.ptufestival.common.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final int BEARER_PREFIX_LENGTH = "Bearer ".length();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX_LENGTH); // "Bearer " 이후

        try {
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UnauthorizedException("사용자 정보를 찾을 수 없습니다."));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, getAuthorities(user)); // 권한 추가
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
            return;
        } catch (JwtException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            return;
        } catch (Exception e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "인증 오류가 발생했습니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String body = String.format("{\"data\":null,\"message\":\"%s\",\"statusCode\":%d}", message, status);
        response.getWriter().write(body);
    }

    // Role에서 권한 목록을 추출하여 GrantedAuthority로 변환하는 메소드
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return "ROLE_" + user.getRole().name();
            }
        });

        return collection;
    }
}
