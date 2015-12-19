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
		// Método para tratar los gets de /users
		get("/users", (request, response) -> userService.getAllUsers(), new JsonTransformer());

		// Método para tratar los gets de /users/:id (Datos del usuario dado)
		get("/users/:idUser", (req, res) -> {
			String idUser = req.params(":idUser");
			User user = userService.getUser(idUser);
			if (user != null) {
				return user;
			}

			res.status(400);
			return "No user with id '" + idUser + "' found";
		}, new JsonTransformer());

		// Método para tratar los posts de /users (Creación de usuarios)
		post("/users", (req, res) -> {
			// Se cargan los parámetros de la query (URL)
			String name = req.queryParams("name");
			String email = req.queryParams("email");
			String body = req.body();
			
			// Convertimos de JSON a objeto de la clase User
			User user = json2User(body);
			if (user != null) {
				name = user.getName();
				email = user.getEmail();
			}
			
			return userService.createUser(name, email);
		}, new JsonTransformer());

		// Método para tratar los put de /users/:idUser (Modificación de datos de usuarios)
		put("/users/:idUser", (req, res) -> {
			String idUser = req.params(":idUser");
			
			// Se cargan los parámetros de la query (URL)
			String name = req.queryParams("name");
			String email = req.queryParams("email");
			
			User userData = json2User(req.body());
			if (userData != null) {
				name = userData.getName();
				email = userData.getEmail();
			}

			User user = userService.getUser(idUser);
			if (user != null) {				
				return userService.updateUser(idUser, name, email);
			}

			res.status(400);
			return "No user with id '" + idUser + "' found";
		}, new JsonTransformer());

		// Método para tratar los delete de /users/:idUser (Eliminar usuario)
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
	

	/**
	 * Convierte el objeto JSON dado en un objeto de tipo User.
	 * @param body body de la request con losa datos del usuario.
	 * @return Objeto de tipo User.
	 */
	private static User json2User(String body) {
		User user = null;
		try {
			user = new Gson().fromJson(body, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}
