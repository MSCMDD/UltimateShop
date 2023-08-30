package cn.superiormc.ultimateshop.objects.caches;

import cn.superiormc.ultimateshop.managers.ConfigManager;
import cn.superiormc.ultimateshop.objects.ObjectItem;
import cn.superiormc.ultimateshop.utils.CommonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ObjectPlayerUseTimesCache {

    private int buyUseTimes;

    private int sellUseTimes;

    private LocalDateTime lastBuyTime;

    private LocalDateTime lastSellTime;

    private ObjectItem product;


    public ObjectPlayerUseTimesCache(int buyUseTimes,
                                     int sellUseTimes,
                                     String lastBuyTime,
                                     String lastSellTime,
                                     ObjectItem product) {
        this.buyUseTimes = buyUseTimes;
        this.lastBuyTime = CommonUtil.stringToTime(lastBuyTime);
        this.sellUseTimes = sellUseTimes;
        this.lastSellTime = CommonUtil.stringToTime(lastSellTime);
        this.product = product;
    }

    public int getBuyUseTimes() {
        return buyUseTimes;
    }

    public int getSellUseTimes() {
        return sellUseTimes;
    }

    public void setBuyUseTimes(int i) {
        buyUseTimes = i;
    }

    public void setSellUseTimes(int i) {
        sellUseTimes = i;
    }

    public String getLastBuyTime() {
        if (lastBuyTime == null) {
            return null;
        }
        return CommonUtil.timeToString(lastBuyTime);
    }

    public String getLastSellTime() {
        if (lastSellTime == null) {
            return null;
        }
        return CommonUtil.timeToString(lastSellTime);
    }

    public LocalDateTime getBuyRefreshTime() {
        String mode = product.getItemConfig().getString("limits-reset-mode",
                ConfigManager.configManager.getString("use-times.default-reset-mode"));
        String tempVal1 = product.getItemConfig().getString("limits-reset-time",
                ConfigManager.configManager.getString("use-times.default-reset-time"));
        if (mode.equals("TIMED")) {
            return getTimedBuyRefreshTime(tempVal1);
        }
        else if (mode.equals("TIMER")) {
            return getTimerBuyRefreshTime(tempVal1);
        }
        else {
            return LocalDateTime.now().withYear(1999);
        }
    }

    public LocalDateTime getSellRefreshTime() {
        String mode = product.getItemConfig().getString("limits-reset-time-mode", "NEVER");
        String tempVal1 = product.getItemConfig().getString("limits-reset-time", "00:00:00");
        if (mode.equals("TIMED")) {
            return getTimedSellRefreshTime(tempVal1);
        }
        else if (mode.equals("TIMER")) {
            return getTimerSellRefreshTime(tempVal1);
        }
        else {
            return LocalDateTime.now().withYear(1999);
        }
    }

    public String getBuyRefreshTimeDisplayName() {
        LocalDateTime tempVal1 = getBuyRefreshTime();
        if (tempVal1.getYear() == 1999) {
            return ConfigManager.configManager.getString("placeholder.refresh.never");
        }
        return CommonUtil.timeToString(tempVal1, ConfigManager.configManager.getString("placeholder.refresh.format"));
    }

    public String getSellRefreshTimeDisplayName() {
        LocalDateTime tempVal1 = getSellRefreshTime();
        if (tempVal1.getYear() == 1999) {
            return ConfigManager.configManager.getString("placeholder.refresh.never");
        }
        return CommonUtil.timeToString(tempVal1, ConfigManager.configManager.getString("placeholder.refresh.format"));
    }

    private LocalDateTime getTimedBuyRefreshTime(String time) {
        String tempVal1 = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        tempVal1 = tempVal1 + " " + time;
        LocalDateTime refreshResult = CommonUtil.stringToTime(tempVal1);
        if (lastBuyTime.isAfter(refreshResult)) {
            refreshResult.plusDays(1L);
        }
        return refreshResult;
    }

    private LocalDateTime getTimedSellRefreshTime(String time) {
        String tempVal1 = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        tempVal1 = tempVal1 + " " + time;
        LocalDateTime refreshResult = CommonUtil.stringToTime(tempVal1);
        if (lastSellTime.isAfter(refreshResult)) {
            refreshResult.plusDays(1L);
        }
        return refreshResult;
    }

    private LocalDateTime getTimerBuyRefreshTime(String time) {
        LocalDateTime refreshResult = lastBuyTime;
        refreshResult.plusHours(Long.parseLong(time.split(":")[0]));
        refreshResult.plusMinutes(Long.parseLong(time.split(":")[1]));
        refreshResult.plusSeconds(Long.parseLong(time.split(":")[2]));
        return refreshResult;
    }

    private LocalDateTime getTimerSellRefreshTime(String time) {
        LocalDateTime refreshResult = lastSellTime;
        refreshResult.plusHours(Long.parseLong(time.split(":")[0]));
        refreshResult.plusMinutes(Long.parseLong(time.split(":")[1]));
        refreshResult.plusSeconds(Long.parseLong(time.split(":")[2]));
        return refreshResult;
    }
}
