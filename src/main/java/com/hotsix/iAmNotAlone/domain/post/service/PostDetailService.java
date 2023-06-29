package com.hotsix.iAmNotAlone.domain.post.service;

import com.hotsix.iAmNotAlone.domain.comments.repository.CommentsRepository;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostDetailDto;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.RedisUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_POST;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostDetailService {

    private final PostRepository postRepository;
    private final CommentsRepository commentsRepository;
    private final MembershipRepository membershipRepository;
    private final RedisUtil redisUtil;

    public PostDetailDto findPost(Long postId, Long membershipId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_POST)
        );
        Long commentCount = commentsRepository.countByPostId(postId);
        PostDetailDto postDetailDto = PostDetailDto.from(post, commentCount);
        log.info("게시글 정보 조회");

        Membership membership = membershipRepository.findById(membershipId).orElseThrow(
            () -> new BusinessException(NOT_FOUND_USER)
        );
        // 좋아요 확인
        List<Long> likeList = membership.getLikelist().stream().filter(s->!s.isEmpty()).map(Long::parseLong).collect(
            Collectors.toList());
        postDetailDto.setLike(likeList.isEmpty() ? false : likeList.contains(postId));

        //좋아요 수
        Long likeCount = redisUtil.getLikeCount("likes:" + postId);
        postDetailDto.setLikes(likeCount);

        return postDetailDto;
    }

}
