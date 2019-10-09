package json;

import org.springframework.core.io.Resource;
import org.springframework.test.util.JsonExpectationsHelper;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public final class JsonAssertions {
    private JsonAssertions() {}

    public static void assertJsonContentEquals(Resource expected, Resource actual) throws Exception {
        new JsonExpectationsHelper().assertJsonEqual(
                asString(expected),
                asString(actual));
    }

    private static String asString(Resource resource) throws IOException {
        return StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8"));
    }
}
