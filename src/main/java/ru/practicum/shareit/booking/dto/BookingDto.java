package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    @FutureOrPresent
    private LocalDateTime start;    //— дата и время начала бронирования

    @Future
    private LocalDateTime end;      //— дата и время конца бронирования
    private Long itemId;            //— вещь, которую пользователь бронирует
}
