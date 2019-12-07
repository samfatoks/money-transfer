package com.demo.moneytransfer.resource;

import com.demo.moneytransfer.dto.AccountDTO;
import com.demo.moneytransfer.dto.ResponseDTO;
import com.demo.moneytransfer.dto.mapper.AccountMapper;
import com.demo.moneytransfer.exception.AppException;
import com.demo.moneytransfer.domain.Account;
import com.demo.moneytransfer.service.AccountService;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Path("account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private AccountService accountService;

    @Inject
    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    public Response getAccounts() throws Exception {
        List<AccountDTO> accounts = AccountMapper.toDTOList(accountService.findAllAccounts());
        ResponseDTO responseDTO = new ResponseDTO(0, "successful");
        responseDTO.setData(accounts);
        return Response
                .status(Response.Status.OK)
                .entity(responseDTO)
                .build();
    }

    @GET
    @Path("/{username}")
    public Response getAccount(@PathParam("username") String username) throws Exception {
        Account account = accountService.findAccountByUsername(username)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND_404, 4, "Invalid username - " + username));

        ResponseDTO responseDTO = new ResponseDTO(0, "successful");
        responseDTO.setData(AccountMapper.toDTO(account));

        return Response
                .status(Response.Status.OK)
                .entity(responseDTO)
                .build();
    }

    @POST
    public Response createAccount(@Valid AccountDTO accountDTO) throws Exception {
        Account account = AccountMapper.fromDTO(accountDTO);

        Long id = accountService.createAccount(account);

        ResponseDTO responseDTO = new ResponseDTO(0, "Account created successfully");
        return Response
                .status(Response.Status.CREATED)
                .entity(responseDTO)
                .build();
    }

}
