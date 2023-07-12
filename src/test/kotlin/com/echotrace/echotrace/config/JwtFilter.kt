package com.echotrace.echotrace.config

import com.echotrace.echotrace.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtService: JwtService
): OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authHeader = request.getHeader("Authorization") ?: ""
        if (!validAuthHeader(authHeader)) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)
        runCatching {
            val user = jwtService.getUser(token)
            val auth = UsernamePasswordAuthenticationToken(user, null, emptyList())
            SecurityContextHolder.getContext().authentication = auth
        }.onFailure {
            filterChain.doFilter(request, response)
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun validAuthHeader(authHeader: String): Boolean =
        authHeader.startsWith("Bearer ") && authHeader.length > 7
}