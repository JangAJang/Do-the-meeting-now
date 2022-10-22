package com.swprogramming.dothemeetingnow.controller;


import com.swprogramming.dothemeetingnow.dto.station.AddStationRequestDto;
import com.swprogramming.dothemeetingnow.response.Response;
import com.swprogramming.dothemeetingnow.service.StationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/station")
public class StationController {

    private final StationService stationService;

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public Response getAllStation(){
        return Response.success(stationService.searchAllStation());
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/")
    public Response addStation(@RequestBody AddStationRequestDto addStationRequestDto){
        return Response.success(stationService.addStation(addStationRequestDto));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Response updateStation(@RequestParam("${id}")Long id, @RequestBody AddStationRequestDto addStationRequestDto){
        return Response.success(stationService.updateStation(addStationRequestDto, id));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public Response deleteStation(@RequestParam("${id}")Long id){
        return Response.success(stationService.deleteStation(id));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/searchName/")
    public Response searchStationByName(@RequestBody String name){
        return Response.success(stationService.searchStationByName(name));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/searchLine/")
    public Response searchStationByLine(@RequestBody String line){
        return Response.success(stationService.searchStationByLine(line));
    }



}
