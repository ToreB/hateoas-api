package no.toreb.hateoasapi.configuration;

import org.springframework.http.MediaType;

public final class CustomMediaType {

    public static final String TYPE = "application";
    public static final String SUBTYPE_PREFIX = "vnd.no.toreb.hateoasapi";
    public static final String SUBTYPE_SUFFIX = "+json";
    private static final String SUBTYPE_PATTERN = SUBTYPE_PREFIX + "*" + SUBTYPE_SUFFIX;

    public static final MediaType MEDIA_TYPE = new MediaType(TYPE, SUBTYPE_PATTERN) {
        @Override
        public boolean isCompatibleWith(final MediaType other) {
            if (other == null) {
                return false;
            } else if (other.getSubtype().startsWith(SUBTYPE_PREFIX)
                    && other.getSubtype().endsWith(SUBTYPE_SUFFIX)) {
                return true;
            }
            return super.isCompatibleWith(other);
        }
    };

    private CustomMediaType() {
        // EMPTY
    }
}
