package com.echotrace.echotrace.config

import com.echotrace.echotrace.repository.UserRepositoryInterface
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiTokenFilter(
    private val userRepository: UserRepositoryInterface
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authHeader = request.getHeader("Authorization") ?: ""
        validateHeader(authHeader)
        filterChain.doFilter(request, response)
    }

    private fun validateHeader(header: String) {
        if (!validateAuth(header)) return

        val token = header.substring(4)
        val user = userRepository.findByApiToken(token) ?: return
        val auth = UsernamePasswordAuthenticationToken(user, null, emptyList())
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun validateAuth(authHeader: String): Boolean =
        authHeader.startsWith("Api ") && authHeader.length > 4
}
