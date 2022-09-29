package com.swprogramming.dothemeetingnow.controller;

import com.swprogramming.dothemeetingnow.dto.route.AddRouteRequestDto;
import com.swprogramming.dothemeetingnow.response.Response;
import com.swprogramming.dothemeetingnow.service.RouteService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/route")
public class RouteController {

    private final RouteService routeService;

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/")
    public Response addRoute(@RequestBody AddRouteRequestDto addRouteRequestDto){
        return Response.success(routeService.addRoute(addRouteRequestDto));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Response updateRoute(@RequestBody AddRouteRequestDto addRouteRequestDto, @RequestParam("${id}") Long id){
        return Response.success(routeService.updateRoute(addRouteRequestDto, id));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public Response deleteRoute(@RequestParam("${id}")Long id){
        return Response.success(routeService.deleteRoute(id));
    }

}
