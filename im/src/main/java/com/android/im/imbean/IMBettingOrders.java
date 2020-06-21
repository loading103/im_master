package com.android.im.imbean;

public class IMBettingOrders {
    private String amount;
    private String bettingOrderId;
    private String gameId;
    private String gameName;
    private String investAmount;
    private String investContent;
    private String status;
    private String tradeTime;

    public IMBettingOrders() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBettingOrderId() {
        return bettingOrderId;
    }

    public void setBettingOrderId(String bettingOrderId) {
        this.bettingOrderId = bettingOrderId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getInvestContent() {
        return investContent;
    }

    public void setInvestContent(String investContent) {
        this.investContent = investContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }
}
