package telran.company.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

	Logger LOG = LoggerFactory.getLogger(SecurityConfigurations.class);
	@Value("${app.admin.username}")
	String admin;	
	@Value("${app.admin.password}")
	String adminPassword;
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeHttpRequests(requests ->
				requests.requestMatchers(HttpMethod.GET).authenticated()
					.requestMatchers("/employees/salary").hasRole("ACCOUNTER")
					.requestMatchers("/employees/age").anonymous()
					.requestMatchers("/employees/month").anonymous()   
					.requestMatchers("/accounts/**").hasRole("ADMIN")
					.anyRequest().hasRole("ADMIN_COMPANY"))
			.httpBasic();
		return http.build();
	}
	
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
			
	@Bean
	UserDetailsManager userDetailsService(PasswordEncoder bCryptPasswordEncoder) {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();			
		manager.createUser(User.withUsername(admin)
				.password(bCryptPasswordEncoder.encode(adminPassword))
				.roles("ADMIN")
				.build());
		LOG.info("added administrator: {}", manager.loadUserByUsername(admin)); 
		return manager;
	}   
	 
}

