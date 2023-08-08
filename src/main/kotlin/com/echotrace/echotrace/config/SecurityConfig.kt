package com.echotrace.echotrace.config

import com.echotrace.echotrace.repository.UserRepositoryInterface
import com.echotrace.echotrace.service.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtService: JwtService,
    private val userRepository: UserRepositoryInterface
) {


    @Bean
    fun jwtFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors {}
            .securityMatcher(RequestMatcher { request ->
                val path = request.requestURI
                path.startsWith("/user") || (path.startsWith("/event") && (request.method == "GET" || request.method == "OPTIONS"))
            })
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/user/api-token").authenticated()
                it.requestMatchers("/user/validate").authenticated()
                it.requestMatchers("/event").authenticated()
                it.anyRequest().permitAll()
            }
            .addFilterBefore(JwtFilter(jwtService), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }


    @Bean
    fun apiTokenFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors {}
            .securityMatcher(RequestMatcher { request ->
                val path = request.requestURI
                path.startsWith("/event") && (request.method == "POST" || request.method == "OPTIONS")
            })
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/event").authenticated()
                it.anyRequest().permitAll()
            }
            .addFilterBefore(ApiTokenFilter(userRepository), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.applyPermitDefaultValues()
//        configuration.allowedOrigins = listOf("http://localhost:5173")
//        configuration.allowedMethods = listOf("GET", "POST")
//        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}