package app.rafo.bs_personal_finance_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración web para exponer recursos estáticos, como la carpeta "uploads".
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String UPLOADS_DIR = "uploads/";

    /**
     * Configura un ResourceHandler para exponer la carpeta "uploads" públicamente.
     *
     * @param registry Registro de manejadores de recursos.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Exponer la carpeta "uploads" como recurso estático
        registry
                .addResourceHandler("/uploads/**") // Ruta pública
                .addResourceLocations("file:" + UPLOADS_DIR); // Ruta local
    }
}
