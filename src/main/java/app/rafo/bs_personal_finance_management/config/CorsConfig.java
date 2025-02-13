package app.rafo.bs_personal_finance_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 🔥 Permitir cualquier frontend en Render y Localhost
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:[*]",
                "https://*.onrender.com" // Permite cualquier subdominio en Render
        ));

        // 🔥 Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 🔥 Headers permitidos
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // 🔥 Exponer headers importantes
        config.setExposedHeaders(List.of("Authorization"));

        // 🔥 Permitir credenciales (necesario si usas autenticación)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
