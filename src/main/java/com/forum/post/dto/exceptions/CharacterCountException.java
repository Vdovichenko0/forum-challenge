package com.forum.post.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CharacterCountException extends RuntimeException{

    private static final long serialVersionUID = -203115668743393948L;
}
