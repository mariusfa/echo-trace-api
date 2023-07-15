package com.echotrace.echotrace.config

import com.echotrace.echotrace.repository.UserRepository
import com.echotrace.echotrace.repository.UserRepositoryInterface
import com.echotrace.echotrace.service.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtService: JwtService,
    private val userRepository: UserRepositoryInterface
) {

    @Bean
    fun jwtFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher(RequestMatcher { request ->
                val path = request.requestURI
                path.startsWith("/user") || (path.startsWith("/event")&& request.method == "GET")})
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/user/api-token").authenticated()
                it.requestMatchers("/user/refresh").authenticated()
                it.requestMatchers(HttpMethod.GET,"/event").authenticated()
                it.anyRequest().permitAll()
            }
            .addFilterBefore(JwtFilter(jwtService), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun apiTokenFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher(RequestMatcher { request ->
                val path = request.requestURI
                path.startsWith("/event") && request.method == "POST"})
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.POST,"/event").authenticated()
                it.anyRequest().permitAll()
            }
            .addFilterBefore(ApiTokenFilter(userRepository), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}