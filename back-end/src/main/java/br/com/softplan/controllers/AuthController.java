package br.com.softplan.controllers;

import br.com.softplan.controllers.forms.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Authentication Micro-Services Controller", value = "AuthController", description = "Controller for Authentication Micro-Services")
public class AuthController {

    @ApiOperation(value = "Performs authentication on the system",
            notes = "This endpoint performs authentication through a user object")
    @PostMapping("/login")
    public void fakeLogin(@RequestBody UserForm user) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }
}