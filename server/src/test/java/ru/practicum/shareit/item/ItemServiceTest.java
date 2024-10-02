package ru.practicum.shareit.item;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDtoShort;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    private final Long id = 1L;
    private final User user = new User(id, "name", "name@email.ru");
    private final User notOwner = new User(2L, "User2", "user2@email.ru");
    private final ItemDto itemDto = new ItemDto("item", "description item", true, null);
    private final ItemDtoOut itemDtoOut = new ItemDtoOut(id, "item", "description item", true,
            new UserDtoShort(id, "name"));
    private final Item item = new Item(id, "item", "description item", true, user, null);
    private final CommentDto commentDto = new CommentDto(id, "text", "name",
            LocalDateTime.of(2023, 7, 1, 12, 12, 12));
    private final Comment comment = new Comment(id, "text", item, user,
            LocalDateTime.of(2023, 7, 1, 12, 12, 12));
    private final Booking booking = new Booking(id, null, null, item, user, Status.WAITING);

    @Test
    void saveNewItem_whenUserFound_thenSavedItem() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(itemRepository.save(any())).thenReturn(item);

        ItemDtoOut actualItemDto = itemService.createItemDto(id, itemDto);

        Assertions.assertEquals(ItemMapper.toItemDtoOut(item), actualItemDto);
        Assertions.assertNull(item.getRequest());
    }

    @Test
    void saveNewItem_whenUserNotFound_thenNotSavedItem() {
        when((userRepository).findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.createItemDto(2L, itemDto));
    }

    @Test
    void saveNewItem_whenNoName_thenNotSavedItem() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doThrow(DataIntegrityViolationException.class).when(itemRepository).save(any(Item.class));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemService.createItemDto(id, itemDto));
    }

    @Test
    void updateItem_whenUserIsOwner_thenUpdatedItem() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        ItemDtoOut actualItemDto = itemService.updateItemDto(id, itemDto, id);

        Assertions.assertEquals(itemDtoOut, actualItemDto);
    }

    @Test
    void updateItem_whenUserNotOwner_thenNotUpdatedItem() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(notOwner));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        Assertions.assertThrows(NotFoundException.class, () -> itemService.updateItemDto(2L, itemDto, id));
    }

    @Test
    void getItemById_whenItemFound_thenReturnedItem() {
        when(bookingRepository.findFirstByItemIdAndStartLessThanEqualAndStatus(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.findFirstByItemIdAndStartAfterAndStatus(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(booking));
        when(commentRepository.findAllByItemId(id)).thenReturn(List.of(comment));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        final ItemDtoOut itemDto = ItemMapper.toItemDtoOut(item);
        itemDto.setLastBooking(BookingMapper.toBookingDtoShort(booking));
        itemDto.setNextBooking(BookingMapper.toBookingDtoShort(booking));
        itemDto.setComments(List.of(CommentMapper.toCommentDto(comment)));

        ItemDtoOut actualItemDto = itemService.getItemDto(id, id);

        Assertions.assertEquals(itemDto, actualItemDto);
    }

    @Test
    void getItemById_whenItemNotFound_thenExceptionThrown() {
        when((itemRepository).findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> itemService.getItemDto(2L, id));
    }

    @Test
    void getItemsByOwner_CorrectArgumentsForPaging_thenReturnItems() {
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(id)).thenReturn(List.of(item));

        List<ItemDtoOut> targetItems = itemService.getItemsDto(id);

        Assertions.assertNotNull(targetItems);
        Assertions.assertEquals(1, targetItems.size());
        verify(itemRepository, times(1))
                .findAllByOwnerIdOrderByIdAsc(id);
    }

    @Test
    void getItemBySearch_whenTextNotBlank_thenReturnItems() {
        when(itemRepository.search(any())).thenReturn(List.of(item));

        List<ItemDtoOut> targetItems = itemService.searchItem(id, "abc");

        Assertions.assertNotNull(targetItems);
        Assertions.assertEquals(1, targetItems.size());
        verify(itemRepository, times(1))
                .search(any());
    }

    @Test
    void getItemBySearch_whenTextIsBlank_thenReturnEmptyList() {
        List<ItemDtoOut> targetItems = itemService.searchItem(id, "");

        Assertions.assertTrue(targetItems.isEmpty());
        Assertions.assertEquals(0, targetItems.size());
        verify(itemRepository, never()).search(any());
    }

    @Test
    void saveNewComment_whenUserWasBooker_thenSavedComment() {
        when(bookingRepository.existsByBookerIdAndItemIdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(commentRepository.save(any())).thenReturn(comment);
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        CommentDto actualComment = itemService.createCommentDto(id, id, new CommentDto("abc"));

        Assertions.assertEquals(commentDto, actualComment);
    }

    @Test
    void saveNewComment_whenUserWasNotBooker_thenThrownException() {
        when((bookingRepository).existsByBookerIdAndItemIdAndEndBefore(anyLong(), anyLong(), any())).thenReturn(false);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                itemService.createCommentDto(2L, id, new CommentDto("abc")));
    }
}