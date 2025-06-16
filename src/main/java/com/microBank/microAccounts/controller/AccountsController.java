package com.microBank.microAccounts.controller;

import com.microBank.microAccounts.DTO.AccountsContactDto;
import com.microBank.microAccounts.DTO.CustomerDTO;
import com.microBank.microAccounts.DTO.ErrorResponceDTO;
import com.microBank.microAccounts.DTO.ResponceDTO;
import com.microBank.microAccounts.constants.AccountsConstants;
import com.microBank.microAccounts.service.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "Accounts  CRUD controller",
        description = "CRUD operations for accounts microservice"
)
@RestController
@RequestMapping(path = "/api", produces =( MediaType.APPLICATION_JSON_VALUE))
@Validated
public class AccountsController {

    @Autowired
    private AccountsContactDto accountsContactDto;

    private final IAccountService accountService ;

    public AccountsController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    private Environment environment;

    // using @Value annotation to get build version from application.yml
    @Value("${build.version}")
    private String buildVersion;

    @Operation(
            summary = "Create account",
            description = "Creates a new account for customer"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 created"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponceDTO> createAccount(@Valid @RequestBody CustomerDTO customerDTO) {

        accountService.createAccount(customerDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponceDTO(AccountsConstants.STATUS_201 , AccountsConstants.MESSAGE_201));

    }

    @Operation(
            summary = "Fetch Account Details REST API",
            description = "REST API to fetch Customer &  Account details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponceDTO.class)
                    )
            )
    }
    )
    @GetMapping("/fetch/{mobileNumber}")
    public ResponseEntity<CustomerDTO> fetchAccountDetails(@PathVariable String mobileNumber) {

        CustomerDTO customerDTO = accountService.fetchAccount(mobileNumber) ;
        return  ResponseEntity.status(HttpStatus.OK).body(customerDTO);

    }


    @Operation(
            summary = "Update Account Details REST API",
            description = "REST API to update Customer &  Account details based on a account number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponceDTO.class)
                    )
            )
    }
    )
    @PutMapping("/update")
    public ResponseEntity<ResponceDTO> updateAccountDetails( @Valid @RequestBody CustomerDTO customerDTO) {
        boolean isUpdated = accountService.updateAccount(customerDTO) ;
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponceDTO(AccountsConstants.STATUS_200 , AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceDTO(AccountsConstants.STATUS_500  , AccountsConstants.MESSAGE_500));
        }
    }



    @Operation(
            summary = "Delete Account & Customer Details REST API",
            description = "REST API to delete Customer &  Account details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponceDTO.class)
                    )
            )
    }
    )
    @DeleteMapping("/delete/{mobileNumber}")
    public ResponseEntity<ResponceDTO> deleteAccountDetails(@PathVariable @Pattern(regexp = "\\d{10}" ) String mobileNumber) {
        boolean isDeleted = accountService.deleteAccount(mobileNumber) ;
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponceDTO(AccountsConstants.STATUS_200 , AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceDTO(AccountsConstants.STATUS_500  , AccountsConstants.MESSAGE_500));
        }
    }

    @Operation(
            summary = "fetch build version REST API",
            description = "REST API to fetch build version"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponceDTO.class)
                    )
            )
    }
    )
    @GetMapping("/version")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    } // return build version from application.yml file>

    @Operation(
            summary = "fetch version of java REST API",
            description = "REST API to fetch version of java"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponceDTO.class)
                    )
            )
    }
    )
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
            .body(environment.getProperty("JAVA_HOME"));
    }


    @Operation(
            summary = "fetch the contact info related to this microservice",
            description = "REST API to fetch the contact info related to this microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponceDTO.class)
                    )
            )
    }
    )
    @GetMapping("/getcontactinfo")
    public ResponseEntity<AccountsContactDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactDto);
    }
}
