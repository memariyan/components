package com.memariyan.components.test.utils;

import com.github.tomakehurst.wiremock.matching.AnythingPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class FileStubUtils {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public FileStubUtils() {
    }

    public static String makeSafeFileName(StubMapping mapping) {
        return makeSafeFileName(mapping, "json");
    }

    public static String makeSafeFileName(StubMapping mapping, String extension) {
        String suffix = "-" + mapping.getId() + "." + extension;
        if (StringUtils.isNotEmpty(mapping.getName())) {
            return makeSafeName(mapping.getName()) + suffix;
        } else {
            UrlPattern urlMatcher = mapping.getRequest().getUrlMatcher();
            if (urlMatcher.getPattern() instanceof AnythingPattern) {
                return suffix.substring(1);
            } else {
                String expectedUrl = urlMatcher.getExpected();
                URI uri = URI.create(sanitise(expectedUrl));
                return makeSafeNameFromUrl(uri.getPath()) + suffix;
            }
        }
    }

    public static String makeSafeNameFromUrl(String urlPath) {
        String startingPath = urlPath.replace("/", "_");
        return makeSafeName(startingPath);
    }

    public static String makeSafeName(String name) {
        String nowhitespace = WHITESPACE.matcher(name).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = sanitise(normalized);
        slug = slug.replaceAll("^[_]*", "");
        slug = slug.replaceAll("[_]*$", "");
        slug = StringUtils.truncate(slug, 200);
        return slug.toLowerCase(Locale.ENGLISH);
    }

    private static String sanitise(String s) {
        return NON_ALPHANUMERIC.matcher(s).replaceAll("");
    }

}
