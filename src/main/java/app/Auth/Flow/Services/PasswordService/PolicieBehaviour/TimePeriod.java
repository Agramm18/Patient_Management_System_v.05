package app.Auth.Flow.Services.PasswordService.PolicieBehaviour;

import java.time.Period;

public enum TimePeriod {
    DAY(Period.ofDays(1), 1),
    WEEK(Period.ofWeeks(1), 2),
    MONTH(Period.ofMonths(1), 4),
    YEAR(Period.ofYears(1), 8),
    FIVE_YEARS(Period.ofYears(5), 25),
    TEN_YEARS(Period.ofYears(10), 50);

    private final Period period;
    private final int threshholdFactor;

    TimePeriod(Period period, int threshholdFactor) {
        this.period = period;
        this.threshholdFactor = threshholdFactor;
    }

    public Period getPeriod() {
        return period;
    }

    public int getThreshholdFactor() {
        return threshholdFactor;
    }

    public int calculateThreshold(PolicyThreshold threshold) {
        return Math.multiplyExact(threshold.getRetryCount(), threshold.getRetryCount());
    }
}
