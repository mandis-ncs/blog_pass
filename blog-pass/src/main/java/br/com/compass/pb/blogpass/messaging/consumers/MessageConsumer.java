package br.com.compass.pb.blogpass.messaging.consumers;



import br.com.compass.pb.blogpass.entities.Post;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageConsumer {

    @JmsListener(destination = "post_population")
    public void receiveMessage(String messagePost) throws JsonParseException, JsonMappingException, IOException {
        System.out.println(messagePost);
        ObjectMapper mapper = new ObjectMapper();
        Post post = mapper.readValue(messagePost, Post.class);
        System.out.println("This is the post id send:" + post.getId() + "pay atention !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
