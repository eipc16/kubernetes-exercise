package com.piisw.cinema_tickets_app.domain.screening.boundary;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ScreeningController.MAIN_PATH)
public class ScreeningController {

    public static final String MAIN_PATH = "/screening";

}
