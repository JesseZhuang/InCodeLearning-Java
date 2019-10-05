package github.incodelearning.design.di;

import com.google.inject.AbstractModule;

public class MessageServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        //bind MessageService to Facebook Message implementation
        bind(MessageService.class).to(FacebookService.class);
    }
}
