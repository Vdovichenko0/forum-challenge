package com.forum.security;

import com.forum.accounting.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	final CustomWebSecurity webSecurity;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/account/register", "/forum/posts/**").permitAll()
				// доступ только у админа
				.requestMatchers("/account/user/{login}/role/{role}").hasRole(Role.ADMINISTRATOR.name())

				// разрешаем только владельцу
				.requestMatchers(HttpMethod.PUT, "/account/user/{login}")
					.access(new WebExpressionAuthorizationManager("#login == authentication.name"))

				// удаление юзера доступ=владелец+админ
				.requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
				.access(new WebExpressionAuthorizationManager(
						"#login == authentication.name or hasRole('ADMINISTRATOR')"))

						.requestMatchers(HttpMethod.POST, "/forum/post/{author}")
						.access(new WebExpressionAuthorizationManager("#author == authentication.name"))
						//add comment
						.requestMatchers(HttpMethod.PUT, "/forum/post/{id}/comment/{author}")
						.access(new WebExpressionAuthorizationManager("#author == authentication.name"))

//						//remove comment
						.requestMatchers(HttpMethod.DELETE, "/forum/post/{id}/comment/{author}")
						.access(new WebExpressionAuthorizationManager("#author == authentication.name or hasRole('ADMINISTRATOR')"))

						//updatePost
						.requestMatchers(HttpMethod.PUT, "/forum/post/{id}")
						.access((authentication, context)
								-> new AuthorizationDecision(webSecurity.checkPostAuthor
								(context.getVariables().get("id"), authentication.get().getName())))
						//delete post
						//collection - getAuthorities
						.requestMatchers(HttpMethod.DELETE, "/forum/post/{id}")
						.access((authentication, context)-> {
							boolean checkAuthor = webSecurity.checkPostAuthor(context.getVariables().get("id"), authentication.get().getName());
							boolean checkModerator =  context.getRequest().isUserInRole("MODERATOR");
							return new AuthorizationDecision(checkAuthor || checkAuthor);
						})

				.anyRequest().authenticated()
		);

		return http.build();
	}
}
