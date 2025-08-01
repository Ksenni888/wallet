package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Wallet;
import org.example.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
@Slf4j
@RequiredArgsConstructor
public class WalletController {
    @Autowired
    private final WalletService walletService;

    @PostMapping("/wallet")
    public double changeBalance(@Valid @RequestBody Wallet wallet) {
        return walletService.changeBalance(wallet);
    }

    @GetMapping("/wallets/{id}")
    public double getBalance(@Valid @PathVariable int id) {
        return walletService.getBalance(id);
    }
}