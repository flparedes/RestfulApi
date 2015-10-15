package es.franl2p;

import es.franl2p.controllers.UserController;
import es.franl2p.services.UserService;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        new UserController(new UserService());
    }
}
