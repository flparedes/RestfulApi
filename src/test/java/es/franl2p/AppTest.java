package es.franl2p;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.franl2p.model.User;
import spark.Spark;
import spark.utils.IOUtils;

/**
 * Unit test for simple App.
 */
public class AppTest {
	private static final String NOMBRE = "Fran";
	private static final String CORREO = "fran@correo.com";
	private static final String NOMBRE_MOD = "Adrian";
	private static final String CORREO_MOD = "adrian@correo.com";

	private static final String JAMES_ID = "007";
	private static final String JAMES_NAME = "James Bond";
	private static final String JAMES_MAIL = "james.bond@007.com";
	
	@BeforeClass
	public static void beforeClass() {
		App.main(null);
	}

	@AfterClass
	public static void afterClass() {
		Spark.stop();
	}
	
	@Test
	public void createUser() {
		TestResponse res = request("POST", "/users?name=" + NOMBRE+ "&email=" + CORREO);
		User user = json2User(res.body);
		assertEquals(200, res.status);
		assertEquals(NOMBRE, user.getName());
		assertEquals(CORREO, user.getEmail());
		assertNotNull(user.getId());
	}
	
	@Test
	public void getUsers() {
		TestResponse res = request("GET", "/users");
		List<User> users = json2ListOfUser(res.body);
		assertEquals(200, res.status);
		assertNotNull(users);
	}
	
	@Test
	public void getUser() {
		TestResponse res = request("GET", "/users/" + JAMES_ID);
		User user = json2User(res.body);
		assertEquals(200, res.status);
		assertEquals(JAMES_NAME, user.getName());
		assertEquals(JAMES_MAIL, user.getEmail());
		assertNotNull(user.getId());
	}
	
	@Test
	public void getUserFail() {
		TestResponse res = request("GET", "/users/no_existe");
		assertEquals(null, res);
	}
	
	@Test
	public void updateUser() {
		TestResponse res = request("POST", "/users?name=" + NOMBRE + "&email=" + CORREO);
		User user = json2User(res.body);
		assertEquals(200, res.status);
		assertEquals(NOMBRE, user.getName());
		assertEquals(CORREO, user.getEmail());
		String idUser = user.getId();
		assertNotNull(idUser);
		
		// Update user
		res = request("PUT", "/users/" + idUser + "?name=" + NOMBRE_MOD + "&email=" + CORREO_MOD);
		user = json2User(res.body);
		assertEquals(200, res.status);
		assertEquals(NOMBRE_MOD, user.getName());
		assertEquals(CORREO_MOD, user.getEmail());
		assertEquals(idUser, user.getId());
	}
	
	@Test
	public void updateUserFail() {
		TestResponse res = request("PUT", "/users/no_existe?name=" + NOMBRE_MOD + "&email=" + CORREO_MOD);
		assertEquals(null, res);
	}
	
	@Test
	public void deleteUser() {
		TestResponse res = request("POST", "/users?name=" + NOMBRE + "&email=" + CORREO);
		User user = json2User(res.body);
		assertEquals(200, res.status);
		assertEquals(NOMBRE, user.getName());
		assertEquals(CORREO, user.getEmail());
		String idUser = user.getId();
		assertNotNull(idUser);
		
		// Delete user
		res = request("DELETE", "/users/" + idUser);
		assertEquals(200, res.status);
		
		// Find user
		res = request("GET", "/users/" + idUser);
		assertEquals(null, res);
	}
	
	@Test
	public void deleteUserFail() {
		TestResponse res = request("DELETE", "/users/no_existe");
		assertEquals(null, res);
	}

	private TestResponse request(String method, String path) {
		try {
			URL url = new URL("http://localhost:4567" + path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.connect();
			String body = IOUtils.toString(connection.getInputStream());
			return new TestResponse(connection.getResponseCode(), body);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static class TestResponse {

		public final String body;
		public final int status;

		public TestResponse(int status, String body) {
			this.status = status;
			this.body = body;
		}
	}
	
	private static User json2User(String body) {
		return new Gson().fromJson(body, User.class);
	}
	
	private static List<User> json2ListOfUser(String body) {
		Type listType = new TypeToken<List<User>>(){}.getType();
		List<User> users = new Gson().fromJson(body, listType);
		return users;
	}
}
