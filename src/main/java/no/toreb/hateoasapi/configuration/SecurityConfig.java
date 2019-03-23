package no.toreb.hateoasapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String user;
    private final String password;
    private final List<String> roles;

    @Autowired
    public SecurityConfig(@Value("${spring.security.user.name}") final String user,
                          @Value("${spring.security.user.password}")final String password,
                          @Value("${spring.security.user.roles}") final List<String> roles) {
        this.user = user;
        this.password = password;
        this.roles = new ArrayList<>(roles);
    }

    @Override
    protected void configure(final HttpSecurity security) throws Exception {
        security.csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
            .passwordEncoder(encoder)
            .withUser(user)
            .password(encoder.encode(password))
            .roles(roles.toArray(new String[0]));
    }
}
