package nl.quad.opentrivia;

import nl.quad.opentrivia.functionality.answerstore.service.AnswerStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTriviaConfiguration {

    @Bean
    public InitializingBean initializer(AnswerStore answerStore) {
        return answerStore::init;
    }
}
