package no.toreb.hateoasapi.api;

import org.springframework.http.MediaType;

public final class CustomMediaType extends MediaType {

    private static final String TYPE = "application";
    private static final String SUBTYPE_PREFIX = "vnd.no.toreb.hateoasapi.v";
    private static final String SUBTYPE_SUFFIX = "+json";

    public static final MediaType V1 = new CustomMediaType("1");
    public static final String V1_VALUE = TYPE + "/" + SUBTYPE_PREFIX + "1" + SUBTYPE_SUFFIX;

    public CustomMediaType(final String version) {
        super(TYPE, SUBTYPE_PREFIX + version + SUBTYPE_SUFFIX);
    }

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
}
