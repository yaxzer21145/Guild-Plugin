package com.guild.guild;

import java.util.HashMap;
import java.util.Map;

public class GuildBank {
    
    private long balance;
    private Map<String, Map<Long, Long>> depositHistory; // 玩家名 -> (时间戳 -> 金额)
    private Map<String, Map<Long, Long>> withdrawHistory; // 玩家名 -> (时间戳 -> 金额)
    
    public GuildBank() {
        this.balance = 0;
        this.depositHistory = new HashMap<>();
        this.withdrawHistory = new HashMap<>();
    }
    
    public long getBalance() {
        return balance;
    }
    
    public void setBalance(long balance) {
        this.balance = balance;
    }
    
    public boolean deposit(long amount) {
        if (amount <= 0) return false;
        balance += amount;
        return true;
    }
    
    public boolean withdraw(long amount) {
        if (amount <= 0) return false;
        if (balance < amount) return false;
        balance -= amount;
        return true;
    }
    
    public void addDepositRecord(String playerName, long amount) {
        depositHistory.computeIfAbsent(playerName, k -> new HashMap<>())
                     .put(System.currentTimeMillis(), amount);
    }
    
    public void addWithdrawRecord(String playerName, long amount) {
        withdrawHistory.computeIfAbsent(playerName, k -> new HashMap<>())
                      .put(System.currentTimeMillis(), amount);
    }
    
    public Map<String, Map<Long, Long>> getDepositHistory() {
        Map<String, Map<Long, Long>> result = new HashMap<>();
        depositHistory.forEach((player, records) -> 
            result.put(player, new HashMap<>(records)));
        return result;
    }
    
    public Map<String, Map<Long, Long>> getWithdrawHistory() {
        Map<String, Map<Long, Long>> result = new HashMap<>();
        withdrawHistory.forEach((player, records) -> 
            result.put(player, new HashMap<>(records)));
        return result;
    }
    
    public void clearHistory() {
        depositHistory.clear();
        withdrawHistory.clear();
    }
}
