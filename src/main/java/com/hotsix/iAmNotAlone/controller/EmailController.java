package com.hotsix.iAmNotAlone.controller;

import com.hotsix.iAmNotAlone.domain.dto.EmailRequestDto;
import com.hotsix.iAmNotAlone.domain.dto.EmailVerifyDto;
import com.hotsix.iAmNotAlone.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/api/email")
    public ResponseEntity<?> sendAuthMail(@RequestBody EmailRequestDto emailRequestDto) {
        emailService.sendMail(emailRequestDto);
        return ResponseEntity.ok("이메일이 발송 되었습니다. 이메일을 확인해 주세요.");
    }

    @PostMapping("/api/email/verify")
    public ResponseEntity<?> verifyEmailAuth(@RequestBody EmailVerifyDto emailVerifyDto) {
        emailService.verifyMail(emailVerifyDto.getEmail(), emailVerifyDto.getCode());
        return ResponseEntity.ok("인증이 완료되었습니다.");
    }
}
