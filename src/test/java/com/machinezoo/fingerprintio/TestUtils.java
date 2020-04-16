// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.charset.*;
import org.apache.commons.io.*;
import com.fasterxml.jackson.databind.*;
import com.machinezoo.noexception.*;

public class TestUtils {
	public static byte[] resource(Class<?> clazz, String filename) {
		return Exceptions.sneak().get(() -> {
			try (InputStream stream = clazz.getResourceAsStream(filename)) {
				return IOUtils.toByteArray(stream);
			}
		});
	}
	public static void compareJson(Class<?> clazz, String filename, Object template) {
		Exceptions.sneak().run(() -> {
			ObjectMapper mapper = new ObjectMapper()
				.enable(SerializationFeature.INDENT_OUTPUT);
			String json = mapper.writeValueAsString(template);
			JsonNode fresh = mapper.readTree(json);
			/*
			 * This is how the JSON files were originally generated. Uncomment this line if some files need to be generated again.
			 * Then copy the generated file into resources.
			 */
			//Files.write(Paths.get(filename), json.getBytes(StandardCharsets.UTF_8));
			JsonNode stored = mapper.readTree(new String(resource(clazz, filename), StandardCharsets.UTF_8));
			assertEquals(stored, fresh);
		});
	}
}
