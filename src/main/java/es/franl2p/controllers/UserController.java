package es.franl2p.controllers;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;

import com.google.gson.Gson;

import es.franl2p.services.UserService;
import es.franl2p.utils.JsonTransformer;
import es.franl2p.model.User;

public class UserController {
	public UserController(final UserService userService) {
		// Método para tratar los gets de /users
		get("/users", (request, response) -> userService.getAllUsers(), new JsonTransformer());


// Método para tratar los posts de /users (Creación de usuarios)
post("/users", (req, res) -> {
	// Se cargan los parámetros de la query (URL)
	String name = req.queryParams("name");
	String email = req.queryParams("email");
	String body = req.body();
	
	// Convertimos de JSON a objeto de la clase User
	es.franl2p.model.User user = new Gson().fromJson(body, User.class);
	if (user != null) {
		System.out.println("---- Usuario cargado correctamente.");
		name = user.getName();
		email = user.getEmail();
	}

	System.out.println("---- Datos del usuario.");
	System.out.println("---- name: " + name);
	System.out.println("---- email: " + email);
	
	return userService.createUser(name, email);
}, new JsonTransformer());

		// Filtro para convertir la salida a formato JSON
		after((request, response) -> {
			response.type("application/json");
		});
	}
}
