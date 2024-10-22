package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
@Slf4j
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDtoOut saveNewRequest(ItemRequestDto requestDto, long userId) {
        log.info("Создание нового запроса {}", requestDto.getDescription());
        User requestor = getUser(userId);
        ItemRequest request = RequestMapper.toItemRequest(requestDto);
        request.setCreated(LocalDateTime.now());
        request.setRequestor(requestor);
        return RequestMapper.toItemRequestDtoOut(requestRepository.save(request));
    }

    @Override
    public List<ItemRequestDtoOut> getRequestsByRequestor(long userId) {
        log.info("Получение всех запросов пользователя с id: {}", userId);
        getUser(userId);
        List<ItemRequest> requests = requestRepository.findAllByRequestorId(userId, Sort.by(DESC, "created"));
        return addItems(requests);
    }

    @Override
    public List<ItemRequestDtoOut> getAllRequests(Integer from, Integer size, long userId) {
        log.info("Получение всех запросов");
        getUser(userId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());
        List<ItemRequest> requests = requestRepository.findAllByRequestorIdIsNot(userId, pageable);
        return addItems(requests);
    }

    @Override
    public ItemRequestDtoOut getRequestById(long requestId, long userId) {
        log.info("Получение запроса с id: {}", requestId);
        getUser(userId);
        ItemRequestDtoOut requestDtoOut = RequestMapper.toItemRequestDtoOut(requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Запрос не найден")));
        requestDtoOut.setItems(itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::toItemDtoOut).collect(toList()));
        return requestDtoOut;
    }

    private List<ItemRequestDtoOut> addItems(List<ItemRequest> requests) {
        final List<ItemRequestDtoOut> requestsOut = new ArrayList<>();
        for (ItemRequest request : requests) {
            ItemRequestDtoOut requestDtoOut = RequestMapper.toItemRequestDtoOut(request);
            List<ItemDtoOut> items = itemRepository.findAllByRequestId(request.getId()).stream()
                    .map(ItemMapper::toItemDtoOut).collect(toList());
            requestDtoOut.setItems(items);
            requestsOut.add(requestDtoOut);
        }
        return requestsOut;
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь не найден"));
    }
}
