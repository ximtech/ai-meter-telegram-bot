package aimeter.telegram.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.web.servlet.function.RequestPredicates.accept;

@Configuration
public class RouterConfig {
    
    @Bean
    public RouterFunction<ServerResponse> getRootRedirectRout() {
        return RouterFunctions.route()
                .GET("/", accept(MediaType.TEXT_PLAIN), 
                        (ServerRequest request) -> 
                                ServerResponse.permanentRedirect(URI.create("/actuator/info")).body("")).build();
    }
}
