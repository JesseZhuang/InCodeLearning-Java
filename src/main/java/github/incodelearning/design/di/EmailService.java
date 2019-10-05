package github.incodelearning.design.di;

import com.google.inject.Singleton;

@Singleton
public class EmailService implements MessageService {
    public boolean sendMessage(String msg, String recipient) {
        //some fancy code to send email
        System.out.println("Email Message sent to " + recipient + " with message=" + msg);
        return true;
    }
}
