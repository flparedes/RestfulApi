package es.franl2p.services;

import java.util.LinkedList;
import java.util.List;

import es.franl2p.model.User;

public class UserService {
	/**
	 * Lista de usuarios para pruebas
	 */
	private static List<User> users;

	/**
	 * Contador para el id de los nuevos usuarios
	 */
	private static int contador = 0;

	/**
	 * Contructor sin parámetros
	 */
	public UserService() {
		// Carga de usuarios de pruebas
		users = new LinkedList<User>();
		users.add(new User("001", "Periquito", "perico@correo.es"));
		users.add(new User("007", "James Bond", "james.bond@007.com"));
	}

	/**
	 * Recupera la lista completa de usuarios.
	 * 
	 * @return lista de usuarios.
	 */
	public List<User> getAllUsers() {
		// Recupera la lista de usuarios cargada en el constructor
		return users;
	}

	/**
	 * Recupera el usuario con el id dado.
	 * 
	 * @param id
	 *            identificador del usuario.
	 * @return Usuario encontrado o nulo en caso contrario.
	 */
	public User getUser(String id) {
		User salida = null;

		for (User user : users) {
			if (user.getId().equals(id)) {
				salida = user;
			}
		}

		return salida;
	}

	/**
	 * Crea un usuario con los datos dados. El id se autocalcula.
	 * 
	 * @param name
	 *            nombre del usuario.
	 * @param email
	 *            email del usuario.
	 * @return Usuario creado.
	 */
	public User createUser(String name, String email) {
		contador++;
		User user = new User(contador + "", name, email);
		users.add(user);
		return user;
	}

	/**
	 * Actualiza los datos de usuario con el id dado.
	 * 
	 * @param id
	 *            identificador del usuario.
	 * @param name
	 *            nombre del usuario.
	 * @param email
	 *            email del usuario.
	 * @return Usuario con los datos modificados.
	 */
	public User updateUser(String id, String name, String email) {
		User salida = null;

		for (User user : users) {
			if (user.getId().equals(id)) {
				user.setName(name);
				user.setEmail(email);
				salida = user;
			}
		}

		return salida;
	}
}
