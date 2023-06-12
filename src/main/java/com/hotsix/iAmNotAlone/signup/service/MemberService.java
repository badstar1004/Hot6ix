package com.hotsix.iAmNotAlone.signup.service;

import com.hotsix.iAmNotAlone.signup.domain.Member;
import com.hotsix.iAmNotAlone.signup.domain.Region;
import com.hotsix.iAmNotAlone.signup.domain.dto.AddMemberDto;
import com.hotsix.iAmNotAlone.signup.domain.dto.MemberDto;
import com.hotsix.iAmNotAlone.signup.domain.dto.S3FileDto;
import com.hotsix.iAmNotAlone.signup.repository.MemberRepository;
import com.hotsix.iAmNotAlone.signup.repository.RegionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadService s3UploadService;

    public List<MemberDto> findAll() {
        List<MemberDto> memberDtos = new ArrayList<>();
        for (Member m : memberRepository.findAll()) {
            memberDtos.add(MemberDto.from(m));
        }
        return memberDtos;
    }

    @Transactional
    public Member update(AddMemberDto form, Long id, List<MultipartFile> multipartFiles) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Region region = regionRepository.findById(form.getRegionId()).orElseThrow(
            () -> new IllegalArgumentException("일치하는 지역이 없습니다. 지역을 선택해주세요.")
        );

        List<S3FileDto> s3FileDtos = s3UploadService.uploadFiles(multipartFiles);
        s3FileDtos.get(0).getUploadFileUrl();

        String password = passwordEncoder.encode(form.getPassword());
        return memberRepository.save(Member.of(form, region, password, s3FileDtos.get(0).getUploadFileUrl()));
    }

}
