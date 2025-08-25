package org.yzh.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yzh.commons.model.APIResult;
import org.yzh.web.service.T1078DataReader;

@Tag(name = "T1078 Status", description = "T1078 data packet reader status")
@RestController
@RequestMapping("/api/t1078")
public class T1078StatusController {

    @Autowired
    private T1078DataReader t1078DataReader;

    @Operation(summary = "Get T1078 status", description = "Get T1078 data packet reader status")
    @GetMapping("/status")
    public APIResult<T1078DataReader.T1078Status> getT1078Status() {
        return APIResult.ok(t1078DataReader.getStatus());
    }
}
