// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import org.apache.commons.io.*;
import com.fasterxml.jackson.databind.*;
import com.machinezoo.fingerprintio.ansi378v2004.*;
import com.machinezoo.fingerprintio.ansi378v2009.*;
import com.machinezoo.fingerprintio.ansi378v2009am1.*;
import com.machinezoo.fingerprintio.iso19794p2v2005.*;
import com.machinezoo.fingerprintio.iso19794p2v2011.*;
import com.machinezoo.noexception.*;

public class TestUtils {
	public static byte[] resource(Class<?> clazz, String filename) {
		return Exceptions.sneak().get(() -> {
			try (InputStream stream = clazz.getResourceAsStream(filename)) {
				return IOUtils.toByteArray(stream);
			}
		});
	}
	private static Path cacheRoot() {
		String xdg = System.getenv("XDG_CACHE_HOME");
		if (xdg != null && !xdg.trim().isEmpty())
			return Paths.get(xdg);
		String home = System.getProperty("user.home");
		if (home != null && !home.trim().isEmpty())
			return Paths.get(home, ".cache");
		throw new IllegalStateException();
	}
	private static Path cacheDir() {
		Path path = cacheRoot().resolve("fingerprintio");
		if (!Files.isDirectory(path))
			Exceptions.sneak().run(() -> Files.createDirectories(path));
		return path;
	}
	public static void compareJson(Class<?> clazz, String filename, Object template) {
		Exceptions.sneak().run(() -> {
			ObjectMapper mapper = new ObjectMapper()
				.enable(SerializationFeature.INDENT_OUTPUT);
			String json = mapper.writeValueAsString(template);
			JsonNode fresh = mapper.readTree(json);
			/*
			 * JSON files are regenerated during every test run.
			 * Copy them into resources if the files in resources are outdated.
			 */
			Files.write(cacheDir().resolve(filename), json.getBytes(StandardCharsets.UTF_8));
			JsonNode stored = mapper.readTree(new String(resource(clazz, filename), StandardCharsets.UTF_8));
			assertEquals(stored, fresh);
		});
	}
	public static byte[] sample(TemplateFormat format) {
		switch (format) {
		case ANSI_378_2004:
			return Ansi378v2004TemplateTest.sampleWithFixedVersion();
		case ANSI_378_2009:
			return Ansi378v2009TemplateTest.sample();
		case ANSI_378_2009_AM1:
			return Ansi378v2009Am1TemplateTest.sample();
		case ISO_19794_2_2005:
			return Iso19794p2v2005TemplateTest.sample();
		case ISO_19794_2_2011:
			return Iso19794p2v2011TemplateTest.sample();
		default:
			throw new IllegalArgumentException();
		}
	}
}
