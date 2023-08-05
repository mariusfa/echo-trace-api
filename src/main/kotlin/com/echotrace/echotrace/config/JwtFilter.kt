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
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authHeader = request.getHeader("Authorization") ?: ""
        validateHeader(authHeader)
        response.setHeader("Access-Control-Allow-Origin", "*")
        filterChain.doFilter(request, response)
    }

    private fun validateHeader(header: String) {
        if (!validateAuth(header)) return

        val token = header.substring(7)
        val user = jwtService.getUser(token) ?: return
        val auth = UsernamePasswordAuthenticationToken(user, null, emptyList())
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun validateAuth(authHeader: String): Boolean =
        authHeader.startsWith("Bearer ") && authHeader.length > 7
}