package commercial.TrendWay.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public class BadRequestException extends HttpClientErrorException {
    private final ErrorCodes errorCode;

    public BadRequestException(final String message, ErrorCodes errorCode) {
        super(HttpStatus.BAD_REQUEST, message);
        this.errorCode = errorCode;
    }
}
