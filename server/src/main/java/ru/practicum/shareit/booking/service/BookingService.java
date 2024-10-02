package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingDtoOut saveNewBooking(BookingDto bookingDto, Long userId);

    BookingDtoOut approve(Long bookingId, Boolean isApproved, Long userId);

    BookingDtoOut getBookingById(Long bookingId, Long userId);

    List<BookingDtoOut> getAllByBooker(State state, Long bookerId);

    List<BookingDtoOut> getAllByOwner(State state, Long ownerId);
}
