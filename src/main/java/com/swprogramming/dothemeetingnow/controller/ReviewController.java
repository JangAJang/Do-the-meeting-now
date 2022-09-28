package com.swprogramming.dothemeetingnow.controller;

import com.swprogramming.dothemeetingnow.dto.review.ReviewRequestDto;
import com.swprogramming.dothemeetingnow.response.Response;
import com.swprogramming.dothemeetingnow.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/ofStation/{id}")
    public Response writeReview(@RequestParam("${id}") Long id, @RequestBody ReviewRequestDto reviewRequestDto){
        return Response.success(reviewService.writeReview(id, reviewRequestDto));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Response updateReview(@RequestParam("${id}") Long id, @RequestBody ReviewRequestDto reviewRequestDto){
        return Response.success(reviewService.updateReview(reviewRequestDto, id));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public Response deleteReview(@RequestParam("${id}") Long id){
        return Response.success(reviewService.deleteReview(id));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Response showReview(@RequestParam("${id}")Long id){
        return Response.success(reviewService.getReview(id));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public Response getAllReviews(){
        return Response.success(reviewService.getReviews());
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ofStation/{id}")
    public Response getReviewOfStation(@RequestParam("${id}")Long id){
        return Response.success(reviewService.getStationsReviews(id));
    }


}
