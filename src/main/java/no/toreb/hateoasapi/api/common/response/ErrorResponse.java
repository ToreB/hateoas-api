package no.toreb.hateoasapi.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

    private final Long timestamp;

    private final Integer status;

    private final String error;

    private final String message;

    private final String path;

    public ErrorResponse(final Long timestamp, final HttpStatus status, final String message, final String path) {
        this.timestamp = timestamp;
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("timestamp", timestamp)
                .append("status", status)
                .append("error", error)
                .append("message", message)
                .append("path", path)
                .toString();
    }
}
