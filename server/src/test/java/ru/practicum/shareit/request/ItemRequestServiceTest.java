package ru.practicum.shareit.request;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl requestService;

    private final LocalDateTime now = LocalDateTime.now();
    private final User requestor = new User(2L, "user2", "user2@email.ru");
    private final User user = new User(1L, "name", "name@email.com");
    private final ItemRequest request = new ItemRequest(1L, "description request", requestor, now);
    private final Item item = new Item(1L, "item", "description", true, user, request);

    @Test
    void saveNewRequest() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(requestor));
        when(requestRepository.save(any())).thenReturn(request);

        final ItemRequestDtoOut actualRequest = requestService.saveNewRequest(
                new ItemRequestDto("description"), 2L);

        Assertions.assertEquals(RequestMapper.toItemRequestDtoOut(request), actualRequest);
    }

    @Test
    void getRequestsByRequestor_whenUserFound_thenSavedRequest() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(requestor));
        when(requestRepository.findAllByRequestorId(anyLong(), any())).thenReturn(List.of(request));
        when(itemRepository.findAllByRequestId(1L)).thenReturn(List.of(item));
        final ItemRequestDtoOut requestDtoOut = RequestMapper.toItemRequestDtoOut(request);
        requestDtoOut.setItems(List.of(ItemMapper.toItemDtoOut(item)));

        List<ItemRequestDtoOut> actualRequests = requestService.getRequestsByRequestor(2L);

        Assertions.assertEquals(List.of(requestDtoOut), actualRequests);
    }

    @Test
    void getRequestsByRequestor_whenUserNotFound_thenThrownException() {
        when((userRepository).findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                requestService.getRequestsByRequestor(3L));
    }

    @Test
    void getRequestById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(itemRepository.findAllByRequestId(1L)).thenReturn(List.of(item));
        final ItemRequestDtoOut requestDto = RequestMapper.toItemRequestDtoOut(request);
        requestDto.setItems(List.of(ItemMapper.toItemDtoOut(item)));

        ItemRequestDtoOut actualRequest = requestService.getRequestById(1L, 1L);

        Assertions.assertEquals(requestDto, actualRequest);
    }
}