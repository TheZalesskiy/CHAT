package client;

/**
 * ClientGuiController class
 * date: 22.05.2016
 *
 * @autor TheZalesskie
 */
public class ClientGuiController extends Client {
    // Create and initialize the field , responsible for the model ClientGuiModel model.
    private ClientGuiModel model = new ClientGuiModel();
    //Create and initialize the field , responsible for the presentation of ClientGuiView view.
    private ClientGuiView view = new ClientGuiView(this);

    public static void main(String[] args) {
        new ClientGuiController().run();
    }
    /** methods **/
    // should create and return an object of type GuiSocketThread.
    @Override
    protected SocketThread getSocketThread() {
        return new GuiSocketThread();
    }
    // Must receive object SocketThread through getSocketThread () method and cause him run () method .
    // Understand why there is no need to call the run method in a separate thread , as we did for the console client.
    @Override
    public void run() {
        getSocketThread().run();
    }
    //getters
    @Override
    protected String getServerAddress() {
        return view.getServerAddress();
    }

    @Override
    protected int getServerPort() {
        return view.getServerPort();
    }

    @Override
    protected String getUserName() {
        return view.getUserName();
    }

    public ClientGuiModel getModel(){
        return model;
    }


    /** inner class GuiSocketThread**/
    //Add GuiSocketThread inner class inherits from SocketThread. GuiSocketThread class must be public.
    public class GuiSocketThread extends SocketThread {
        // shall establish a new message from the model and cause upgrade output messages in the view.
        @Override
        protected void processIncomingMessage(String message) {
            model.setNewMessage(message);
            view.refreshMessages();
        }
        // I must add a new user in the model output and cause upgrade users from display.
        @Override
        protected void informAboutAddingNewUser(String userName) {
            model.addUser(userName);
            view.refreshUsers();
        }
        // must remove the user from the model output and cause upgrade users from display.
        @Override
        protected void informAboutDeletingNewUser(String userName) {
            model.deleteUser(userName);
            view.refreshUsers();
        }
        // should cause a similar method in the view.
        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            view.notifyConnectionStatusChanged(clientConnected);
        }

    }

}
