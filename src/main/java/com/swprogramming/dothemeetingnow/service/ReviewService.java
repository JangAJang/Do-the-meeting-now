package com.swprogramming.dothemeetingnow.service;

import com.swprogramming.dothemeetingnow.dto.review.ReviewRequestDto;
import com.swprogramming.dothemeetingnow.dto.review.ReviewResponseDto;
import com.swprogramming.dothemeetingnow.entity.Category;
import com.swprogramming.dothemeetingnow.entity.Member;
import com.swprogramming.dothemeetingnow.entity.Review;
import com.swprogramming.dothemeetingnow.entity.Station;
import com.swprogramming.dothemeetingnow.exception.MemberNotAuthorized;
import com.swprogramming.dothemeetingnow.exception.MemberNotFoundException;
import com.swprogramming.dothemeetingnow.exception.ReviewNotFoundException;
import com.swprogramming.dothemeetingnow.repository.CategoryRepository;
import com.swprogramming.dothemeetingnow.repository.MemberRepository;
import com.swprogramming.dothemeetingnow.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewResponseDto writeReview(Station station, ReviewRequestDto reviewRequestDto){
        Member member = getMember();
        Category category = categoryRepository.findByName(reviewRequestDto.getCategory_name());
        Review review = Review.builder()
                .category(category)
                .content(reviewRequestDto.getContent())
                .rate(reviewRequestDto.getRate())
                .station(station)
                .member(member)
                .build();
        reviewRepository.save(review);
        return ReviewResponseDto.toDto(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviews(){
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewResponseDto> reviewResponseDtos = new LinkedList<>();
        for(Review review : reviews){
            reviewResponseDtos.add(ReviewResponseDto.toDto(review));
        }
        return reviewResponseDtos;
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> searchReviewsByCategory(Long category_name){
        List<ReviewResponseDto> reviewResponseDtos = getReviews();
        for(ReviewResponseDto reviewResponseDto: reviewResponseDtos){
            if(!reviewResponseDto.getCategory_name().equals(category_name)){
                reviewResponseDtos.remove(reviewResponseDto);
            }
        }
        return reviewResponseDtos;
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> searchReviewsByContent(String content){
        List<ReviewResponseDto> reviewResponseDtos = getReviews();
        for(ReviewResponseDto review : reviewResponseDtos){
            if(!review.getContent().contains(content)){
                reviewResponseDtos.remove(review);
            }
        }
        return reviewResponseDtos;
    }

    @Transactional
    public ReviewResponseDto updateReview(ReviewRequestDto reviewRequestDto, Long id){
        Review review = findReview(id);
        checkAuthorization(review);
        Category category = categoryRepository.findByName(reviewRequestDto.getCategory_name());
        review.setCategory(category);
        review.setRate(reviewRequestDto.getRate());
        review.setContent(reviewRequestDto.getContent());
        reviewRepository.save(review);
        return ReviewResponseDto.toDto(review);
    }

    @Transactional String deleteReview(Long id){
        Review review = reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        checkAuthorization(review);
        reviewRepository.delete(review);
        return "삭제 완료";
    }

    private Review findReview(Long id){
        Review review = reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        return review;
    }

    private Member getMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return member;
    }

    private void checkAuthorization(Review review){
        Member member = getMember();
        if(!review.getMember().equals(member)){
            throw new MemberNotAuthorized();
        }
    }
}
