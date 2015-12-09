package es.franl2p.controllers;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;

import com.google.gson.Gson;

import es.franl2p.services.UserService;
import es.franl2p.utils.JsonTransformer;
import es.franl2p.model.User;

public class UserController {
	public UserController(final UserService userService) {
		// M�todo para tratar los gets de /users
		get("/users", (request, response) -> userService.getAllUsers(), new JsonTransformer());

		// M�todo para tratar los gets de /users/:id (Datos del usuario dado)
		get("/users/:idUser", (req, res) -> {
			String idUser = req.params(":idUser");
			User user = userService.getUser(idUser);
			if (user != null) {
				return user;
			}

			res.status(400);
			return "No user with id '" + idUser + "' found";
		}, new JsonTransformer());

		// M�todo para tratar los posts de /users (Creaci�n de usuarios)
		post("/users", (req, res) -> {
			// Se cargan los par�metros de la query (URL)
			String name = req.queryParams("name");
			String email = req.queryParams("email");
			String body = req.body();
			
			// Convertimos de JSON a objeto de la clase User
			es.franl2p.model.User user = new Gson().fromJson(body, User.class);
			if (user != null) {
				name = user.getName();
				email = user.getEmail();
			}
			
			return userService.createUser(name, email);
		}, new JsonTransformer());

		// M�todo para tratar los put de /users/:idUser (Modificaci�n de datos de usuarios)
		put("/users/:idUser", (req, res) -> {
			String idUser = req.params(":idUser");
			
			// Se cargan los par�metros de la query (URL)
			String name = req.queryParams("name");
			String email = req.queryParams("email");

			User user = userService.getUser(idUser);
			if (user != null) {				
				return userService.updateUser(idUser, name, email);
			}

			res.status(400);
			return "No user with id '" + idUser + "' found";
		}, new JsonTransformer());

		// M�todo para tratar los delete de /users/:idUser (Eliminar usuario)
		delete("/users/:idUser", (req, res) -> {
			String idUser = req.params(":idUser");

			User user = userService.getUser(idUser);
			if (user != null) {
				userService.deleteUser(idUser);
				res.status(200);
				return "User with id '" + idUser + "' deleted";
			}

			res.status(400);
			return "No user with id '" + idUser + "' found";
		}, new JsonTransformer());

		// Filtro para convertir la salida a formato JSON
		after((request, response) -> {
			response.type("application/json");
		});
	}
}
