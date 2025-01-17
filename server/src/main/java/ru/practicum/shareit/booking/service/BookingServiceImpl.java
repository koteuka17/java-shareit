package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.EntityException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDtoOut saveNewBooking(BookingDto bookingDto, Long userId) {
        log.info("Создание бронирования {}", bookingDto);
        User booker = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Вещь не найдена"));
        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Вещь недоступна для брони");
        }
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        return BookingMapper.toBookingDtoOut(bookingRepository.save(BookingMapper.toBooking(bookingDto, booking)));
    }

    @Override
    public BookingDtoOut approve(Long bookingId, Boolean isApproved, Long userId) {
        Booking booking = getById(bookingId);
        if (booking.getStatus() != Status.WAITING) {
            throw new EntityException("Вещь уже забронирована");
        }
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(() ->
                new EntityNotFoundException("Вещь не найдена"));
        if (!Objects.equals(userId, item.getOwner().getId())) {
            throw new IllegalArgumentException("Подтвердить бронирование может только собственник вещи");
        }
        Status newBookingStatus = isApproved ? Status.APPROVED : Status.REJECTED;
        booking.setStatus(newBookingStatus);
        log.info("Бронирование с id {} обновлено", booking.getId());
        return BookingMapper.toBookingDtoOut(booking);
    }

    @Override
    public BookingDtoOut getBookingById(Long bookingId, Long userId) {
        log.info("Получение бронирования c id {}", bookingId);
        Booking booking = getById(bookingId);
        User booker = booking.getBooker();
        User owner = userRepository.findById(booking.getItem().getOwner().getId()).orElseThrow(() ->
                new EntityNotFoundException("Пользователь не найден"));
        if (!Objects.equals(booker.getId(), userId) && !Objects.equals(owner.getId(), userId)) {
            throw new EntityException("Только автор или владелец может просматривать данное бронирование");
        }
        return BookingMapper.toBookingDtoOut(booking);
    }

    @Override
    public List<BookingDtoOut> getAllByBooker(State state, Long bookerId) {

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByBookerId(bookerId);
            case CURRENT -> bookingRepository.findAllByBookerIdAndStateCurrent(bookerId);
            case PAST -> bookingRepository.findAllByBookerIdAndStatePast(bookerId);
            case FUTURE -> bookingRepository.findAllByBookerIdAndStateFuture(bookerId);
            case WAITING -> bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.WAITING);
            case REJECTED -> bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.REJECTED);
            default -> throw new EntityException("Статус не поддерживается");
        };
        return bookings.stream().map(BookingMapper::toBookingDtoOut).collect(toList());
    }

    @Override
    public List<BookingDtoOut> getAllByOwner(State state, Long ownerId) {
        userRepository.findById(ownerId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь не найден"));

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByOwnerId(ownerId);
            case CURRENT -> bookingRepository.findAllByOwnerIdAndStateCurrent(ownerId);
            case PAST -> bookingRepository.findAllByOwnerIdAndStatePast(ownerId);
            case FUTURE -> bookingRepository.findAllByOwnerIdAndStateFuture(ownerId);
            case WAITING -> bookingRepository.findAllByOwnerIdAndStatus(ownerId, Status.WAITING);
            case REJECTED -> bookingRepository.findAllByOwnerIdAndStatus(ownerId, Status.REJECTED);
            default -> throw new EntityException("Статус не поддерживается");
        };
        return bookings.stream().map(BookingMapper::toBookingDtoOut).collect(toList());
    }

    private Booking getById(long id) {
        log.info("Получение бронирования с id {}", id);
        return bookingRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Бронирование не найдено"));
    }


}
