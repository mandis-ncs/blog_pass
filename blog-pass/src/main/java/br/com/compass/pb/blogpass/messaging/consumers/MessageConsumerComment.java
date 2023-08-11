package br.com.compass.pb.blogpass.messaging.consumers;



import br.com.compass.pb.blogpass.entities.Post;
import br.com.compass.pb.blogpass.services.PostService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageConsumerComment {

    private final PostService postService;

    public MessageConsumerComment(PostService postService) {
        this.postService = postService;
    }

    @JmsListener(destination = "comment_population")
    public void receiveMessage(String messageComment) throws JsonParseException, JsonMappingException, IOException {
        if (messageComment.startsWith("Post")) {
            String[] parts = messageComment.split(" ");
            Long postId = Long.parseLong(parts[1]);

            // calling method to populate Comment
            postService.populateCommentByPostId(postId);
        }
    }
}
