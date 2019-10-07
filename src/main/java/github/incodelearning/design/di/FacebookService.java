package github.incodelearning.design.di;

import com.google.inject.Singleton;

@Singleton
public class FacebookService implements MessageService {
    public boolean sendMessage(String msg, String recipient) {
        //some complex code to send Facebook message
        System.out.println("Message sent to Facebook user "+ recipient +" with message="+msg);
        return true;
    }
}
