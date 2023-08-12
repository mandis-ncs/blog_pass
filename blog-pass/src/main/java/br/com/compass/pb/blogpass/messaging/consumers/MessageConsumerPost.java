package br.com.compass.pb.blogpass.messaging.consumers;



import br.com.compass.pb.blogpass.messaging.producers.MessageProducerComment;
import br.com.compass.pb.blogpass.services.PostService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageConsumerPost {

    private final PostService postService;
    private final MessageProducerComment messageProducerComment;

    public MessageConsumerPost(PostService postService, MessageProducerComment messageProducerComment) {
        this.postService = postService;
        this.messageProducerComment = messageProducerComment;
    }

    @JmsListener(destination = "post_population")
    public void receiveMessage(String messagePost) throws JsonParseException, JsonMappingException, IOException {
        if (messagePost.startsWith("Post")) {
            String[] parts = messagePost.split(" ");
            Long postId = Long.parseLong(parts[1]);

            // calling method to populate Post
            postService.populatePostById(postId);

            // sending to the second queue
            String secondMessage = "Post " + postId + " postok";
            messageProducerComment.sendMessageToDestination("comment_population", secondMessage);
        }
        else {
            throw new RuntimeException("Failed to process post in queue post_population. Try again.");
        }
    }
}
