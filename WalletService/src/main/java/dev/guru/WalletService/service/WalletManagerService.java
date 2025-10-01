package dev.guru.WalletService.service;

import dev.guru.WalletService.domain.Wallet;
import dev.guru.WalletService.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletManagerService {

    private final WalletRepo walletRepo;

    @Autowired
    public WalletManagerService(WalletRepo walletRepo) {
        this.walletRepo = walletRepo;
    }

    public Wallet createWallet(Long id) {
        Wallet wallet = new Wallet
                .Builder()
                .withUserId(id)
                .withBalance(0.0)
                .build();
        return walletRepo.save(wallet);
    }
}
