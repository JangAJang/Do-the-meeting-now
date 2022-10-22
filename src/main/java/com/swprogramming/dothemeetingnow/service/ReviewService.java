package com.swprogramming.dothemeetingnow.service;

import com.swprogramming.dothemeetingnow.dto.review.ReviewRequestDto;
import com.swprogramming.dothemeetingnow.dto.review.ReviewResponseDto;
import com.swprogramming.dothemeetingnow.entity.Category;
import com.swprogramming.dothemeetingnow.entity.Member;
import com.swprogramming.dothemeetingnow.entity.Review;
import com.swprogramming.dothemeetingnow.entity.Station;
import com.swprogramming.dothemeetingnow.exception.*;
import com.swprogramming.dothemeetingnow.repository.CategoryRepository;
import com.swprogramming.dothemeetingnow.repository.MemberRepository;
import com.swprogramming.dothemeetingnow.repository.ReviewRepository;
import com.swprogramming.dothemeetingnow.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviews(){
        List<Review> reviews = getAllReviews();
        return changeEntityToDto(reviews);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> searchReviewByStation(Long stationId){
        List<Review> reviews = reviewRepository.findAllByStation(getStationById(stationId));
        if(reviews.isEmpty()) throw new ReviewNotFoundException();
        return changeEntityToDto(reviews);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> searchReviewsByContent(String content){
        List<Review> reviews = getAllReviews();
        reviews.removeIf(review -> !review.getContent().contains(content));
        return changeEntityToDto(reviews);
    }

    @Transactional
    public ReviewResponseDto writeReview(Long stationID, ReviewRequestDto reviewRequestDto){
        Review review = setReview(new Review(), reviewRequestDto);
        review.setStation(getStationById(stationID));
        reviewRepository.save(review);
        return ReviewResponseDto.toDto(review);
    }

    private Review setReview(Review review, ReviewRequestDto reviewRequestDto){
        review.setCategory(getCategory(reviewRequestDto.getCategory_name()));
        review.setRate(reviewRequestDto.getRate());
        review.setContent(reviewRequestDto.getContent());
        review.setMember(getMember());
        return review;
    }
    @Transactional
    public ReviewResponseDto updateReview(ReviewRequestDto reviewRequestDto, Long id){
        Review review = findReview(id);
        checkAuthorization(review);
        setReview(review, reviewRequestDto);
        reviewRepository.save(review);
        return ReviewResponseDto.toDto(review);
    }

    @Transactional
    public String deleteReview(Long id){
        Review review = findReview(id);
        checkAuthorization(review);
        reviewRepository.delete(review);
        return "삭제 완료";
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto getReview(Long id){
        return ReviewResponseDto.toDto(findReview(id));
    }

    private Review findReview(Long id){
        return reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
    }

    private Member getMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
    }

    private void checkAuthorization(Review review){
        Member member = getMember();
        if(!review.getMember().equals(member)){
            throw new MemberNotAuthorized();
        }
    }

    private List<Review> getAllReviews(){
        List<Review> reviews = reviewRepository.findAll();
        if(reviews.isEmpty()) throw new ReviewNotFoundException();
        return reviews;
    }

    private List<ReviewResponseDto> changeEntityToDto(List<Review> reviews){
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for(Review review : reviews){
            reviewResponseDtos.add(ReviewResponseDto.toDto(review));
        }
        return reviewResponseDtos;
    }

    private Station getStationById(Long id){
        return stationRepository.findById(id).orElseThrow(StationNotFoundException::new);
    }

    private Category getCategory(String name){
        return categoryRepository.findByName(name).orElseThrow(CategoryNotFoundException::new);
    }

}
