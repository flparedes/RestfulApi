package es.franl2p.controllers;

import static spark.Spark.after;
import static spark.Spark.get;

import es.franl2p.services.UserService;
import es.franl2p.utils.JsonTransformer;

public class UserController {
	public UserController(final UserService userService) {
		// Método para tratar los gets de /users
		get("/users", (request, response) -> userService.getAllUsers(), new JsonTransformer());

		// Filtro para convertir la salida a formato JSON
		after((request, response) -> {
			response.type("application/json");
		});
	}
}
