package com.gb.usersCRUD.dto;

public record Response<T>(int status, String message, T data) {
}
