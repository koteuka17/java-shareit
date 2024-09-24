package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut saveNewBooking(@RequestBody @Valid BookingDto bookingDto,
                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.saveNewBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut approve(@PathVariable long bookingId,
                              @RequestParam(name = "approved") Boolean isApproved,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.approve(bookingId, isApproved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@PathVariable long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoOut> getAllByBooker(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                           @RequestHeader("X-Sharer-User-Id") long bookerId) {
        return bookingService.getAllByBooker(state, bookerId);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllByOwner(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                          @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return bookingService.getAllByOwner(state, ownerId);
    }
}
