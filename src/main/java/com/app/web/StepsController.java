package com.app.web;

import com.app.dao.StepsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StepsController {
    @Autowired
    private StepsDao stepsDao;
}
