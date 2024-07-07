package com.example.bank_app.service;

import com.example.bank_app.dto.AccountDTO;
import com.example.bank_app.dto.TransferRequestDTO;
import com.example.bank_app.dto.TransferResponseDTO;
import com.example.bank_app.entity.Account;
import com.example.bank_app.entity.Transaction;
import com.example.bank_app.exception.AccountNotFoundException;
import com.example.bank_app.exception.InsufficientBalanceException;
import com.example.bank_app.repository.AccountRepository;
import com.example.bank_app.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Account createAccount(AccountDTO accountDTO) {
        Account account = new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setAccountHolderName(accountDTO.getAccountHolderName());
        account.setBalance(accountDTO.getBalance());

        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
    }

    @Transactional
    public TransferResponseDTO transferFunds(TransferRequestDTO transferRequestDTO) {
        Account fromAccount = accountRepository.findById(transferRequestDTO.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException("From account not found"));

        Account toAccount = accountRepository.findById(transferRequestDTO.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException("To account not found"));

        if (fromAccount.getBalance() < transferRequestDTO.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance in the account: " + fromAccount.getAccountNumber());
        }

        fromAccount.setBalance(fromAccount.getBalance() - transferRequestDTO.getAmount());
        toAccount.setBalance(toAccount.getBalance() + transferRequestDTO.getAmount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setFromAccountId(transferRequestDTO.getFromAccountId());
        transaction.setToAccountId(transferRequestDTO.getToAccountId());
        transaction.setAmount(transferRequestDTO.getAmount());

        transactionRepository.save(transaction);

        logger.info("Transferred {} from account {} to account {}", transferRequestDTO.getAmount(), fromAccount.getAccountNumber(), toAccount.getAccountNumber());

        return new TransferResponseDTO("Transfer successful");
    }

    @Transactional
    public Account deposit(Long accountId, Double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));

        account.setBalance(account.getBalance() + amount);

        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(Long accountId, Double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));

        account.setBalance(account.getBalance() - amount);

        return accountRepository.save(account);
    }
}
