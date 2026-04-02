package com.goolbitg.api.v1.controller;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.BuyOrNotApi;
import com.goolbitg.api.model.BuyOrNotDto;
import com.goolbitg.api.model.BuyOrNotReportRequest;
import com.goolbitg.api.model.BuyOrNotVoteChangeDto;
import com.goolbitg.api.model.BuyOrNotVoteDto;
import com.goolbitg.api.model.ChatMessageDto;
import com.goolbitg.api.model.PaginatedBuyOrNotDto;
import com.goolbitg.api.v1.entity.chat.ChatMessage;
import com.goolbitg.api.v1.security.AuthUtil;
import com.goolbitg.api.v1.service.BuyOrNotService;
import com.goolbitg.api.v1.service.ChatService;

import lombok.RequiredArgsConstructor;

/**
 * BuyOrNotController
 */
@RestController
@RequiredArgsConstructor
public class BuyOrNotController implements BuyOrNotApi {

    private final BuyOrNotService buyOrNotService;
    private final ChatService chatService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

    @Override
    public ResponseEntity<BuyOrNotDto> createBuyOrNot(BuyOrNotDto buyOrNotDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        BuyOrNotDto result = buyOrNotService.createBuyOrNot(userId, buyOrNotDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteBuyOrNot(Long postId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        buyOrNotService.deleteBuyOrNot(userId, postId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BuyOrNotDto> getBuyOrNot(Long postId) throws Exception {
        BuyOrNotDto result = buyOrNotService.getBuyOrNot(postId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<PaginatedBuyOrNotDto> getBuyOrNots(Integer page, Integer size,
            Boolean created) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        String writerId = null;
        if (created) writerId = userId;
        PaginatedBuyOrNotDto result = buyOrNotService.getBuyOrNots(page, size, userId, writerId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<BuyOrNotDto> updateBuyOrNot(Long postId, BuyOrNotDto buyOrNotDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        BuyOrNotDto result = buyOrNotService.updateBuyOrNot(userId, postId, buyOrNotDto);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<BuyOrNotVoteChangeDto> voteBuyOrNot(Long postId, BuyOrNotVoteDto buyOrNotVoteDto)
            throws Exception {
        String userId = AuthUtil.getLoginUserId();
        BuyOrNotVoteChangeDto result = buyOrNotService.voteBuyOrNot(userId, postId, buyOrNotVoteDto);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> buyOrNotReport(Long postId, BuyOrNotReportRequest buyOrNotReportRequest)
            throws Exception {
        String userId = AuthUtil.getLoginUserId();
        buyOrNotService.reportBuyOrNot(userId, postId, buyOrNotReportRequest.getReason());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ChatMessageDto>> chatHistory(Long postId, Long chatLastId) throws Exception {
        List<ChatMessage> messageHistory = chatService.getMessageHistory(postId, chatLastId);
        return ResponseEntity.ok(messageHistory.stream()
            .map(x -> convert(x))
            .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<PaginatedBuyOrNotDto> chatList(String userId, Integer page, Integer size)
            throws Exception {
        List<BuyOrNotDto> chattingBuyOrNots = buyOrNotService.getChattingBuyOrNots(userId, page, size);
        int totalSize = buyOrNotService.getTotalChattingBuyOrNots(userId);
        PaginatedBuyOrNotDto paginatedBuyOrNotDto = new PaginatedBuyOrNotDto();
        paginatedBuyOrNotDto.setPage(page);
        paginatedBuyOrNotDto.setSize(size);
        paginatedBuyOrNotDto.setItems(chattingBuyOrNots);
        paginatedBuyOrNotDto.setTotalSize(totalSize);
        paginatedBuyOrNotDto.setTotalPages((int)Math.ceil((double)totalSize / size));

        return ResponseEntity.ok(paginatedBuyOrNotDto);
    }

    private ChatMessageDto convert(ChatMessage chatMessage) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(chatMessage.getId().intValue());
        dto.setUsername(chatMessage.getUsername());
        dto.setContent(chatMessage.getContent());
        ZoneOffset offset = ZonedDateTime.now(ZoneId.systemDefault()).getOffset();
        dto.setSentDateTime(OffsetDateTime.of(chatMessage.getSentDateTime(), offset));

        return dto;
    }


}
