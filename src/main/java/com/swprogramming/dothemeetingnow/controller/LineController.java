package com.swprogramming.dothemeetingnow.controller;

import com.swprogramming.dothemeetingnow.dto.line.LineDto;
import com.swprogramming.dothemeetingnow.response.Response;
import com.swprogramming.dothemeetingnow.service.LineService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/line")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public Response getAllLine(){
        return Response.success(lineService.getAllLines());
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/searchByName")
    public Response searchLineByName(@RequestBody String name){
        return Response.success(lineService.searchAllLineByName(name));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/")
    public Response addLine(@RequestBody LineDto lineDto){
        return Response.success(lineService.createLine(lineDto));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Response updateLine(@RequestParam("${id}")Long id, @RequestBody LineDto lineDto){
        return Response.success(lineService.updateLine(lineDto, id));
    }

    @ApiOperation(value = "", notes = "")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public Response deleteLine(@RequestParam("${id}")Long id){
        return Response.success(lineService.deleteLine(id));
    }

}
