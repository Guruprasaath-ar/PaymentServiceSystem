package dev.guru.WalletService.controller;

import dev.guru.WalletService.domain.Wallet;
import dev.guru.WalletService.service.WalletManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletManagerService walletManagerService;

    @Autowired
    public WalletController(WalletManagerService walletManagerService) {
        this.walletManagerService = walletManagerService;
    }

    @PostMapping("")
    public Wallet createWallet(@RequestParam Long id){
        return walletManagerService.createWallet(id);
    }


}
