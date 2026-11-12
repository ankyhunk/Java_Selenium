package rsa.testcomponents;

/**
 * Supplies the test account login without storing it in the repository.
 *
 * Set the environment variables TEST_EMAIL and TEST_PASSWORD, or pass
 * -Dtest.email=... -Dtest.password=... to Maven. Test data files use the
 * placeholders ${TEST_EMAIL} and ${TEST_PASSWORD}, which resolve() replaces.
 */
public final class TestCredentials {

	private TestCredentials() {
	}

	public static String email() {
		return read("test.email", "TEST_EMAIL");
	}

	public static String password() {
		return read("test.password", "TEST_PASSWORD");
	}

	/** Replaces ${TEST_EMAIL} and ${TEST_PASSWORD} in a test data value. */
	public static String resolve(String value) {
		if (value == null) {
			return null;
		}
		if (value.contains("${TEST_EMAIL}")) {
			value = value.replace("${TEST_EMAIL}", email());
		}
		if (value.contains("${TEST_PASSWORD}")) {
			value = value.replace("${TEST_PASSWORD}", password());
		}
		return value;
	}

	private static String read(String property, String envVar) {
		String value = System.getProperty(property);
		if (value == null || value.isBlank()) {
			value = System.getenv(envVar);
		}
		if (value == null || value.isBlank()) {
			throw new IllegalStateException("Test login not set: set the " + envVar
					+ " environment variable or pass -D" + property + "=... to Maven");
		}
		return value;
	}
}
