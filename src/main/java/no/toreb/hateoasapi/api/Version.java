package no.toreb.hateoasapi.api;

import static no.toreb.hateoasapi.configuration.CustomMediaType.SUBTYPE_PREFIX;
import static no.toreb.hateoasapi.configuration.CustomMediaType.SUBTYPE_SUFFIX;
import static no.toreb.hateoasapi.configuration.CustomMediaType.TYPE;

public final class Version {

    public static final String V1 = TYPE + "/" + SUBTYPE_PREFIX + ".v1" + SUBTYPE_SUFFIX;

    private Version() {
        // EMPTY
    }
}
