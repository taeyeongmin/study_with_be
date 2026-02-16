package com.ty.study_with_be.global.advisor;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.error.ErrorResponse;
import com.ty.study_with_be.global.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBase(BaseException ex, HttpServletRequest req) {
        ErrorCode code = ex.getErrorCode();

        String message = messageSource.getMessage(
                code.getMessageKey(),
                null,
                LocaleContextHolder.getLocale()
        );

        ErrorResponse body = ErrorResponse.of(code, message, req.getRequestURI());
        log.warn("[{}] {}", code.name(), message);
        return ResponseEntity.status(code.getHttpStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest req) {

        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatRequiredMessage)
                .toList();

        String combinedMessage = String.join(", ", messages);

        ErrorResponse body = ErrorResponse.of(
                ErrorCode.VALIDATION_FAILED,
                combinedMessage,
                req.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * JSON 파싱 오류
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest req
    ) {
        log.warn("[BAD_REQUEST] JSON parse error", ex);

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(
                        ErrorCode.INVALID_JSON,
                        getMessage(ErrorCode.INVALID_JSON),
                        req.getRequestURI()
                ));
    }

    /**
     * 인증 실패(401)
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(
            AuthenticationException ex,
            HttpServletRequest req
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(
                        ErrorCode.UNAUTHORIZED,
                        getMessage(ErrorCode.UNAUTHORIZED),
                        req.getRequestURI()
                ));
    }

    /**
     * 권한 실패(403)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest req
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(
                        ErrorCode.FORBIDDEN,
                        getMessage(ErrorCode.FORBIDDEN),
                        req.getRequestURI()
                ));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            NoHandlerFoundException ex,
            HttpServletRequest req
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        ErrorCode.NOT_FOUND,
                        getMessage(ErrorCode.NOT_FOUND),
                        req.getRequestURI()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex,
            HttpServletRequest req
    ) {
        log.error("[INTERNAL_ERROR]", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        getMessage(ErrorCode.INTERNAL_SERVER_ERROR),
                        req.getRequestURI()
                ));
    }

    /**
     * 필드명 기반으로 일관된 에러 메시지를 생성
     */
    private String formatRequiredMessage(FieldError error) {
        String field = error.getField();
        return messageSource.getMessage(
                ErrorCode.VALIDATION_FAILED.getMessageKey(),
                new Object[]{field},
                LocaleContextHolder.getLocale()
        );
    }

    private String getMessage(ErrorCode code) {
        return messageSource.getMessage(
                code.getMessageKey(),
                null,
                LocaleContextHolder.getLocale()
        );
    }

}
