package org.koreait.games.exceptions;

import org.koreait.global.exceptions.scripts.AlertBackException;
import org.springframework.http.HttpStatus;

public class SelectCheckException extends AlertBackException {
    public SelectCheckException() {
        super("NotGameCheck.pokemon", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
