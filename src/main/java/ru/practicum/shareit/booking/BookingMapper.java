package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

@Component
public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId()
        );
    }

    public static BookingDtoShort toBookingDtoShort(Booking booking) {
        return new BookingDtoShort(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker().getId()
        );
    }

    public static BookingDtoOut toBookingDtoOut(Booking booking) {
        return new BookingDtoOut(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.toItemDtoShort(booking.getItem()),
                UserMapper.toUserDtoShort(booking.getBooker()),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto bookingDto, Booking booking) {
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(Status.WAITING);
        return booking;
    }
}
