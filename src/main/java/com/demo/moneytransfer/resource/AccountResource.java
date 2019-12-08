package com.demo.moneytransfer.resource;

import com.demo.moneytransfer.domain.Account;
import com.demo.moneytransfer.dto.AccountDTO;
import com.demo.moneytransfer.dto.ResponseDTO;
import com.demo.moneytransfer.dto.mapper.AccountMapper;
import com.demo.moneytransfer.exception.AppException;
import com.demo.moneytransfer.service.AccountService;
import org.eclipse.jetty.http.HttpStatus;

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

    @DELETE
    @Path("/{username}")
    public Response deleteAccount(@PathParam("username") String username) throws Exception {
        Account account = accountService.findAccountByUsername(username)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND_404, 4, "Invalid username - " + username));

        accountService.deleteAccount(username);

        ResponseDTO responseDTO = new ResponseDTO(0, "successful");
        return Response
                .status(Response.Status.OK)
                .entity(responseDTO)
                .build();
    }

    @GET
    @Path("{username}/debit")
    public Response debitAmount(@PathParam("username") String username, @QueryParam("amount") double amount) throws Exception {
        BigDecimal amountBD = BigDecimal.valueOf(amount).setScale(2,
                BigDecimal.ROUND_HALF_EVEN);
        boolean status = accountService.debitAccount(username, amountBD);

        if(status) {
            ResponseDTO responseDTO = new ResponseDTO(0, "Debit successful");
            return Response
                    .status(Response.Status.OK)
                    .entity(responseDTO)
                    .build();
        } else {
            ResponseDTO responseDTO = new ResponseDTO(1, "Debit request failed");
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseDTO)
                    .build();
        }
    }

    @GET
    @Path("{username}/credit")
    public Response creditAmount(@PathParam("username") String username, @QueryParam("amount") double amount) throws Exception {
        BigDecimal amountBD = BigDecimal.valueOf(amount).setScale(2,
                BigDecimal.ROUND_HALF_EVEN);
        boolean status = accountService.creditAccount(username, amountBD);
        if(status) {
            ResponseDTO responseDTO = new ResponseDTO(0, "Credit successful");
            return Response
                    .status(Response.Status.OK)
                    .entity(responseDTO)
                    .build();
        } else {
            ResponseDTO responseDTO = new ResponseDTO(1, "Credit request failed");
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseDTO)
                    .build();
        }
    }

    @GET
    @Path("{username}/transfer")
    public Response transfer(@PathParam("username") String srcUsername, @QueryParam("to") String destUsername, @QueryParam("amount") double amount) throws Exception {
        if(srcUsername.equalsIgnoreCase(destUsername)) {
            throw new AppException(HttpStatus.BAD_REQUEST_400, 8, "Invalid transfer: source and destination accounts are the same");
        }
        BigDecimal amountBD = BigDecimal.valueOf(amount).setScale(2,
                BigDecimal.ROUND_HALF_EVEN);
        boolean status = accountService.transfer(srcUsername, destUsername, amountBD);
        String successfulMessage = "You have successfully transferred " + amount + " USD to " + destUsername;
        String failureMessage = "Money transfer failed";
        if(status) {
            ResponseDTO responseDTO = new ResponseDTO(0, successfulMessage);
            return Response
                    .status(Response.Status.OK)
                    .entity(responseDTO)
                    .build();
        } else {
            ResponseDTO responseDTO = new ResponseDTO(1, failureMessage);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseDTO)
                    .build();
        }
    }
}
