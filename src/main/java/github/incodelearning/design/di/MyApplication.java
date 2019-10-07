package github.incodelearning.design.di;

public class MyApplication {

    private MessageService service;

    //constructor based injector
//	@Inject
//	public MyApplication(MessageService svc){
//		this.service=svc;
//	}

    //setter method injector
    @javax.inject.Inject
    public void setService(MessageService svc) {
        this.service = svc;
    }

    public boolean sendMessage(String msg, String rec) {
        //some business logic here
        return service.sendMessage(msg, rec);
    }
}
